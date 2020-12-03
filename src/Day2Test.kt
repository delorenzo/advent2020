import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day2Test() {
    @Test
    fun examples() {
        assertTrue(isMatching(1, 3, 'a', "abcde"))
        assertFalse(isMatching(1, 3, 'b', "cdefg"))
        assertTrue(isMatching(2, 9, 'c', "ccccccccc"))
    }
}