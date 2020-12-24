import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.File

fun main() {
    val regex = Regex("(e|se|sw|w|nw|ne)")
    val instructions = File("src/input/day24-input.txt").readLines().map {
        val match = regex.findAll(it)
        match.map { it.groupValues[1] }.toList()
    }
    val tiles = mutableMapOf<Coordinate, Boolean>()
    for (instruction in instructions) {
        traverse(instruction, tiles)
    }
    val count = tiles.count{it.value}
    println("Part one is $count")

    var oldTiles = tiles
    for (i in 1..100) {
        var newTiles = oldTiles.toMutableMap()
        for (x in 0 until 200) {
            for (y in 0 until 200) {
                val coordinate = Coordinate(x, y)
                val isBlack = oldTiles.getOrDefault(coordinate, false)
                val adjacentBlackTiles = oldTiles.adjacentBlackTiles(Coordinate(x, y))
                when {
                    (adjacentBlackTiles == 0 || adjacentBlackTiles > 2) && isBlack -> newTiles[coordinate] = false
                    adjacentBlackTiles == 2 && !isBlack -> newTiles[coordinate] = true
                }
            }
        }
        val blackTiles = newTiles.count { it.value }
        println("Day $i:  $blackTiles")
        oldTiles = newTiles
    }
}

fun Map<Coordinate, Boolean>.adjacentBlackTiles(location: Coordinate) : Int {
    val coordinates = listOf(
        Coordinate(location.x+1, location.y),//
        Coordinate(location.x, location.y+1),//
        Coordinate(location.x-1, location.y+1),//
        Coordinate(location.x-1, location.y),//
        Coordinate(location.x, location.y-1),//
        Coordinate(location.x+1, location.y-1)//
    )
    return coordinates.count { this[it] == true }
}

fun traverse(instruction: List<String>, tiles: MutableMap<Coordinate, Boolean>) {
    //var current = root
    var location = Coordinate(100, 100)
    for (direction in instruction) {
        when (direction) {
            "e" -> {
                location.x++
                //current = current.east!!
            }
            "se" -> {
                location.y++
                //current = current.southeast!!
            }
            "sw" -> {
                location.y++
                location.x--
                //current = current.southwest!!
            }
            "w" -> {
                location.x--
                //current = current.west!!
            }
            "nw" -> {
                location.y--
                //current = current.northwest!!
            }
            "ne" -> {
                location.y--
                location.x++
                //current = current.northeast!!
            }
        }
    }
    tiles.computeIfAbsent(location) { false }
    tiles[location] = !(tiles[location]!!)
}

