import java.io.File
private val rangeRegex = Regex("^.*: ([0-9]+)-([0-9]+) or ([0-9]+)-([0-9]+)\$")
private val yourTicketString = "your ticket:"
private val nearbyTicketsString = "nearby tickets:"
fun main() {
    val input = File("src/input/day16.txt").readLines().map { it.trim() }
    day16partOne(input)
    day16partTwo(input)
}

fun day16partOne(input: List<String>) {
    val validInput = IntArray(1000)
    val iterator = input.iterator()
    var current = iterator.next()
    var errorCount = 0
    while (iterator.hasNext()) {
        when {
            rangeRegex.matches(current) -> {
                val match = rangeRegex.matchEntire(current)
                val firstRange = match!!.groupValues[1].toInt() to match.groupValues[2].toInt()
                val secondRange = match.groupValues[3].toInt() to match.groupValues[4].toInt()

                for (j in firstRange.first..firstRange.second) {
                    validInput[j] = 1
                }
                for (j in secondRange.first..secondRange.second) {
                    validInput[j] = 1
                }

                current = iterator.next()
            }
            current == yourTicketString -> {
                iterator.next()
                current = iterator.next()
            }
            current == nearbyTicketsString -> {
                while (iterator.hasNext()) {
                    errorCount += iterator.next().split(",").map { it.toInt() }.sumBy {
                        when (validInput[it] == 1) {
                            true -> 0
                            else -> it
                        }
                    }
                }
            }
            current == "" -> current = iterator.next()
            else -> throw Exception("Unexpected line:  $current")
        }
    }
    println("Part one:  $errorCount")
}

fun day16partTwo(input: List<String>) {
    val validInput = IntArray(1000)
    val validInputsForField = mutableListOf<IntArray>()
    val iterator = input.iterator()
    var current = iterator.next()
    val ranges = mutableMapOf<Int, Pair<Pair<Int, Int>, Pair<Int,Int>>>()
    var ticket = listOf<Int>()
    var departureIndices = IntArray(6)
    var typeIndex = 0
    var nearbyTickets = mutableListOf<List<Int>>()
    while (iterator.hasNext()) {
        when {
            rangeRegex.matches(current) -> {
                validInputsForField.add(IntArray(1000))
                val match = rangeRegex.matchEntire(current)
                val firstRange = match!!.groupValues[1].toInt() to match.groupValues[2].toInt()
                val secondRange = match.groupValues[3].toInt() to match.groupValues[4].toInt()
                ranges[typeIndex] = firstRange to secondRange

                for (j in firstRange.first..firstRange.second) {
                    validInput[j] = 1
                    validInputsForField[typeIndex][j] = 1
                }
                for (j in secondRange.first..secondRange.second) {
                    validInput[j] = 1
                    validInputsForField[typeIndex][j] = 1
                }

                typeIndex++
                current = iterator.next()
            }
            current == yourTicketString -> {
                ticket = iterator.next().split(",").map{ it.toInt()}
                current = iterator.next()
            }
            current == nearbyTicketsString -> {
                while (iterator.hasNext()) {
                    val ticketList = iterator.next().split(",").map { it.toInt() }
                    if (allValidValues(ticketList, validInput)) {
                        nearbyTickets.add(ticketList)
                    }
                }
            }
            current == "" -> current = iterator.next()
            else -> throw Exception("Unexpected line:  $current")
        }
    }

    val sum = IntRange(0, ticket.size-1).sum()
    var crossedOff = mutableMapOf<Int, MutableSet<Int>>()
    var found = 0
    val usedCols = IntArray(ticket.size)
    val usedFields = IntArray(ticket.size)

    while (found < ticket.size) {
        for (field in ticket.indices) {
            if (usedFields[field] == 1) { continue }
            for (colIndex in usedCols.indices) {
                if (usedCols[colIndex] == 1) { continue }
                if (crossedOff[field]?.contains(colIndex) == true) { continue }
                val col = nearbyTickets.map { it[colIndex] }
                if (col.any { validInputsForField[field][it] == 0 }) {
                    crossedOff.computeIfAbsent(field) { mutableSetOf() }
                    crossedOff[field]?.add(colIndex)
                }
            }
            if (crossedOff[field]?.size == ticket.size - 1) {
                val remaining = sum - crossedOff[field]!!.sum()
                usedFields[field] = 1
                usedCols[remaining] = 1
                for (i in ticket.indices) {
                    crossedOff.computeIfAbsent(i) { mutableSetOf() }
                    crossedOff[i]?.add(remaining)
                }
                if (field < 6) {
                    departureIndices[field] = remaining
                }
                found++
            }
        }
    }
    val ticketValues = departureIndices.map{ticket[it]}
    println("Departure ticket values:  $ticketValues")
    val departures = ticketValues.fold(1L, { mul, next -> mul * next })
    println("Part two:  $departures")
}

fun allValidValues(list : List<Int>, values: IntArray) : Boolean {
    for (i in list) {
        if (values[i] != 1) {
            return false
        }
    }
    return true
}
//19082