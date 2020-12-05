import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.asserter

class Day5Test {
    @Test
    fun examples() {
        assertEquals(357, getSeatId("FBFBBFFRLR"))
        assertEquals(567, getSeatId("BFFFBBFRRR"))
        assertEquals(119, getSeatId("FFFBBBFRRR"))
        assertEquals(820, getSeatId("BBFFBBFRLL"))
    }
}