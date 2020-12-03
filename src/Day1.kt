import java.io.File
import kotlin.math.exp

fun main() {
    val expenses = File("src/input/day1.txt").readLines().map { it.trim().toInt() }
    val dict = mutableMapOf<Int, Int>()
    // Part 1
    for (expense in expenses) {
        val remaining = 2020 - expense
        if (dict.containsKey(remaining)) {
            val answer = expense * remaining
            println("Part 1 Result:  $answer")
            break
        }
        dict[expense] = expense
    }
    // Part 2
    val dict2 = mutableMapOf<Int, Pair<Int, Int>>()
    for (i in 0 until expenses.size-1) {
        for (j in i+1 until expenses.size-1) {
            val sum = expenses[i] + expenses[j]
            dict2[sum] = (expenses[i] to  expenses[j])
        }
    }
    for (expense in expenses) {
        val remaining = 2020 - expense
        if (dict2.containsKey(remaining)) {
            dict2[remaining]?.let {
                val answer = expense * it.first * it.second
                println("$remaining")
                println("$expense ${it.first} ${it.second}")
                println("Part 2 result :  $answer")
                return
            }
        }
    }
}