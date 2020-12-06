import java.io.File

var totalAnyoneCount = 0
var totalEveryoneCount = 0
var groupCount = 0
val dict = mutableMapOf<Char, Int>()

fun main() {
    val lines = File("src/input/day6.txt").readLines().map { it.trim() }
    lines.mapIndexed { index, it ->
        if (it.isBlank()) {
            incrementCount()
            resetValues()
        } else {
            groupCount++
            it.forEach { char ->
                if (dict.getOrDefault(char, 0) == 0) {
                    dict[char] = 1
                } else {
                    dict[char] += 1
                }
            }
            if (index == lines.size-1) {
                incrementCount()
            }
        }
    }
    println("Part 1:  $totalAnyoneCount")
    println("Part 2:  $totalEveryoneCount")
}

operator fun Int?.plus(other: Int) = this?.plus(other) ?: other

private fun incrementCount() {
    totalAnyoneCount += dict.entries.count { it.value > 0 }
    totalEveryoneCount += dict.entries.count { it.value == groupCount }
}

private fun resetValues() {
    groupCount = 0
    dict.clear()
}
