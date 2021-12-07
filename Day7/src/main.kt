import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

fun main() {
    val sample = sequenceOf(
        16, 1, 2, 0, 4, 2, 7, 1, 2, 14
    )
    val fileInput = Path("Day7/input.txt")
        .bufferedReader()
        .readLine()
        .split(',')
        .map { it.toInt() }
        .asSequence()
    val input = fileInput
    println(
        input.sorted()
            .elementAt(input.count() / 2)
            .also(::println)
            .let { median -> input.map { abs(median - it) }.sum() }
    )
    println(
        input.average()
            .also(::println)
            .let { sequenceOf(floor(it), ceil(it)) }
            .map { it.roundToInt() }
            .map { average -> input.map { abs(average - it) }.map { it * (it + 1) / 2 }.sum() }
            .also { println(it.joinToString(",")) }
            .minOrNull()
    )
}
