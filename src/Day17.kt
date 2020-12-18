import com.sun.jmx.snmp.EnumRowStatus.active
import java.io.File

const val ACTIVE_CUBE = '#'

data class CubePosition(val x: Int, val y: Int, val z: Int, val w: Int)
fun main() {
    var activeCubes = mutableSetOf<CubePosition>()
    File("src/input/day17.txt").readLines().mapIndexed { y, s ->
        s.mapIndexed { x, c ->
            if (c == ACTIVE_CUBE) {
                activeCubes.add(CubePosition(x, y, 0, 0))
            }
        }
    }

    println("Part one:  ${boot(activeCubes, false)}")
    println("Part two:  ${boot(activeCubes, true)}")
}

fun boot(initialCubes : Set<CubePosition>, fourDimensions: Boolean) : Int {
    var activeCubes = initialCubes
    for (cycle in 0 until 6) {
        val minZ = activeCubes.minBy { it.z }!!.z - 1
        val maxZ = activeCubes.maxBy { it.z }!!.z + 1
        val minY = activeCubes.minBy { it.y }!!.y - 1
        val maxY = activeCubes.maxBy { it.y }!!.y + 1
        val minX = activeCubes.minBy { it.x }!!.x - 1
        val maxX = activeCubes.maxBy { it.x }!!.x + 1
        val minW = when (fourDimensions) {
            true -> activeCubes.minBy { it.w }!!.w - 1
            else -> 0
        }
        val maxW = when (fourDimensions) {
            true -> activeCubes.maxBy { it.w }!!.w + 1
            else -> 0
        }
        val newCubes = activeCubes.toMutableSet()

        for (w in minW..maxW) {
            for (z in minZ..maxZ) {
                for (y in minY..maxY) {
                    for (x in minX..maxX) {
                        val neighbors = getActiveNeighbors(x, y, z, w, activeCubes)
                        val cube = CubePosition(x, y, z, w)
                        when (activeCubes.contains(cube)) {
                            true -> when (neighbors) {
                                2, 3 -> {
                                } // stays active
                                else -> newCubes.remove(cube)
                            }
                            false -> when (neighbors) {
                                3 -> newCubes.add(cube)
                                else -> {
                                } // stays inactive
                            }
                        }
                    }
                }
            }
        }

        activeCubes = newCubes
    }

    return activeCubes.size
}

fun Set<CubePosition>.print() {
    val minZ = this.minBy { it.z }!!.z - 1
    val maxZ = this.maxBy { it.z }!!.z + 1
    val minY = this.minBy { it.y }!!.y - 1
    val maxY = this.maxBy { it.y }!!.y + 1
    val minX = this.minBy { it.x }!!.x - 1
    val maxX = this.maxBy { it.x }!!.x + 1
    val minW = this.minBy { it.w }!!.w - 1
    val maxW = this.maxBy { it.w }!!.w + 1

    for (w in minW..maxW) {
        for (z in minZ..maxZ) {
            println("z = $z")
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    if (this.contains(CubePosition(x, y, z, w))) {
                        print("#")
                    } else {
                        print(".")
                    }
                }
                println()
            }
            println()
            println()
        }
    }
}

fun getActiveNeighbors(x: Int, y: Int, z: Int, w: Int, active: Set<CubePosition>) : Int {
    var count = 0
    for (a in x-1..x+1) {
        for (b in y-1..y+1) {
            for (c in z-1..z+1) {
                for (d in w-1..w+1) {
                    if (a == x && b == y && c == z && d == w) { continue }
                    if (active.contains(CubePosition(a, b, c, d))) {
                        count++
                    }
                }
            }
        }
    }
    return count
}