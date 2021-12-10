import java.util.Stack
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sample = sequenceOf(
        "[({(<(())[]>[[{[]{<()<>>",
        "[(()[<>])]({[<{<<[]>>(",
        "{([(<{}[<>[]}>{[]{[(<()>",
        "(((({<>}<{<{<>}{[]{[]{}",
        "[[<[([]))<([[{}[[()]]]",
        "[{[{({}]{}}([{[{{{}}([]",
        "{<[[]]>}<{[{[{[]{()[[[]",
        "[<(<(<(<{}))><([]([]()",
        "<{([([[(<>()){}]>(<<{{",
        "<{([{{}}[<[[[<>{}]]]>[]]",
    )
    val fileInput = Path("Day10/input.txt")
        .useLines { lines -> lines.toList() }
        .asSequence()

    val pairs = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )

    class Incorrect(last: Char) {
        val score = mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        ).getValue(last)
    }

    class Incomplete(stack: Stack<Char>) {
        val score: Long

        init {
            val scoreMap = mapOf(
                '(' to 1,
                '[' to 2,
                '{' to 3,
                '<' to 4
            )
            score = stack.asReversed()
                .fold(0L) { acc, c -> acc * 5 + scoreMap.getValue(c) }
        }
    }

    fun parse(s: String): Any {
        val stack = Stack<Char>()
        for (i in s) {
            pairs[i]
                ?.also { if (stack.pop() != it) return Incorrect(i) }
                ?: stack.push(i)
        }
        return Incomplete(stack)
    }

    val input = fileInput
        .map { parse(it) }

    input
        .filterIsInstance<Incorrect>()
        .sumOf { it.score }
        .also { println(it) }

    input
        .filterIsInstance<Incomplete>()
        .map { it.score }
        .sorted()
        .toList()
        .let { it[it.size / 2] }
        .also { println(it) }
}
