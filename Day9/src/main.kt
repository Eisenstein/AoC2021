import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sample = sequenceOf(
        "2199943210",
        "3987894921",
        "9856789892",
        "8767896789",
        "9899965678"
    )
    val fileInput = Path("Day9/input.txt")
        .useLines { lines -> lines.toList() }
        .asSequence()
    val input = fileInput
        .map { line -> line.map { it.toString().toInt() }.toList() }
        .toList()

    fun Pair<Int, Int>.neighbours() =
        sequenceOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            .map { first + it.first to second + it.second }
            .filter { (row, col) -> row in input.indices && col in input[row].indices }

    fun Pair<Int, Int>.height() = input[first][second]

    fun Pair<Int, Int>.isLowest() = neighbours().all { it.height() > height() }

    val points = input.flatMapIndexed { index: Int, row: List<Int> -> row.indices.map { index to it } }.asSequence()
    points
        .filter { it.isLowest() }
        .sumOf { it.height() + 1 }
        .also { println(it) }
    points
        .filter { it.isLowest() }
        .map { point ->
            val used = mutableSetOf<Pair<Int, Int>>()
            val queue = ArrayDeque(listOf(point))
            while (queue.any()) {
                val cur = queue.removeFirst()
                if (cur in used || cur.height() == 9) {
                    continue
                }
                cur.neighbours()
                    .filter { it !in used }
                    .takeIf { it.all { p -> p.height() >= cur.height() } }
                    ?.also {
                        used.add(cur)
                        queue.addAll(it)
                    }
            }
            used.toSet()
        }
        .map { it.size }
        .sortedDescending()
        .take(3)
        .reduce(Int::times)
        .also { println(it) }
}
