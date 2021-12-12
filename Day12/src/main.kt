import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sample = sequenceOf(
        "start-A",
        "start-b",
        "A-c",
        "A-b",
        "b-d",
        "A-end",
        "b-end"
    )
    val fileInput = Path("Day12/input.txt")
        .useLines { lines -> lines.toList() }
        .asSequence()

    val input = fileInput
        .map { it.split("-").run { first() to last() } }
        .flatMap { sequenceOf(it, it.second to it.first) }
        .groupBy({ it.first }, { it.second })

    fun walk(cur: String, used: Set<String> = setOf(), allowTwice: Boolean = false): Int {
        if (cur == "end") {
            return 1
        }
        val next = if (cur.all { it.isLowerCase() }) used.plus(cur) else used
        return input.getValue(cur)
            .sumOf {
                when {
                    it !in next -> walk(it, next, allowTwice)
                    allowTwice && it in next && it != "start" -> walk(it, next)
                    else -> 0
                }
            }
    }
    walk("start")
        .also { println(it) }
    walk("start", allowTwice = true)
        .also { println(it) }
}
