import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sample = listOf(
        "00100",
        "11110",
        "10110",
        "10111",
        "10101",
        "01111",
        "00111",
        "11100",
        "10000",
        "11001",
        "00010",
        "01010"
    )
    val fileInput = Path("Day3/input.txt")
        .useLines { lines -> lines.toList() }
    val input = fileInput
    val size = input.first().length
    val cnt = Array(size) {0}
    input.forEach {
        it.forEachIndexed { index, c -> if (c == '1') cnt[index]++ }
    }
    val gamma = cnt.map { if (it > input.size/2) '1' else '0' }.joinToString("")
    val eps = cnt.map { if (it < input.size/2) '1' else '0' }.joinToString("")
    println(
        gamma.toInt(2) * eps.toInt(2)
    )
    fun getSensor(condition: (Int, Int) -> Boolean): String {
        return (0 until size).fold(input) { acc, i ->
            if (acc.size == 1) return acc.first()
            val count = acc.asSequence().map { it[i].toString().toInt() }.sum()
            val char = if (condition(count, acc.size)) '1' else '0'
            acc.filter { it[i] == char }
        }.first()
    }
    val oxygen = getSensor { count, length -> count * 2 >= length }
    val co2 = getSensor { count, length -> count * 2 < length }
    println(
        oxygen.toInt(2) * co2.toInt(2)
    )
}
