fun main() {
    val sample = 4 to 8

    val fileInput = 2 to 7

    val input = fileInput

    // first task:
    // 8+2+4+4+2+8+2+4+4+2 = 40
    // 2+5+6+5+2+7+10+1+10+7 = 55
    // 1000 < 55 * 18 + (2+5+6)
    // (18 * 10 + 3) * 6 * (18 * 40 + 8+2+4) = 805932

    val diracSums = (1..3)
        .flatMap { i -> (1..3).map { it + i } }
        .flatMap { i -> (1..3).map { it + i } }
        .toList()
        .asSequence()

    data class Key(val pos: Int, val score: Int)

    fun countGames(initialPos: Int, targetScore: Int) = generateSequence(mapOf(Key(initialPos, 0) to 1L)) { old ->
        old
            .filterKeys { it.score < targetScore }
            .asSequence()
            .flatMap { (key, count) ->
                diracSums.map { (key.pos + it - 1) % 10 + 1 }.map { Key(it, key.score + it) to count }
            }
            .groupingBy { it.first }
            .sumEachByLong { it.second }
    }
        .takeWhile { it.isNotEmpty() }
        .map { it.filterKeys { it.score >= targetScore }.values.sum() to it.filterKeys { it.score < targetScore }.values.sum() }
        .unzip()

    val (finished1, unfinished1) = countGames(input.first, 21)
    val (finished2, unfinished2) = countGames(input.second, 21)

    fun sumGames(finished: List<Long>, unfinished: List<Long>) = finished.zip(unfinished, Long::times).sum()

    sumGames(finished1, listOf(0L) + unfinished2)
        .also { println(it) }
    sumGames(finished2, unfinished1)
        .also { println(it) }
}
