import java.io.File
import java.lang.Exception
import java.lang.Integer.min
import kotlin.math.*

const val GRID_SIZE = 10
fun main() {
    val tileRegex = Regex("Tile (\\d+)+:")
    var currentTile = 0
    val tiles = mutableMapOf<Int, List<List<Char>>>()
    var tile = mutableListOf<List<Char>>()
    val tileMap = mutableMapOf<Int, List<Int>>()
    val edges = mutableMapOf<Int, MutableList<Int>>()
    val tileEdges = mutableMapOf<Int, Array<String>>()
    val corners = mutableSetOf<Int>()

    File("src/input/day20-input.txt").readLines().mapIndexed{ y, it ->
        when {
            tileRegex.matches(it) -> {
                currentTile = tileRegex.matchEntire(it)!!.groupValues[1].toInt()
            }
            it == "" -> {
                tiles.put(currentTile, tile)
                tile = mutableListOf()
            }
            else -> {
                tile.add(it.map { it })
            }
        }
    }

    //part 1
    populateEdges(tiles, edges, tileMap, tileEdges)
    populateCorners(tileMap, corners, edges)
    if (corners.size > 4) {
        corners.remove(corners.first())
    }
    println("Corners:  $corners")
    val part1 = corners.fold(1L) { acc, i -> acc * i }
    println("Part 1 is $part1")

    //part 2
    val size = sqrt(tiles.size.toDouble()).roundToInt()
    val ids = Array(size) { Array(size) { 0 } }
    val grid = Array(size) { Array(size) { emptyList<List<Char>>() } }
    ids[0][0] = corners.first()
    //find correct corner rotation and place
    val matches = listOf(TOP, RIGHT, BOTTOM, LEFT).map { edge ->
        tiles.count {
            it.key != corners.first() && listOf(tileEdges[corners.first()]!![edge], tileEdges[corners.first()]!![edge].reversed()).any{str -> str in tileEdges[it.key]!!}
        }
    }
    when {
        matches[TOP] == 0 && matches[RIGHT] == 0 -> {
            tiles[corners.first()] = tiles[corners.first()]!!.rotateClockwise().rotateClockwise().rotateClockwise()
        }
        matches[RIGHT] == 0 && matches[BOTTOM] == 0 -> {
            tiles[corners.first()] = tiles[corners.first()]!!.rotateClockwise().rotateClockwise()
        }
        matches[LEFT] == 0 && matches[BOTTOM] == 0 -> {
            tiles[corners.first()] = tiles[corners.first()]!!.rotateClockwise()
        }
    }
    grid[0][0] = tiles[corners.first()]!!
    tileEdges.updateEdges(grid[0][0], corners.first())
    //put the grid together
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            when {
                i==0 && j == 0 -> {} //skip top-left corner
                i == 0 -> {
                    val matchId = ids[i][j-1]
                    // find the (left-side matching) tile for the right edge of the last piece in the row
                    val matchEdge = tileEdges[matchId]!![LEFT]
                    val matchingEdge = tileEdges.findOther(matchEdge, matchId)
                    val matchedId = matchingEdge.first
                    tiles[matchedId] = when (matchingEdge.second) {
                        TOP -> {
                            tiles[matchedId]!!.flipHorizontal().rotateClockwise().rotateClockwise().rotateClockwise()
                        }
                        RIGHT -> {
                            tiles[matchedId]!!.flipHorizontal()
                        }
                        BOTTOM -> {
                            tiles[matchedId]!!.rotateClockwise()
                        }
                        LEFT -> {
                            //wowee we don't need to do anything
                            tiles[matchedId]!!
                        }
                        TOP_FLIPPED -> {
                           tiles[matchedId]!!.rotateClockwise().rotateClockwise().rotateClockwise()
                        }
                        RIGHT_FLIPPED -> {
                            tiles[matchedId]!!.rotateClockwise().rotateClockwise()
                        }
                        BOTTOM_FLIPPED -> {
                            tiles[matchedId]!!.flipHorizontal().rotateClockwise()
                        }
                        LEFT_FLIPPED -> {
                            tiles[matchedId]!!.flipHorizontal().rotateClockwise().rotateClockwise()
                        }
                        else -> {
                            throw Exception("Unexpected edge match type.")
                        }
                    }
                    ids[i][j] = matchedId
                    grid[i][j] = tiles[matchedId]!!
                    tileEdges.updateEdges(grid[i][j], matchedId)
                }
                else -> {
                    val matchId = ids[i-1][j]
                    val matchEdge = tileEdges[matchId]!![RIGHT]
                    val matchingEdge = tileEdges.findOther(matchEdge, matchId)
                    val matchedId = matchingEdge.first
                    tiles[matchedId] = when (matchingEdge.second) {
                        TOP -> {
                            //wowee we don't do anything
                            tiles[matchedId]!!
                        }
                        RIGHT -> {
                            tiles[matchedId]!!.rotateClockwise().rotateClockwise().rotateClockwise()
                        }
                        BOTTOM -> {
                            tiles[matchedId]!!.flipHorizontal().rotateClockwise().rotateClockwise()
                        }
                        LEFT -> {
                            tiles[matchedId]!!.rotateClockwise().flipHorizontal()
                        }
                        TOP_FLIPPED -> {
                            tiles[matchedId]!!.flipHorizontal()
                        }
                        RIGHT_FLIPPED -> {
                            tiles[matchedId]!!.flipHorizontal().rotateClockwise()
                        }
                        BOTTOM_FLIPPED -> {
                            tiles[matchedId]!!.rotateClockwise().rotateClockwise()
                        }
                        LEFT_FLIPPED -> {
                            tiles[matchedId]!!.rotateClockwise()
                        }
                        else -> {
                            throw Exception("Unexpected edge match type.")
                        }
                    }
                    ids[i][j] = matchedId
                    grid[i][j] = tiles[matchedId]!!
                    tileEdges.updateEdges(grid[i][j], matchedId)
                }
            }
        }
    }
    //slice off the edges and turn it into something we can search better
    var finishedGrid = Array(grid.size*8) { Array(grid.size*8) { '.' } }
    var row = 0
    var col = 0
    for (i in grid.indices) {
        for (j in grid[0].indices) {
            val id = ids[i][j]
            val tile = tiles[id]!!
            row = i * 8
            col = j * 8
            for (x in 1..tile.size-2 ) {
                for (y in 1..tile[0].size-2) {
                    finishedGrid[row][col] = tile[x][y]
                    col++
                }
                row++
                col = j * 8
            }
        }
    }
    //rotate 4 times, flip, then rotate 4 more times, labelling all the monsters
    for (i in 0 until 4) {
        finishedGrid.findMonsters()
        finishedGrid = finishedGrid.rotateClockwise()
    }
    finishedGrid = finishedGrid.flipHorizontal()
    for (i in 0 until 4) {
        finishedGrid.findMonsters()
        finishedGrid = finishedGrid.rotateClockwise()
    }
    //count all the non-labelled edges
    val roughness = finishedGrid.map { it.count{ char -> char == '#' } }.sum()
    println("Roughness:  $roughness")
}

