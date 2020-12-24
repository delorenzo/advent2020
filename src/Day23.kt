import java.io.File
import java.util.*

fun main() {
    //playGame("389125467".map { it.toString().toLong() })
    //playGame("963275481".map { it.toString().toLong() })
    //playGame2("389125467".map { it.toString().toLong() })
    //playGame2("963275481".map { it.toString().toLong() })
}

fun playGame(labelling: List<Long>) {
    println(labelling)
    val highestValue = labelling.max()!!
    val lowestValue = labelling.min()!!
    val root: Node = Node(labelling.first(), null, null)
    var current = root
    for (i in 1 until labelling.size) {
        val newNode = Node(labelling[i], null, current)
        current.next = newNode
        current = newNode
    }
    current.next = root
    root.prev = current
    var moves = 1
    current = root

    while (moves <= 100) {
        println("Move $moves")
        println("Current:  ${current.value}")
        println(root.toString(current))
        val pickedUpCups = current.pickupCups()
        println("Picked up:  ${pickedUpCups.value}, ${pickedUpCups.next?.value}, ${pickedUpCups.next?.next?.value}")
        val destinationCup = current.searchForDestination(current.value-1, highestValue, lowestValue, pickedUpCups)
        println("Destination:  ${destinationCup.value}")
        destinationCup.placeCups(pickedUpCups)

        moves++
        current = current.next!!
    }
    println("Done playing.")

    println("Cup labels:  ${cupLabels(root)}")
}

fun playGame2(labelling: List<Long>) {
    var highestValue = labelling.max()!!
    val lowestValue = labelling.min()!!
    val root: Node = Node(labelling.first(), null, null)
    var current = root
    for (i in 1 until labelling.size) {
        val newNode = Node(labelling[i], null, current)
        current.next = newNode
        current = newNode
    }
    for (i in highestValue+1..1000000) {
        val newNode = Node(i, null, current)
        current.next = newNode
        current = newNode
    }
    highestValue = 1000000 // :'(
    current.next = root
    root.prev = current
    var moves = 1
    current = root

    while (moves <= 10000000) {
        val pickedUpCups = current.pickupCups()
        val destinationCup = current.searchForDestination(current.value-1, highestValue, lowestValue, pickedUpCups)
        destinationCup.placeCups(pickedUpCups)

        moves++
        current = current.next!!
    }

    val cupOne = root.search(1L)
    println("Part 2:  ${cupOne.next!!.value} ${cupOne.next!!.next!!.value}")
    val partTwo = cupOne.next!!.value * cupOne.next!!.next!!.value
    println("Part 2:  $partTwo")
    File("src/day22-out.txt").writeText("Part two:  $partTwo")
}

private fun cupLabels(root: Node): String {
    val valueOne = root.search(1)
    var current = valueOne.next
    val sb = StringBuilder()
    while (valueOne != current) {
        sb.append(current!!.value.toString())
        current = current.next
    }
    return sb.toString()
}

private class Node(val value: Long, var next: Node?, var prev: Node?) {
    override fun equals(other: Any?): Boolean {
        return other is Node && this.value == other.value
    }

    fun toString(selected: Node) : String {
        var current = this
        val sb = StringBuilder()
        doAppend(current, selected, sb)
        current = current.next!!
        while (this != current) {
            doAppend(current, selected, sb)
            current = current.next!!
        }
        return sb.toString()
    }

    private fun doAppend(current:Node, selected: Node, sb: StringBuilder) {
        if (current == selected) {
            sb.append("(${current.value}) ")
        } else {
            sb.append("${current.value} ")
        }
    }

    fun pickupCups() : Node {
        val pickedUp = this.next
        this.next = this.next?.next?.next?.next
        return pickedUp!!
    }

    fun placeCups(firstCup: Node) {
        firstCup.next?.next?.next = this.next
        this.next = firstCup
    }

    fun getCorrectedDestinationValue(value: Long, pickedUpCups:Node, lowestValue: Long, highestValue: Long) : Long {
        var newValue = value
        if (newValue < lowestValue) {
            newValue = highestValue
        }
        while (pickedUpCups.value == newValue || pickedUpCups.next!!.value == newValue || pickedUpCups.next!!.next!!.value == newValue) {
            newValue--
            if (newValue < lowestValue) {
                newValue = highestValue
            }
        }
        return newValue
    }

    fun searchForDestination(initialValue: Long, highestValue: Long, lowestValue: Long, pickedUpCups: Node) : Node {
        var target = getCorrectedDestinationValue(initialValue, pickedUpCups, lowestValue, highestValue)
        return search(target)
    }

    fun search(target: Long) : Node {
        if (target == this.value) {
            return this
        }
        var forward = this.next
        var backward = this.prev
        while (forward != this || backward != this) {
            if (forward?.value == target) {
                return forward
            }
            if (backward?.value == target) {
                return backward
            }
            forward = forward!!.next
            backward = backward!!.prev
        }
        throw Exception("Could not find Node")
    }
}