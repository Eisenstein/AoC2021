import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

fun main() {
    val fileInput = Path("Day24/input.txt")
        .bufferedReader()
        .use {
            it.lineSequence().toList()
        }

    //1{1-2}{7-9}{1-3}{1-4}{6-9}9{1-8}{6-9}{1-4}{2-9}1{8-9}9
    //val input = "12934998949199".iterator()
    val input = "11711691612189".iterator()

    val commands = fileInput
    commands
        .asSequence()
        .map { it.split(' ').plus("0") }
        .fold(mapOf("x" to 0L, "y" to 0L, "z" to 0L, "w" to 0L)) { map, (command, arg1, arg2) ->
            val val1 = map[arg1]!!
            val val2 = map[arg2] ?: arg2.toLong()
            map.plus(arg1 to
                when (command) {
                    "inp" -> input.nextChar().toString().toLong()
                    "add" -> val1 + val2
                    "mul" -> val1 * val2
                    "div" -> val1 / val2
                    "mod" -> val1 % val2
                    "eql" -> if (val1 == val2) 1 else 0
                    else -> 0L
                }
            )
        }
        .also { println(it) }
}
