import java.io.File
import java.util.*
import kotlin.math.pow

fun main() {
    val nums = File("src/input/day10.txt").readLines().map { it.trim().toInt()}.sorted().toMutableList()

    var oneJoltCount = 0
    var threeJoltCount = 1 // your device's built-in adapter is always 3 higher than the highest adapter
    var currentJolt = 0

    for (i in nums) {
        when (val difference = i - currentJolt) {
            0 -> {}
            1 -> oneJoltCount++
            2 -> {}
            3 -> threeJoltCount++
            else -> throw Exception("uhoh, difference is $difference")
        }
        currentJolt = i
    }

    println("There are $oneJoltCount differences of one jolts and $threeJoltCount differences of three jolts.")
    println("The answer to part one is ${oneJoltCount * threeJoltCount}")
    
    nums.add(0, 0)
    nums.add(nums.size, nums.last() + 3)
    val count = distinctArrangements(nums)
    println("The answer to part two is $count.")
}

fun distinctArrangements(list: List<Int>) : Long {
    val counts = LongArray(list.size)
    counts[0] = 1

    for (i in counts.indices) {
        for (j in 1..3) {
            if (i + j < counts.size && list[i+j] <= list[i] + 3) {
                counts[i+j] += counts[i]
            }
        }
    }

    return counts.last()
}

fun partTwoWithPower(nums: MutableList<Int>) : Double {
    nums.add(0, 0)
    nums.add(nums.size, nums.last() + 3)
    var pow2 = 0
    var pow7 = 0
    for (i in 1 until nums.size-1) {
        if (i >= 3 && nums[i+1] - nums[i-3] == 4) {
            pow7++
            pow2-=2
        }
        else if (nums[i+1] - nums[i-1] == 2) {
            pow2++
        }
    }
   return 2.0.pow(pow2) * 7.0.pow(pow7)
}