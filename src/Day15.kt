import java.util.*

fun main() {
    println(getMemoryNumber(listOf(0,3,1,6,7,5), 30000000))
}

fun getMemoryNumber(startingNums: List<Int>, iteration: Int): Int {
    var turn = 1
    val mostRecentMap = mutableMapOf<Int, PriorityQueue<Int>>()
    for (i in startingNums) {
        mostRecentMap[i] = PriorityQueue()
        mostRecentMap[i]?.add(turn)
        turn++
    }
    var mostRecent = startingNums.last()
    while (turn <= iteration) {
        val queue = mostRecentMap[mostRecent]
        if (queue?.size ?: 0 < 2) {
            mostRecent = 0
            mostRecentMap[0]?.add(turn)
        } else {
            while (queue!!.size > 2) {
                queue.poll()
            }
            val x = queue.first()
            val y = queue.last()
            val num = y - x
            if (mostRecentMap[num] == null) {
                mostRecentMap[num] = PriorityQueue()
            }
            mostRecent = num
            mostRecentMap[num]?.add(turn)
        }
        turn++
    }
    return mostRecent
}