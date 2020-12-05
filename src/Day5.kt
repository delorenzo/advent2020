import java.io.File

fun main() {
    val seats = File("src/input/day5.txt").readLines().map { getSeatId(it) }.sorted()
    val max = seats.last()
    println("Part 1:  $max")
    seats.mapIndexed { index, i ->
        if (seats[index+1] != i+1) {
            println("Part 2:  My seat is ${i+1}")
            return
        }
    }
}

fun getStep(range: Int): Int {
    return when {
        range %2 == 0 -> range / 2
        else -> range / 2 + 1
    }
}

fun getSeatId(pass: String) : Int {
    var minRow = 0
    var maxRow = 127
    var minCol = 0
    var maxCol = 7

    for (i in 0 until 7) {
        val current = pass[i]
        val range = maxRow - minRow
        val step = getStep(range)
        when (current) {
            'F' -> maxRow -= step
            'B' -> minRow += step
        }
        //println("Rows $minRow to $maxRow")
    }

    for (i in 7 until 10) {
        val current = pass[i]
        val range = maxCol - minCol
        val step = getStep(range)
        when (current) {
            'L' -> maxCol -= step
            'R' -> minCol += step
        }
        //println("Rows $minCol to $maxCol")
    }
    assert(minCol == maxCol)
    assert(minRow == maxRow)
    val id = minRow * 8 + minCol
    return id
}