fun MutableMap<Int, Array<String>>.updateEdges(newTile: List<List<Char>>, id: Int) {
    val topEdge = StringBuilder()
    val bottomEdge = StringBuilder()
    val leftEdge = StringBuilder()
    val rightEdge = StringBuilder()
    for (row in newTile) {
        leftEdge.append(row.first())
        rightEdge.append(row.last())
    }
    this[id]!![BOTTOM] = leftEdge.toString()
    this[id]!![LEFT] = rightEdge.toString()
    for (i in newTile.indices) {
        topEdge.append(newTile.first()[i])
        bottomEdge.append(newTile.last()[i])
    }
    this[id]!![RIGHT] = bottomEdge.toString()
    this[id]!![TOP] = topEdge.toString()
}

fun List<List<Char>>.printTile() {
    this.map { row -> println(row.map { it }.joinToString("")) }

    println()
}

fun printEdges(id: Int, edges: Map<Int, Array<String>>) {
    println("Top:  ${edges[id]!![TOP]}")
    println("Right:  ${edges[id]!![LEFT]}")
    println("Bottom:  ${edges[id]!![RIGHT]}")
    println("Left:  ${edges[id]!![BOTTOM]}")
}

fun Array<Array<List<List<Char>>>>.print() {
    println("attempting to print grid")
    val sb = StringBuilder()
    for (i in this.indices) {
        for (j in this[0][0]) {
            for (element in this) {
                if (element.isNotEmpty()) {
                    sb.append(element.joinToString(""))
                    sb.append("|")
                }
            }
            sb.append("\n")
        }
    }
    println(sb.toString())
}

