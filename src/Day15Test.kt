import org.junit.Test
import kotlin.test.assertEquals

class Day15Test {
    @Test
    fun partOne() {
        val iterations = 2020
        assertEquals(436, getMemoryNumber(listOf(0,3,6), iterations))
        assertEquals(852, getMemoryNumber(listOf(0,3,1,6,7,5), iterations))
    }
}