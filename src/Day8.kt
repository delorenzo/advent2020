import java.io.File
import kotlin.system.exitProcess

fun main() {
    val regex = Regex("([a-z]+) ([+-])([\\d]+)")
    val instructions = File("src/input/day8.txt").readLines().map{stringToOperation(it, regex)}

    partOne(instructions)
    partTwo(instructions)
}

fun partOne(instructions: List<Operation>) {
    val dict = mutableMapOf<Int, Boolean>()
    val state = State()

    while (state.instructionPointer < instructions.size) {
        val operation = instructions[state.instructionPointer]
        if (dict[state.instructionPointer] == true) {
            println("Part 1:  $operation is executing a second time and the accumulator is at ${state.accumulator}")
            return
        }
        dict[state.instructionPointer] = true
        operation.execute(state)
    }
}

fun partTwo(instructions: List<Operation>) {
    val dict = mutableMapOf<Int, Int>()
    val state = State()

    while (state.instructionPointer < instructions.size && state.instructionToMutate < instructions.size) {
        val operation = instructions[state.instructionPointer]
        dict[state.instructionPointer] += 1

        if (state.instructionPointer == state.instructionToMutate) {
            operation.executeMutation(state)
        } else {
            operation.execute(state)
        }


        if (dict[state.instructionPointer] == 3) {
            dict.clear()
            state.reset(instructions)
        }
    }
    println("Part 2:  Acc at program termination:  ${state.accumulator}")
}

private fun stringToOperation(string: String, regex: Regex) : Operation {
    val match = regex.matchEntire(string.trim())
    return Operation(match!!.groupValues[1], match.groupValues[2], match.groupValues[3].toInt())
}

data class Operation(private val type: String, private val sign: String, private val scale: Int) {
    private val argument = when (sign) {
        "+" -> scale
        "-" -> scale * -1
        else -> throw Exception("Invalid sign.")
    }

    fun execute(state: State) {
        when (type) {
            "nop" -> state.instructionPointer++
            "acc" -> {
                state.instructionPointer++
                state.accumulator+=argument
            }
            "jmp" -> state.instructionPointer+=argument
            else -> throw Exception("Invalid operation.")
        }
    }

    fun executeMutation(state: State) {
        when (type) {
            "nop" -> state.instructionPointer+=argument
            "jmp" -> state.instructionPointer++
            else -> execute(state)
        }
    }

    val mutable: Boolean = when (type) {
        "jmp" -> true
        "nop" -> true
        else -> false
    }
}

data class State(var instructionPointer: Int = 0, var accumulator: Int = 0, var instructionToMutate: Int = 0) {
    fun reset(instructions: List<Operation>) {
        instructionPointer = 0
        accumulator = 0
        instructionToMutate++
        while (!instructions[instructionToMutate].mutable) {
            instructionToMutate++
        }
    }
}