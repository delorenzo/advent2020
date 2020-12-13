import org.junit.Test
import kotlin.test.assertEquals

class Day13Test {
    @Test
    fun crt() {
        assertEquals(1068781, partTwoChineseRemainder("7,13,x,x,59,x,31,19"))
        assertEquals(3417, partTwoChineseRemainder("17,x,13,19"))
        assertEquals(754018, partTwoChineseRemainder("67,7,59,61"))
        assertEquals(779210, partTwoChineseRemainder("67,x,7,59,61"))
        assertEquals(1261476, partTwoChineseRemainder("67,7,x,59,61"))
        assertEquals(1202161486, partTwoChineseRemainder("1789,37,47,1889"))
    }

    @Test
    fun naive() {
        assertEquals(1068781, partTwoNaive("7,13,x,x,59,x,31,19"))
        assertEquals(3417, partTwoNaive("17,x,13,19"))
        assertEquals(754018, partTwoNaive("67,7,59,61"))
        assertEquals(779210, partTwoNaive("67,x,7,59,61"))
        assertEquals(1261476, partTwoNaive("67,7,x,59,61"))
        assertEquals(1202161486, partTwoNaive("1789,37,47,1889"))
    }
}