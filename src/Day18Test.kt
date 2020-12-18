import org.junit.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun ex1() {
        assertEquals(71, homework("1 + 2 * 3 + 4 * 5 + 6"))
    }

    @Test
    fun ex2() {
        assertEquals(51, homework("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun ex3() {
        assertEquals(26, homework("2 * 3 + (4 * 5)"))
    }

    @Test
    fun ex4() {
        assertEquals(437, homework("5 + (8 * 3 + 9 + 3 * 4 * 3)"))
    }

    @Test
    fun ex5() {
        assertEquals(12240, homework("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"))
    }

    @Test
    fun ex6() {
        assertEquals(13632, homework("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }

    @Test
    fun ex7() {
        assertEquals(231, homework2("1 + 2 * 3 + 4 * 5 + 6"))
    }

    @Test
    fun ex8() {
        assertEquals(51, homework2("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun ex9() {
        assertEquals(46, homework2("2 * 3 + (4 * 5)"))
    }

    @Test
    fun ex10() {
        assertEquals(1445, homework2("5 + (8 * 3 + 9 + 3 * 4 * 3)"))
    }

    @Test
    fun ex11() {
        assertEquals(669060, homework2("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))"))
    }

    @Test
    fun ex12() {
        assertEquals(23340, homework2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }
}