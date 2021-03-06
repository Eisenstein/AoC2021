import java.util.TreeSet
import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

fun main() {
    val sample = sequenceOf(
        "1163751742",
        "1381373672",
        "2136511328",
        "3694931569",
        "7463417111",
        "1319128137",
        "1359912421",
        "3125421639",
        "1293138521",
        "2311944581",
    )
    val fileInput = Path("Day15/input.txt")
        .bufferedReader()
        .useLines { lines -> lines.toList().asSequence() }

    val tile = fileInput
        .map { line -> line.toList().map { it.toString().toInt() } }
        .toList()

    val input = (0 until 5)
        .flatMap { r ->
            tile.map { line ->
                (0 until 5)
                    .flatMap { c -> line.map { (it + r + c - 1) % 9 + 1 } }
            }
        }

    fun Pair<Int, Int>.riskLevel() = input[first][second]

    fun Pair<Int, Int>.neighbours() =
        sequenceOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            .map { first + it.first to second + it.second }
            .filter { (row, col) -> row in input.indices && col in input[row].indices }

    val distances = mutableMapOf((0 to 0) to 0)

    fun Pair<Int, Int>.distance() = distances.getOrDefault(this, Int.MAX_VALUE)

    val tree = TreeSet(compareBy<Pair<Int, Int>> { it.distance() }
        .then(compareBy({it.first}, {it.second})))
    tree.add(0 to 0)
    while (tree.any()) {
        val cur = tree.pollFirst()!!
        for (p in cur.neighbours()) {
            if (cur.distance() + p.riskLevel() < p.distance()) {
                tree.remove(p)
                distances[p] = cur.distance() + p.riskLevel()
                tree.add(p)
            }
        }
    }
    distances[tile.size - 1 to tile[0].size - 1]
        .also { println(it) }
    distances[input.size - 1 to input[0].size - 1]
        .also { println(it) }
}
