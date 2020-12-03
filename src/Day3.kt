import java.io.File
import kotlin.math.exp
const val TREE = '#'
const val SNOW = '.'
fun main() {
    val lines = File("src/input/day3.txt").readLines().map { it.trim() }
    val rowLen = lines[0].length
    var posX1 = 0
    var posX2 = 0
    var posX3 = 0
    var posX4 = 0
    var posX5 = 0
    var posY = 0
    var trees1 = 0
    var trees2 = 0
    var trees3 = 0
    var trees4 = 0
    var trees5 = 0
    while (posY < lines.size-1) {
        posX1 += 1
        posX2 += 3
        posX3 += 5
        posX4 += 7
        posY += 1
        if (lines[posY][posX1 % rowLen] == TREE) {
            trees1++
        }
        if (lines[posY][posX2 % rowLen] == TREE) {
            trees2++
        }
        if (lines[posY][posX3 % rowLen] == TREE) {
            trees3++
        }
        if (lines[posY][posX4 % rowLen] == TREE) {
            trees4++
        }
    }
    posY = 0
    while (posY < lines.size-1) {
        posX5 += 1
        posY += 2
        val index = posX5 % rowLen
        if (lines[posY][index] == TREE) {
            trees5++
        }
    }
    println("Part 1 :  $trees2")
    val two = trees1 * trees2 * trees3 * trees4 * trees5
    println("Part 2:  $two")
}