fun Map<Int, Array<String>>.findOther(matchEdge: String, matchId: Int) : Pair<Int, Int> {
    val reverseMatch = matchEdge.reversed()
    return this.filter {(it.value.contains(matchEdge) || it.value.contains(reverseMatch))}
        .filterNot { it.key == matchId }
        .map {
        it.key to when(it.value.contains(matchEdge)) {
                true -> it.value.indexOf(matchEdge)
                false -> it.value.indexOf(reverseMatch) + 4
            }
    }.first()
}

fun List<List<Char>>.rotateClockwise() : List<List<Char>> {
    return this.mapIndexed { i, list -> list.mapIndexed { j, _ -> this[this.size-j-1][i] } }.toList()
}

fun Array<Array<Char>>.rotateClockwise() : Array<Array<Char>> {
    return this.mapIndexed { i, list -> list.mapIndexed { j, _ -> this[this.size-j-1][i] }.toTypedArray() }.toTypedArray()
}

fun List<List<Char>>.flipHorizontal() : List<List<Char>> {
    return this.mapIndexed { i, list -> list.mapIndexed { j, _ -> this[i][this[0].size-j-1]}}.toList()
}

fun Array<Array<Char>>.flipHorizontal() : Array<Array<Char>> {
    return this.mapIndexed { i, list -> list.mapIndexed { j, _ -> this[i][this[0].size-j-1]}.toTypedArray()}.toTypedArray()
}

const val MONSTER_CHAR = '@'

fun Array<Array<Char>>.findMonsters() {
    for (r in this.indices) {
        for (c in this[0].indices) {
            //can the monster fit here even
            if (c + 19 < this[0].size && r + 2 < this.size) {
                if (this[r][c + 18] == '#' && this[r + 1][c] == '#' && this[r + 1][c + 5] == '#'
                    && this[r + 1][c + 6] == '#' && this[r + 1][c + 11] == '#' && this[r + 1][c + 12] == '#'
                    && this[r + 1][c + 17] == '#' && this[r + 1][c + 18] == '#' && this[r + 1][c + 19] == '#'
                    && this[r + 2][c + 1] == '#' && this[r + 2][c + 4] == '#' && this[r + 2][c + 7] == '#'
                    && this[r + 2][c + 10] == '#' && this[r + 2][c + 13] == '#' && this[r + 2][c + 16] == '#'
                ) {
                    this[r][c + 18] = MONSTER_CHAR
                    this[r + 1][c] = MONSTER_CHAR
                    this[r + 1][c + 5] = MONSTER_CHAR
                    this[r + 1][c + 6] = MONSTER_CHAR
                    this[r + 1][c + 11] = MONSTER_CHAR
                    this[r + 1][c + 12] = MONSTER_CHAR
                    this[r + 1][c + 17] = MONSTER_CHAR
                    this[r + 1][c + 18] = MONSTER_CHAR
                    this[r + 1][c + 19] = MONSTER_CHAR
                    this[r + 2][c + 1] = MONSTER_CHAR
                    this[r + 2][c + 4] = MONSTER_CHAR
                    this[r + 2][c + 7] = MONSTER_CHAR
                    this[r + 2][c + 10] = MONSTER_CHAR
                    this[r + 2][c + 13] = MONSTER_CHAR
                    this[r + 2][c + 16] = MONSTER_CHAR
                }
            }
        }
    }
}

