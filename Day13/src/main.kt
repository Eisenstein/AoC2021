import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

fun main() {
    val sample = sequenceOf(
        "6,10",
        "0,14",
        "9,10",
        "0,3",
        "10,4",
        "4,11",
        "6,0",
        "6,12",
        "4,1",
        "0,13",
        "10,12",
        "3,4",
        "3,0",
        "8,4",
        "1,10",
        "2,14",
        "8,10",
        "9,0"
    ) to sequenceOf(
        "fold along y=7",
        "fold along x=5"
    )
    val fileInput = Path("Day13/input.txt")
        .bufferedReader()
        .run {
            lineSequence().takeWhile { it.isNotEmpty() }.toList().asSequence() to
                lineSequence().toList().asSequence()
        }

    val (dots, folds) = fileInput
        .let { (dots, folds) ->
            dots.map { line -> line.split(",").map { it.toInt() }.run { first() to last() } } to
                folds.map { line -> line.split(" ")[2].split("=").run { first() to last().toInt() } }
        }

    fun Pair<String, Int>.mapper(dot: Pair<Int, Int>) = when (first) {
        "x" -> if (dot.first < second) dot else dot.copy(first = 2 * second - dot.first)
        "y" -> if (dot.second < second) dot else dot.copy(second = 2 * second - dot.second)
        else -> dot
    }

    folds.fold(dots.toSet()) { sheet, fold ->
        sheet.map(fold::mapper)
            .toSet()
            .also { println(it.size) }
    }.also { sheet ->
        for (i in 0..sheet.maxOf { it.second }) {
            for (j in 0..sheet.maxOf { it.first }) {
                print(if (j to i in sheet) "#" else ".")
            }
            println()
        }
    }
}
