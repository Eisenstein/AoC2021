import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sample = listOf(
        "v...>>.vv>",
        ".vv>>.vv..",
        ">>.>v>...v",
        ">>v>>.>.v.",
        "v>v.vv.v..",
        ">.>>..v...",
        ".vv..>.>v.",
        "v.v..>>v.v",
        "....v..v.>",
    )

    val fileInput = Path("Day25/input.txt")
        .useLines { it.toList() }

    data class Point(val row: Int, val column: Int)

    val (initial, dimensions) = fileInput
        .run {
            flatMapIndexed { row, s ->
                s.mapIndexed { col, c -> Point(row, col) to c }
            }
                .filterNot { it.second == '.' }
                .toMap() to (size to first().length)
        }

    fun Map.Entry<Point, Char>.nextPosition() =
        when (value) {
            '>' -> key.copy(column = (key.column + 1) % dimensions.second)
            'v' -> key.copy(row = (key.row + 1) % dimensions.first)
            else -> key
        }

    fun Map<Point, Char>.step(predicate: (Char) -> Boolean) =
        asSequence()
            .associate {
                if (!predicate(it.value) || it.nextPosition() in keys)
                    it.toPair()
                else
                    it.nextPosition() to it.value
            }

    generateSequence(initial) { map -> map.step { it == '>' }.step { it == 'v' } }
        .zipWithNext()
        .indexOfFirst { (a, b) -> a == b }
        .also { println(it + 1) }
}
