import java.util.ArrayDeque
import java.util.Queue
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sample = sequenceOf(
        "5483143223",
        "2745854711",
        "5264556173",
        "6141336146",
        "6357385478",
        "4167524645",
        "2176841721",
        "6882881134",
        "4846848554",
        "5283751526"
    )
    val fileInput = Path("Day11/input.txt")
        .useLines { lines -> lines.toList() }
        .asSequence()

    val input = fileInput
        .map { line -> line.map { it.toString().toInt() }.toMutableList() }
        .toList()

    data class Point(val row: Int, val column: Int) {
        fun neighbours() =
            sequenceOf(-1, 0, 1)
                .flatMap { sequenceOf(it to 0, it to -1, it to 1) }
                .filterNot { it == 0 to 0 }
                .map { Point(row + it.first, column + it.second) }
                .filter { (row, col) -> row in input.indices && col in input[row].indices }

        var energy: Int
            get() = input[row][column]
            set(value) {input[row][column] = value}
    }

    val points = input.flatMapIndexed { index: Int, row: List<Int> -> row.indices.map { Point(index, it) } }

    var count = 0
    generateSequence(1) { it + 1 }
        .forEach { iter ->
            val flashed = mutableSetOf<Point>()
            val queue: Queue<Point> = ArrayDeque()

            val raiseEnergy: (Point) -> Unit = {
                it.energy++
                if (it.energy > 9) {
                    queue.offer(it)
                    flashed.add(it)
                }
            }

            points.forEach(raiseEnergy)
            while (queue.any()) {
                val point = queue.poll()
                point.energy = 0
                point.neighbours()
                    .filterNot { it in flashed }
                    .forEach(raiseEnergy)
            }
            count += flashed.size
            if (iter == 100) {
                println(count)
            }
            if (flashed.size == points.count()) {
                println(iter)
                return
            }
        }
}
