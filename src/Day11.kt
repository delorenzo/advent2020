import java.io.File


private const val empty = 'L'
private const val occupied = '#'
private const val floor = '.'
fun main() {
    var seats = File("src/input/day11.txt").readLines().map { it.trim()}

    partOne(seats)

    partTwo(seats)
}

fun partTwo(input: List<String>) {
    var seats = input
    var changed = true
    var i = 0
    while (changed) {
        i++
        changed = false

        val newSeats = seats.toMutableList()
        seats.mapIndexed { rowIndex, row ->
            val rowCopy = row.toCharArray()
            row.mapIndexed { seatIndex, seat ->
                val adjacentOccupied = adjacentVisibleSeatsOccupied(seatIndex, rowIndex, seats)
                when (seat) {
                    empty -> {
                        if (adjacentOccupied == 0) {
                            rowCopy[seatIndex] = occupied
                            changed = true
                        }
                    }
                    occupied -> {
                        if (adjacentOccupied >= 5) {
                            rowCopy[seatIndex] = empty
                            changed = true
                        }
                    }
                }
            }
            newSeats[rowIndex] = rowCopy.joinToString(separator= "")
        }
        seats = newSeats
        //seats.print()
    }

    println("Stopped changing.")
    val occupied = seats.map { it.count { char -> char == occupied } }.sum()
    println("Number of occupied seats is $occupied")
}

fun partOne(input: List<String>) {
    var seats = input
    var changed = true
    while (changed) {
        changed = false

        val newSeats = seats.toMutableList()
        seats.mapIndexed { rowIndex, row ->
            val rowCopy = row.toCharArray()
            row.mapIndexed { seatIndex, seat ->
                val adjacentOccupied = adjacentSeatsOccupied(seatIndex, rowIndex, seats)
                when (seat) {
                    empty -> {
                        if (adjacentOccupied == 0) {
                            rowCopy[seatIndex] = occupied
                            changed = true
                        }
                    }
                    occupied -> {
                        if (adjacentOccupied >= 4) {
                            rowCopy[seatIndex] = empty
                            changed = true
                        }
                    }
                }
            }
            newSeats[rowIndex] = rowCopy.joinToString(separator= "")
        }
        seats = newSeats
        //seats.print()
    }

    println("Stopped changing.")
    val occupied = seats.map { it.count { char -> char == occupied } }.sum()
    println("Number of occupied seats is $occupied")
}

fun List<String>.print() {
    for (i in this) {
        println(i)
    }
    println()
}

fun adjacentSeatsOccupied(seatX: Int, seatY: Int, seats: List<String>) : Int {
    var count = 0

    if ( seatX > 0 && seatY > 0 && seats[seatY-1][seatX - 1] == occupied) count++ // x-1, y-1 }
    if ( seatY > 0 && seats[seatY-1][seatX] == occupied) count++ // x, y-1
    if ( seatX < seats[0].length - 1 && seatY > 0 && seats[seatY-1][seatX + 1] == occupied) count++ // x+1, y-1

    if ( seatX > 0 && seats[seatY][seatX - 1] == occupied) count++ // x-1, y

    if ( seatX < seats[0].length - 1 && seats[seatY][seatX + 1] == occupied) count++ // x+1, y

    if ( seatX > 0 && seatY < seats.size-1 && seats[seatY+1][seatX - 1] == occupied ) count++ // x-1, y+1
    if ( seatY < seats.size -1 && seats[seatY+1][seatX] == occupied) count++ // x, y+1
    if ( seatX < seats[0].length -1 && seatY < seats.size-1  && seats[seatY+1][seatX + 1] == occupied) count++ // x+1, y+1

    return count
}

fun adjacentVisibleSeatsOccupied(seatX: Int, seatY: Int, seats: List<String>) : Int {
    var count = 0

    if (isTopLeftDiagonalOccupied(seatX, seatY, seats)) count++ // x-1, y-1 }
    if (isTopOccupied(seatX, seatY, seats)) count++ // x, y-1
    if (isTopRightDiagonalOccupied(seatX, seatY, seats)) count++ // x+1, y-1

    if (isLeftOccupied(seatX, seatY, seats)) count++ // x-1, y

    if (isRightOccupied(seatX, seatY, seats)) count++ // x+1, y

    if (isBottomLeftDiagonalOccupied(seatX, seatY, seats)) count++ // x-1, y+1
    if (isBottomOccupied(seatX, seatY, seats)) count++
    if (isBottomRightDiagonalOccupied(seatX, seatY, seats)) count++ // x+1, y+1

    return count
}

fun isTopLeftDiagonalOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var X = seatX
    var Y = seatY

    while (X > 0 && Y > 0) {
        X--
        Y--

        when (seats[Y][X]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

fun isTopRightDiagonalOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var X = seatX
    var Y = seatY

    while (X < seats[0].length-1 && Y > 0) {
        X++
        Y--

        when (seats[Y][X]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

fun isLeftOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var pointer = seatX

    while (pointer > 0) {
        pointer--

        when (seats[seatY][pointer]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

fun isRightOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var pointer = seatX

    while (pointer < seats[0].length-1) {
        pointer++

        when (seats[seatY][pointer]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

fun isTopOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var pointer = seatY

    while (pointer > 0) {
        pointer--

        when (seats[pointer][seatX]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

fun isBottomOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var pointer = seatY

    while (pointer < seats.size-1) {
        pointer++

        when (seats[pointer][seatX]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

fun isBottomLeftDiagonalOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var X = seatX
    var Y = seatY

    while (X > 0 && Y < seats.size-1) {
        X--
        Y++

        when (seats[Y][X]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

fun isBottomRightDiagonalOccupied(seatX: Int, seatY: Int, seats: List<String>) : Boolean {
    var X = seatX
    var Y = seatY

    while (X < seats[0].length-1 && Y < seats.size-1) {
        X++
        Y++

        when (seats[Y][X]) {
            empty -> { return false }
            occupied -> { return true }
        }
    }
    return false
}

