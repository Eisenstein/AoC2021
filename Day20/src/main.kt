import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

fun main() {
    val sample =
        "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##" +
            "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###" +
            ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#." +
            ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#....." +
            ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.." +
            "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#....." +
            "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#" to
            listOf(
                "#..#.",
                "#....",
                "##..#",
                "..#..",
                "..###"
            )

    val fileInput = Path("Day20/input.txt")
        .bufferedReader()
        .use {
            it.readLine() to it.lineSequence().drop(1).toList()
        }

    data class Point(val row: Int, val column: Int) {
        fun neighbours() =
            sequenceOf(-1, 0, 1)
                .flatMap { sequenceOf(it to -1, it to 0, it to 1) }
                .map { Point(row + it.first, column + it.second) }
    }

    val (algorithm, input) = fileInput

    fun Map<Point, Char>.withDefault(c: Char) = filterValues { it != c }.withDefault { c } to c
    fun Sequence<Char>.decodeSignal() = map { if (it == '#') 1 else 0 }
        .reduce { a, b -> (a shl 1) + b }
        .let { algorithm[it] }

    fun Collection<Int>.boundingBox() = minOf { it } - 1..maxOf { it } + 1
    fun Collection<Point>.boundingBox() =
        map { it.row }.boundingBox()
            .flatMap { row ->
                map { it.column }.boundingBox().map { column -> Point(row, column) }
            }

    val init = input
        .flatMapIndexed { row: Int, s: String ->
            s.mapIndexed { column, c -> Point(row, column) to c }
        }
        .toMap()
        .withDefault('.')

    generateSequence(init) { (prev, defaultChar) ->
        prev.keys.boundingBox()
            .associateWith { it.neighbours().map { prev.getValue(it) }.decodeSignal() }
            .withDefault(if (defaultChar == '.') algorithm.first() else algorithm.last())
    }
        .take(51)
        .map { it.first.size }
        .toList()
        .also {
            println(it[2])
            println(it[50])
        }
}
