import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

// not 448
// not 18

//part 2 400 is too high

val simpleRegex = Regex("\"([a-z])\"")
fun main() {
    val ruleRegex = Regex("(\\d+):(.*)")
    var rulesFinished: Boolean = false
    val rules = mutableMapOf<Int, Rule>()
    var messages = mutableListOf<String>()
    val matchDict = mutableMapOf<Int, Set<String>>()
    File("src/input/day19.txt").readLines().map {
        if (it == "") {
            rulesFinished = true
        }
        if (!rulesFinished) {
            val match = ruleRegex.matchEntire(it)
            val rule = Rule(match!!.groupValues[1].toInt(), match.groupValues[2].trim())
            rules[rule.num] = rule
        }
        else {
            messages.add(it)
        }
    }
    messages = messages.filterNot { it.isBlank() }.toMutableList()

    generateSimpleRules(rules, matchDict)
    generateMatchesIterative(rules, matchDict)
    val matchingMessages = matchDict[0]!!
    val part1 = messages.count { matchingMessages.contains(it) }
    println("Part 1:  $part1")

    val regex42 = makeRegex(42, matchDict)
    val regex31 = makeRegex(31, matchDict)
    val rulesRegexString = generateInitalString().replace("42", regex42).replace("31", regex31)
    val rulesRegex = Regex(rulesRegexString)
    val count = messages.count{it.matches(rulesRegex)}
    println("Part 2:  $count")
}

fun generateInitalString() : String {
    val sb = StringBuilder("^(")
    sb.append("(42+)")
    sb.append("((4231)|")
    for (i in 2..10) {
        sb.append("(42{$i}31{$i})|")
    }
    sb.deleteCharAt(sb.length-1) //delete last "|"
    sb.append("))$")
    return sb.toString()
}

fun makeRegex(num: Int, matchDict: Map<Int, Set<String>>) : String {
    val sb = StringBuilder()
    (matchDict[num] ?: error("")).map { sb.append(it).append("|") }
    sb.deleteCharAt(sb.length-1)
    return "(${sb})"
}

fun generateSimpleRules(rules: Map<Int, Rule>, matchDict: MutableMap<Int, Set<String>>) {
    rules.values.map {
        if (simpleRegex.matches(it.match.trim())) {
           matchDict[it.num] = setOf(simpleRegex.matchEntire(it.match.trim())!!.groupValues[1])
        }
    }
}

fun generateMatchesIterative(rules: Map<Int, Rule>, matchDict: MutableMap<Int, Set<String>>) {
    val queue = LinkedList<Pair<Int, String>>()
    val outstandingRules = mutableMapOf<Int, Int>()
    rules.values.map {
        if (!matchDict.containsKey(it.num)) {
            queue.add(it.num to it.match)
            outstandingRules[it.num] = 1
        } else {
            outstandingRules[it.num] = 0
        }
    }
    loop@ while (!queue.isEmpty()) {
        val current = queue.poll()
        val num = current.first
        val match = current.second
        when {
            matchDict.containsKey(num) && outstandingRules[num]!! <= 0 -> {
                matchDict[num]!!
            }
            simpleRegex.matches(match) -> {
                val match = simpleRegex.matchEntire(match)
                matchDict[num] = setOf(match!!.groupValues[1])
                outstandingRules[num] = outstandingRules[num]!! - 1
            }
            match.contains("|") -> {
                val subrules = match.split("|").map { s -> s.trim() }
                subrules.map {rule ->
                    queue.add(num to rule)
                    outstandingRules[num] = outstandingRules[num] +1
                }
                outstandingRules[num] = outstandingRules[num]!! -1
            }
            else -> {
                var completedSet = true
                val ruleSet = match.trim().split(" ").map{ it.toInt() }.map { ruleNum ->
                    if (matchDict.containsKey(ruleNum) && outstandingRules[ruleNum]!! <= 0) {
                        matchDict[ruleNum]!!
                    }
                    else {
                        completedSet = false
                        // ask for this rule to be solved next
                        val next = ruleNum to (rules[ruleNum] ?: error("")).match
                        if (!queue.contains(next)) {
                            queue.add(next)
                        }
                        setOf()
                    }
                }
                if (!completedSet) {
                    // can't solve this right now, pass
                    if (!queue.contains(current)) {
                        queue.add(current)
                    }
                }
                if (completedSet) {
                    val matchSet = ruleSet.fold(setOf("")) { acc: Set<String>, list: Set<String> ->
                        acc.map { buildString ->
                            list.map { newString ->
                                buildString + newString
                            }
                        }.flatten().toSet()
                    }
                    if (matchDict.containsKey(num)) {
                        val otherMatches = matchDict[num]!!
                        matchDict[num] = setOf(matchSet, otherMatches).flatten().toSet()
                    } else {
                        matchDict[num] = matchSet
                    }
                    outstandingRules[num] = outstandingRules[num]!! - 1
                }
            }
        }
    }
}
data class Rule(val num: Int, var match: String)
