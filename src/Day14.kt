import java.io.File
import java.util.*

fun main() {
    val maskRegex = Regex(".*mask = ([X01]+).*")
    val instructionRegex = Regex(".*mem\\[([0-9]+)\\] = ([0-9]+).*")
    val input = File("src/input/day14.txt").readLines()

    partOne(input, maskRegex, instructionRegex)
    partTwo(input, maskRegex, instructionRegex)
}

fun partOne(input: List<String>, maskRegex: Regex, instructionRegex: Regex) {
    val memory = LongArray(100000)
    var mask = ""
    input.map {
        val maskMatch = maskRegex.matchEntire(it.trim())
        if (maskMatch != null) {
            mask = maskMatch.groupValues[1]
        } else {
            val instructionMatch = instructionRegex.matchEntire(it.trim())
            instructionMatch!!
            val address = instructionMatch.groupValues[1].toInt()
            val value = instructionMatch.groupValues[2].toInt()

            var binaryString = Integer.toBinaryString(value).padStart(36, '0')
            val newString = binaryString.mapIndexed { index, char ->
                when (mask[index]) {
                    '0' -> '0'
                    '1' -> '1'
                    'X' -> char
                    else -> throw Exception()
                }
            }.joinToString("")
            val longValue = java.lang.Long.parseLong(newString, 2)
            memory[address] = longValue
        }
    }
    println("Part 1 is ${memory.sum()}")
}

fun partTwo(input: List<String>, maskRegex: Regex, instructionRegex: Regex) {
    val memory = mutableMapOf<Long, Long>()
    var mask = ""

    input.map {
        val maskMatch = maskRegex.matchEntire(it.trim())
        if (maskMatch != null) {
            mask = maskMatch.groupValues[1]
        } else {
            val instructionMatch = instructionRegex.matchEntire(it.trim())
            instructionMatch!!
            val address = instructionMatch.groupValues[1].toLong()
            val value = instructionMatch.groupValues[2].toLong()

            var binaryString = java.lang.Long.toBinaryString(address).padStart(36, '0')
            val newString = binaryString.mapIndexed { index, char ->
                when (mask[index]) {
                    '0' -> char
                    '1' -> '1'
                    'X' -> 'X'
                    else -> throw Exception()
                }
            }.joinToString("")

            val queue = LinkedList<Pair<String, Int>>()
            queue.add("" to 0)
            val addresses = mutableListOf<Long>()
            while (!queue.isEmpty()) {
                val current = queue.poll()
                val string = current.first
                val index = current.second
                if (index >= newString.length) {
                    addresses.add(java.lang.Long.parseLong(string.trim(), 2))
                }
                else {
                    when (val next = newString[index]) {
                        'X' -> {
                            queue.add(string.plus('0') to index + 1)
                            queue.add(string.plus('1') to index + 1)
                        }
                        else -> {
                            queue.add(string.plus(next) to index + 1)
                        }
                    }
                }
            }
            addresses.map {a ->
                memory[a] = value
            }
        }
    }
    println("Part 2 is ${memory.values.sum()}")
}

fun permuteAddresses(binaryString: String) : List<Long> {
    val queue = LinkedList<Pair<String, Int>>()
    queue.add("" to 0)
    val addresses = mutableListOf<Long>()
    while (!queue.isEmpty()) {
        val current = queue.poll()
        val string = current.first
        val index = current.second
        if (index >= binaryString.length) {
            addresses.add(java.lang.Long.parseLong(string.trim(), 2))
        }
        else {
            when (val next = binaryString[index]) {
                'X' -> {
                    queue.add(string.plus('0') to index + 1)
                    queue.add(string.plus('1') to index + 1)
                }
                else -> {
                    queue.add(string.plus(next) to index + 1)
                }
            }
        }
    }
    return addresses
}