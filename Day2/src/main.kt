import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sample = sequenceOf(
        "forward" to 5,
        "down" to 5,
        "forward" to 8,
        "up" to 3,
        "down" to 8,
        "forward" to 2
    )
    val input = Path("Day2/input.txt")
        .useLines { lines -> lines.map { it.split(" ").run { first() to Integer.parseInt(last()) } }.toList() }
        .asSequence()
    println(
        input.fold(
             0 to 0
        ) { (position, depth), (command, x) ->
            when (command) {
                "forward" -> position + x to depth
                "up" -> position to depth - x
                "down" -> position to depth + x
                else -> position to depth
            }
        }.run { first * second }
    )
    data class State(val position: Int, val depth: Int, val aim: Int)
    println(
        input.fold(
             State(0, 0, 0)
        ) { acc, (command, x) ->
            when (command) {
                "forward" -> acc.copy(position = acc.position + x, depth = acc.depth + acc.aim * x)
                "up" -> acc.copy(aim = acc.aim - x)
                "down" -> acc.copy(aim = acc.aim + x)
                else -> acc
            }
        }.run { position * depth }
    )
}