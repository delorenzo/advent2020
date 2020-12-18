import java.io.File
import java.lang.Exception
//4696493914530
fun main() {
    val sum = File("src/input/day18.txt").readLines().map {
        homework(it.split(" ")).toLong()
    }.sum()

    println("Part one:  $sum")

    val sum2 = File("src/input/day18.txt").readLines().map {
        homework2(it.split(" ").toMutableList()).toLong()
    }.sum()

    println("Part two:  $sum2")
}

fun homework(s: String): Long {
    return homework(s.split(" "))
}

fun getNextTerm(current: String, startingIndex:Int, s: List<String>) : Pair<Long, Int> {
    var next = current
    var index = startingIndex
    return when(next.length) {
        1 -> next.toLong() to index
        else -> {
            val term = mutableListOf(next)
            var unClosedParens = next.count { it == '(' }
            do {
                index++
                next = s[index]
                term.add(next)
                if (next.contains("(")) {
                    unClosedParens++
                }
                else if (next.contains(")")) {
                    val parenCount = next.count { it == ')' }
                    unClosedParens -= parenCount
                }
            } while (unClosedParens > 0)

            term[0] = term[0].substring(1, term[0].length)
            term[term.size-1] = term[term.size-1].substring(0, term[term.size-1].length-1)
            homework(term) to index
        }
    }
}

fun homework(s: List<String>): Long {
    var index = 0
    var nextTerm = getNextTerm(s[0], index, s)
    index = nextTerm.second
    var first = nextTerm.first
    while (index < s.size-1) {
        index++
        val operation = s[index]
        index++

        var next = s[index]
        nextTerm = getNextTerm(next, index, s)
        val second = nextTerm.first
        index = nextTerm.second

        first = when (operation) {
            "+" -> first + second
            "-" -> first - second
            "*" -> first * second
            else -> throw Exception("Unexpected operator.")
        }
    }
    return first
}

fun homework2(s: String): Long {
    return homework2(s.split(" ").toMutableList())
}

fun getNextTerm2(current: String, startingIndex:Int, s: List<String>) : Pair<Long, Int> {
    var next = current
    var index = startingIndex
    return when(next.length) {
        1 -> next.toLong() to index
        else -> {
            val term = mutableListOf(next)
            var unClosedParens = next.count { it == '(' }
            do {
                index++
                next = s[index]
                term.add(next)
                if (next.contains("(")) {
                    unClosedParens++
                }
                else if (next.contains(")")) {
                    val parenCount = next.count { it == ')' }
                    unClosedParens -= parenCount
                }
            } while (unClosedParens > 0)

            term[0] = term[0].substring(1, term[0].length)
            term[term.size-1] = term[term.size-1].substring(0, term[term.size-1].length-1)
            homework2(term) to index
        }
    }
}

fun homework2(s: MutableList<String>): Long {
    var index = 0
    var nextTerm = getNextTerm2(s[0], index, s)
    index = nextTerm.second
    var first = nextTerm.first
    val toMultiply = mutableListOf<Long>()
    while (index < s.size-1) {
        index++
        val operation = s[index]
        index++

        var next = s[index]
        nextTerm = getNextTerm2(next, index, s)
        val second = nextTerm.first
        index = nextTerm.second

        first = when (operation) {
            "+" -> first + second
            "-" -> first - second
            "*" -> {
                toMultiply.add(first)
                second
            }
            else -> throw Exception("Unexpected operator.")
        }
    }
    return toMultiply.fold(first, { acc: Long, l: Long ->  acc * l})
}