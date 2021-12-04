import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

fun main() {
    data class Board(
        val numbers: List<List<Int>>,
        var marked: MutableSet<Int> = mutableSetOf(),
        var win: Boolean = false,
        var score: Int = 0,
    )

    val sample =
        sequenceOf(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6, 15, 25, 12, 22, 18, 20, 8, 19, 3, 26, 1)
            .to(
                listOf(
                    Board(
                        listOf(
                            listOf(22, 13, 17, 11, 0),
                            listOf(8, 2, 23, 4, 24),
                            listOf(21, 9, 14, 16, 7),
                            listOf(6, 10, 3, 18, 5),
                            listOf(1, 12, 20, 15, 19)
                        )),

                    Board(listOf(
                        listOf(3, 15, 0, 2, 22),
                        listOf(9, 18, 13, 17, 5),
                        listOf(19, 8, 7, 25, 23),
                        listOf(20, 11, 10, 24, 4),
                        listOf(14, 21, 16, 12, 6)
                    )),

                    Board(listOf(
                        listOf(14, 21, 17, 24, 4),
                        listOf(10, 16, 15, 9, 19),
                        listOf(18, 8, 23, 26, 20),
                        listOf(22, 11, 13, 6, 5),
                        listOf(2, 0, 12, 3, 7)
                    ))
                ))
    val file = Path("Day4/input.txt").bufferedReader()
    val fileInput = file
        .readLine()
        .splitToSequence(',').map { it.toInt() } to file
        .also { it.readLine() }
        .useLines { lines ->
            lines.filterNot { it.isEmpty() }
                .map { it.split(' ')
                    .filterNot { it.isEmpty() }
                    .map { it.toInt() }
                    .toList() }
                .chunked(5)
                .map { Board(it) }
        }
    val (seq, boards) = fileInput
    seq.forEach { n ->
        boards.filterNot { it.win }
            .forEach { board ->
                with(board) {
                    for (line in numbers) {
                        line.indexOf(n)
                            .takeIf { it >= 0 }
                            ?.let { index ->
                                marked.add(n)
                                if (marked.containsAll(line)
                                    || numbers.asSequence().map { it[index] }.all { marked.contains(it) }
                                ) {
                                    win = true
                                    score = numbers.asSequence()
                                        .flatMap { it }
                                        .filterNot { marked.contains(it) }
                                        .sum() * n
                                    println(score)
                                }
                            }
                    }
                }
            }
    }
    println(

    )
}
