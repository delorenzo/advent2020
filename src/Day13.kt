import java.io.File
import java.util.*

fun main() {
    val input = File("src/input/day13.txt").readLines()
    val timestamp = input[0].trim().toInt()
    val buses = input[1].split(",").filterNot { it == "x" }.map { it.toInt() }
    println(buses)
    var earliest = timestamp
    while (true) {
        val availableBuses = buses.filter { earliest % it == 0 }
        if (availableBuses.isNotEmpty()) {
            val bus = availableBuses.first()
            println("Bus $bus is available")
            val waited = earliest - timestamp
            println("Part 1 is ${waited * bus}")
            break
        }
        earliest++
    }

    println("Part 2 is ${partTwoChineseRemainder(input[1])}")
}

fun partTwoNaive(buses: String) : Long {
    var partTwo = 0L
    val partTwoBuses = buses.split(",")
    val firstBus = partTwoBuses.first().toInt()
    while (true) {
        val check = partTwoBuses.filterIndexed { index, i -> when (i) {
            "x" -> true
            else -> {
                (partTwo + index) % i.toInt() == 0L
            }
        } }
        if (check.size == partTwoBuses.size) {
            println("Part 2 is $partTwo")
            break
        }
        partTwo+=firstBus
    }
    return partTwo
}

fun partTwoChineseRemainder(input: String) : Long {
    val buses = input.split(",")
    val numericBuses = buses.filterNot { it == "x" }.map { it.toLong() }
    val product = numericBuses.fold(1L) { acc, i-> acc * i }
    val sum = buses.mapIndexed { index, num ->
        when (num) {
            "x" -> 0L
            else -> {
                val busNum = num.toLong()
                val remainder = busNum - index
                val partialProduct = product / busNum
                val inverse = multiplicativeInverse(partialProduct, busNum)
                partialProduct * inverse * remainder
            }
        }
    }.sum()
    return sum % product
}

fun multiplicativeInverse(a : Long, b: Long) : Long {
    if ( b == 1L) return 1L

    var aPrime = a
    var bPrime = b
    var x0 = 0L
    var x1 = 1L

    while (aPrime > 1) {
        val quotient = aPrime/bPrime
        var t = bPrime

        bPrime = aPrime % bPrime
        aPrime = t
        t = x0
        x0 = x1 - quotient * x0
        x1 = t
    }
    if (x1 < 0) x1 += b
    return x1
}
