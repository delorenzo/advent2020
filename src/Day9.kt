import java.io.File
import java.util.*
import kotlin.system.exitProcess

fun main() {
    val preambleLen = 25
    val nums = File("src/input/day9.txt").readLines().map {it.trim().toLong() }

    val invalidNumber = findInvalidNumber(preambleLen, nums)
    println("The number that does not match the pattern is $invalidNumber")
    val range = findContiguousRange(invalidNumber, nums)
    println("The range is $range")
    val sortedRange = range.sorted()
    val weakness = sortedRange[0] + sortedRange[sortedRange.size-1]
    println("The weakness is $weakness")
}

fun findInvalidNumber(preambleLen: Int, nums: List<Long>) : Long {
    val dict = mutableMapOf<Long, Boolean>()
    for (i in 0 until preambleLen) {
        dict[nums[i]] = true
    }
    for (i in preambleLen until nums.size) {
        val current = nums[i]

        if (!containedInPreamble(current, i-preambleLen, i, nums, dict)) {
            return current
        }

        dict[nums[i-preambleLen]] = false // evict
        dict[current] = true // add
    }
    throw Exception("Did not successfully find invalid number.")
}

fun containedInPreamble(num: Long, startIndex: Int, endIndex: Int, nums: List<Long>, dict: Map<Long, Boolean>) : Boolean {
    for (i in startIndex until endIndex) {
        val remaining = num - nums[i]
        if (dict[remaining] == true) {
            return true
        }
    }
    return false
}

fun findContiguousRange(invalidNumber: Long, nums: List<Long>) : List<Long> {
    var total : Long
    val currentList = LinkedList<Long>()
    for (i in nums.indices) {
        currentList.add(nums[i])
        total = nums[i]
        for (j in i+1 until nums.size) {
            while (total > invalidNumber) {
                total -= currentList.poll()
            }
            if (total == invalidNumber) {
                return currentList
            }
            total += nums[j]
            currentList.add(nums[j])
        }
    }
    throw Exception("Could not find contiguous range.")
}
