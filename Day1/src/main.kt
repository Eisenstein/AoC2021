import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main(args: Array<String>) {
    val input = Path(args[0])
        .useLines { lines -> lines.map { Integer.parseInt(it) }.toList() }
        .asSequence()
    val res = input
        .windowed(2) { it[0] to it[1] }
        .count { it.first < it.second }
    println(res)

    val res2 = input
        .windowed(3) { it.sum() }
        .windowed(2) { it[0] to it[1] }
        .count { it.first < it.second }
    println(res2)
}

