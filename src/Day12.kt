import javafx.geometry.Pos
import java.io.File
import java.lang.Exception
import kotlin.math.*
//51909 too low

//51983
fun main()  {
    val regex = Regex("([A-Z])([0-9]+)")

    var position = Position(0, 0)
    val instructions = File("src/input/day12.txt.").readLines().map {
        val match = regex.matchEntire(it.trim())
        Action(match!!.groupValues[1], match.groupValues[2].toInt())
    }
    instructions.map {
        position.move(it)
    }

    println("Part 1:  ${position.manhattanDistance(Position(0, 0))}")

    position = Position(0,0)
    instructions.map {
        position.moveWaypoint(it)
    }

    println("Part 2:  ${position.manhattanDistance(Position(0,0))}")
}

private data class Action(val character: String, val value: Int)
private data class Position(var x: Int, var y: Int, var facing: Direction = Direction.EAST, var wpX: Int = 10, var wpY: Int = -1) {
    fun manhattanDistance(other: Position) : Long {
        return abs(x - other.x).toLong() + abs(y - other.y).toLong()
    }

    fun move(action: Action) {
        when (action.character) {
            "N" -> moveNorth(action.value)
            "S" -> moveSouth(action.value)
            "E" -> moveEast(action.value)
            "W" -> moveWest(action.value)
            "L" -> facing = facing.turnLeft(action.value)
            "R" -> facing = facing.turnRight(action.value)
            "F" -> {
                when (facing) {
                    Direction.NORTH -> moveNorth(action.value)
                    Direction.EAST -> moveEast(action.value)
                    Direction.WEST -> moveWest(action.value)
                    Direction.SOUTH -> moveSouth(action.value)
                }
            }
        }
    }

    fun moveWaypoint(action: Action) {
        when (action.character) {
            "N" -> wpY -= action.value
            "S" -> wpY += action.value
            "E" -> wpX += action.value
            "W" -> wpX -= action.value
            "L" -> rotate(-action.value)
            "R" -> rotate(action.value)
            "F" -> {
                for (i in 0 until action.value) {
                    val movedX = wpX - x
                    val movedY = wpY - y
                    x = wpX
                    y = wpY
                    wpX += movedX
                    wpY += movedY
                }
            }
        }
    }

    fun Double.toRadians() : Double {
        return this * (PI/180)
    }

    fun rotate(value: Int) {
        // don't forget to do this... :)
        // if sin(90) = 0.89 you did not remember to do this
        val theta = value.toDouble().toRadians()

        // translate so point of rotation (ship) is at origin
        val originX = wpX - x
        val originY = wpY - y

        // do rotation
        wpX = ((originX * cos(theta)) - (originY * sin(theta))).roundToInt()
        wpY = (originY * cos(theta) + originX * sin(theta)).roundToInt()

        // undo translation
        wpX += x
        wpY += y
    }

    fun moveNorth(value: Int) {
        y -= value
    }

    fun moveSouth(value: Int) {
        y += value
    }

    fun moveEast(value: Int) {
        x += value
    }

    fun moveWest(value: Int) {
        x -= value
    }
}
private enum class Direction {
    NORTH {
        override fun turnLeft (degrees: Int) : Direction {
            return when (degrees) {
                90 -> WEST
                180 -> SOUTH
                270 -> EAST
                360 -> NORTH
                else -> throw Exception()
            }
        }
        override fun turnRight (degrees: Int) : Direction {
            return when (degrees) {
                90 -> EAST
                180 -> SOUTH
                270 -> WEST
                360 -> NORTH
                else -> throw Exception()
            }
        }
    },
    EAST {
        override fun turnLeft (degrees: Int) : Direction {
            return when (degrees) {
                90 -> NORTH
                180 -> WEST
                270 -> SOUTH
                360 -> EAST
                else -> throw Exception()
            }
        }
        override fun turnRight (degrees: Int) : Direction {
            return when (degrees) {
                90 -> SOUTH
                180 -> WEST
                270 -> NORTH
                360 -> EAST
                else -> throw Exception()
            }
        }
    },
    WEST {
        override fun turnLeft (degrees: Int) : Direction {
            return when (degrees) {
                90 -> SOUTH
                180 -> EAST
                270 -> NORTH
                360 -> WEST
                else -> throw Exception()
            }
        }
        override fun turnRight (degrees: Int) : Direction {
            return when (degrees) {
                90 -> NORTH
                180 -> EAST
                270 -> SOUTH
                360 -> WEST
                else -> throw Exception()
            }
        }
    },
    SOUTH {
        override fun turnLeft (degrees: Int) : Direction {
            return when (degrees) {
                90 -> EAST
                180 -> NORTH
                270 -> WEST
                360 -> SOUTH
                else -> throw Exception()
            }
        }
        override fun turnRight (degrees: Int) : Direction {
            return when (degrees) {
                90 -> WEST
                180 -> NORTH
                270 -> EAST
                360 -> SOUTH
                else -> throw Exception()
            }
        }
    };

    abstract fun turnLeft(degrees: Int) : Direction
    abstract fun turnRight(degrees: Int) : Direction
}