fun populateCorners(tileMap: Map<Int, List<Int>>, corners: MutableSet<Int>, edges: Map<Int, List<Int>>) : Int {
    var startingEdge: Int = Int.MIN_VALUE
    tileMap.entries.map {
        var matchCount = 0
        var unusedEdge = -1
        it.value.map { edge ->
            when (edges[edge]!!.size) {
                2 -> matchCount++
                1 -> unusedEdge = edge
                else -> {
                }
            }
        }
        assert(matchCount >= 2)
        if (matchCount == 2) {
            corners.add(it.key)
            if (startingEdge == Int.MIN_VALUE) {
                startingEdge = unusedEdge
            }
        }
    }
    return startingEdge
}

const val TOP = 0
const val RIGHT = 1
const val BOTTOM = 2
const val LEFT = 3
const val TOP_FLIPPED = 4
const val RIGHT_FLIPPED = 5
const val BOTTOM_FLIPPED = 6
const val LEFT_FLIPPED = 7

fun populateEdges(tiles: Map<Int, List<List<Char>>>, edges: MutableMap<Int, MutableList<Int>>, tileMap: MutableMap<Int, List<Int>>, tileEdges: MutableMap<Int, Array<String>>) {
    tiles.keys.map { id ->
        tileEdges.computeIfAbsent(id) { Array<String>(4) { "" } }
        val tile = tiles[id]!!
        var left = 0
        var right = 0
        val topEdge = StringBuilder()
        val bottomEdge = StringBuilder()
        val leftEdge = StringBuilder()
        val rightEdge = StringBuilder()
        for (row in tile) {
            left = left shl 1
            left = when(row.first()) {
                '#' -> left or 1
                else -> left or 0
            }
            leftEdge.append(row.first())
            right = right shl 1
            right = when(row.last()) {
                '#' -> right or 1
                else -> right or 0
            }
            rightEdge.append(row.last())
        }
        tileEdges[id]!![LEFT] = leftEdge.toString()
        tileEdges[id]!![RIGHT] = rightEdge.toString()
        var top = 0
        var bottom = 0
        for (i in 0 until GRID_SIZE) {
            top = top shl 1
            top = when(tile.first()[i]) {
                '#' -> top or 1
                else -> top or 0
            }
            topEdge.append(tile.first()[i])
            tileEdges[id]
            bottom = bottom shl 1
            bottom = when(tile.last()[i]) {
                '#' -> bottom or 1
                else -> bottom or 0
            }
            bottomEdge.append(tile.last()[i])
        }
        tileEdges[id]!![BOTTOM] = bottomEdge.toString()
        tileEdges[id]!![TOP] = topEdge.toString()
        left = edgeId(left)
        right = edgeId(right)
        bottom = edgeId(bottom)
        top = edgeId(top)
        tileMap[id] = listOf(
            left,
            right,
            bottom,
            top
        )
        edges.computeIfAbsent(left) {mutableListOf()}
        edges.computeIfAbsent(right) {mutableListOf()}
        edges.computeIfAbsent(bottom) {mutableListOf()}
        edges.computeIfAbsent(top) {mutableListOf()}
        edges[left]!!.add(id)
        edges[right]!!.add(id)
        edges[bottom]!!.add(id)
        edges[top]!!.add(id)
    }
}

fun flip(input: Int) : Int {
    var x = input
    var result = 0
    for (i in 0 until GRID_SIZE) {
        result = result shl 1
        if (x and 1 == 1) {
            result = result or 1
        }
        x = x shr 1
    }
    return result
}

fun edgeId(input: Int) : Int {
    return min(input, flip(input))
}

data class Tile(val id: Int, val data: List<List<ImageData>>)
data class ImageData(val char: Char, val coordinate: Coordinate)
data class Coordinate(var x: Int, var y: Int)