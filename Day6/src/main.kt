import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

val period = 7

fun main() {
    val cnt = 256
    val sample = sequenceOf(3,4,3,1,2)
    val fileInput = Path("Day6/input.txt")
        .bufferedReader()
        .readLine()
        .split(',')
        .map { it.toInt() }
        .asSequence()
    val input = fileInput
    println(
        input
            .map { cnt - it }
            .groupingBy { it }
            .eachCount()
            .map { it.value * countAfter(it.key) }
            .sum()
    )
}

val cache: MutableMap<Int, Long> = mutableMapOf()

fun countAfter(days: Int): Long {
    if (days <= 0)
        return 1
    if (!cache.containsKey(days)) {
        cache[days] = countAfter(days - period) + countAfter(days - period - 2)
    }
    return cache.getValue(days)
}
