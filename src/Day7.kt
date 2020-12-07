import java.io.File
import java.util.*

fun main() {
    val bagRegex = Regex("(.*) bags contain (.*)")
    val containsRegex = Regex("([0-9])+ (.*) bags?.?")

    val dict = mutableMapOf<String, List<Pair<String, Int>>>()
    File("src/input/day7.txt").readLines().map  {
        val match = bagRegex.matchEntire(it.trim())
        val bagType = match!!.groupValues[1]
        val contains = getContains(containsRegex, match!!.groupValues[2])
        dict[bagType] = contains
    }

    val bagColorCount = getBagColorCount(dict, "shiny gold")
    println("Part 1:  $bagColorCount")
    val bagsContainedCount = getBagsContainedCount(dict, "shiny gold", mutableMapOf())
    println("Part 2: $bagsContainedCount")
}

fun getContains(containsRegex: Regex, string: String) : List<Pair<String, Int>> {
    return when (string) {
        "no other bags." -> emptyList()
        else -> {
            string.split(",").mapNotNull {
                val groupValues = containsRegex.matchEntire(it.trim())!!.groupValues
                groupValues[2] to groupValues[1].toInt()
            }
        }
    }
}

fun getBagColorCount(dict: Map<String, List<Pair<String, Int>>>, bagType: String) : Int {
    var count = 0
    val memo = mutableMapOf<String, Boolean>()
    dict.entries.map {
        if (it.key != bagType && search(it.key, bagType, dict, mutableMapOf(), memo)) {
            count++
        }
    }
    return count
}

fun search(source: String, destination: String, graph: Map<String, List<Pair<String, Int>>>, visited: MutableMap<String, Boolean>, memo: MutableMap<String, Boolean>) : Boolean {
    val next: Queue<String> = LinkedList<String>()
    next.add(source)
    while (!next.isEmpty()) {
        val current = next.poll()
        if (visited[current] == true) {
            continue
        }
        visited[current] = true
        if (destination == current || memo[current] == true) {
            memo[current] = true
            return true
        }
        val neighbors = graph[current]
        neighbors?.let {
            next.addAll(it.map { pair -> pair.first })
        }
    }
    return false
}

fun getBagsContainedCount(dict: Map<String, List<Pair<String, Int>>>, bagType: String, memo: MutableMap<String, Int>) : Int {
    memo[bagType]?.let {
        return it
    }
    val bags = dict[bagType] ?: return 0
    var count = bags.sumBy { it.second }
    for (bag in bags) {
        count += (getBagsContainedCount(dict, bag.first, memo) * bag.second)
    }
    memo[bagType] = count
    return count
}