import java.io.File
import kotlin.math.exp

fun main() {
    val passwordRegex = Regex("(\\d+)\\-(\\d+) ([a-z]{1})\\: ([a-z]+)")
    val matches = File("src/input/day2.txt").readLines().map {  passwordRegex.matchEntire(it.trim()) }
    val partOne = matches.count { isMatching(it!!.groupValues[1].toInt(), it.groupValues[2].toInt(), it.groupValues[3].single(), it.groupValues[4]) }
    println("Part 1 answer:  $partOne")
    val partTwo = matches.count { isValid(it!!.groupValues[1].toInt()-1, it.groupValues[2].toInt()-1, it.groupValues[3].single(), it.groupValues[4]) }
    println("Part 2 answer:  $partTwo")
}

// Part 1
fun isMatching(min: Int, max: Int, character: Char, password: String): Boolean {
    return password.count { it == character } in min..max
}

//Part 2
//407
fun isValid(firstPosition: Int, secondPosition: Int, character: Char, password: String): Boolean {
    assert(firstPosition < secondPosition)
    if (firstPosition > password.length) return false
    val first = password[firstPosition] == character
    val second = when (secondPosition >= password.length) {
        true -> false
        false -> password[secondPosition] == character
    }
    val response =  first.xor(second)
    println("$first $second $response")
    return response
}