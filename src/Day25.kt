fun main() {
    val subjectNum = 7L
    val cardPublicKey = 19774466L
    val doorPublicKey = 7290641L
    val cardLoopSize = determineLoopSize(subjectNum, cardPublicKey)
    val doorLoopSize = determineLoopSize(subjectNum, doorPublicKey)

    val encryptionKey = transformSubjectNum(cardPublicKey, doorLoopSize)
    val encryptionKey2 = transformSubjectNum(doorPublicKey, cardLoopSize)
    assert(encryptionKey == encryptionKey2)

    println("Part 1 is $encryptionKey")
}

fun determineLoopSize(subject: Long, publicKey: Long) : Long {
    var loop = 1L
    var num = 1L
    while (true) {
        num = transformSubjectNum(subject, loop, num to loop-1)
        if (num == publicKey) {
            return loop
        }
        loop++
    }
}

fun transformSubjectNum(num: Long, loop:Long, last: Pair<Long, Long> = 1L to 0L) : Long {
    var result = last.first
    for (i in last.second until loop) {
        result *= num
        result %= 20201227
    }
    return result
}