import java.io.File
import java.util.*

fun main() {
    val startingDecks = Array<LinkedList<Int>>(2) { LinkedList() }
    var currentPlayer = 0
    File("src/input/day22-input.txt").readLines().map {
        when {
            it.contains("Player ") -> currentPlayer++
            it.isBlank() -> {}
            else -> startingDecks[currentPlayer-1].add(it.toInt())
        }
        it
    }
    println(startingDecks)
    part1(startingDecks)
    part2(startingDecks)
}

fun part1(startingDecks: Array<LinkedList<Int>>) {
    var round = 1
    while (startingDecks[0].isNotEmpty() && startingDecks[1].isNotEmpty()) {
        val player1 = startingDecks[0].poll()
        val player2 = startingDecks[1].poll()
        println("Round $round")
        when {
            player1 > player2 -> {
                println("Player 1 wins the round!")
                startingDecks[0].add(player1)
                startingDecks[0].add(player2)
            }
            player2 > player1 -> {
                println("Player 2 wins the round!")
                startingDecks[1].add(player2)
                startingDecks[1].add(player1)
            }
            else -> throw Exception("Equal??")
        }
        round++
    }

    val winningDeck = startingDecks.first { it.isNotEmpty() }
    val score = winningDeck.toList().reversed().mapIndexed { index, i -> (index + 1) * i }.sum()

    println("Score:  $score")
}

fun part2(startingDecks: Array<LinkedList<Int>>) : Int {
    var round = 1
    val instantCheck = mutableMapOf<List<LinkedList<Int>>, Boolean>()
    while (startingDecks[0].isNotEmpty() && startingDecks[1].isNotEmpty()) {
        if (instantCheck.containsKey(listOf(startingDecks[0], startingDecks[1]))) {
            println("Player 1 wins the game by instant win!")
            return 1
        }
        instantCheck[listOf(startingDecks[0], startingDecks[1])] = true

        val player1 = startingDecks[0].poll()
        val player2 = startingDecks[1].poll()


        println("Round $round")
        val winner: Int = when {
            startingDecks[0].size >= player1 && startingDecks[1].size >= player2 -> {
                val playerOneCopy = LinkedList<Int>(startingDecks[0].toList().subList(0, player1))
                val playerTwoCopy = LinkedList<Int>(startingDecks[1].toList().subList(0, player2))
                part2(arrayOf(playerOneCopy, playerTwoCopy))
            }
            else -> {
                when {
                    player1 > player2 -> {
                        1
                    }
                    player2 > player1 -> {
                        2
                    }
                    else -> throw Exception("Equal??")
                }
            }
        }
        when (winner) {
            1 -> {
                println("Player 1 wins the round!")
                startingDecks[0].add(player1)
                startingDecks[0].add(player2)
            }
            2 -> {
                println("Player 2 wins the round!")
                startingDecks[1].add(player2)
                startingDecks[1].add(player1)
            }
            else -> throw Exception("Equal??")
        }
        round++
    }

    val winningDeck = startingDecks.indexOfFirst { it.isNotEmpty() }
    val score = startingDecks[winningDeck].toList().reversed().mapIndexed { index, i -> (index + 1) * i }.sum()

    println("Player ${winningDeck+1} wins the game!")
    println("Score:  $score")
    return winningDeck+1
}