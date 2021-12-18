import java.util.Scanner
import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

fun main() {
    val sample = sequenceOf(
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
        "[[[5,[2,8]],4],[5,[[9,9],0]]]",
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
        "[[[[5,4],[7,7]],8],[[8,3],8]]",
        "[[9,3],[[9,9],[6,[4,9]]]]",
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"
    )

    val fileInput = Path("Day18/input.txt")
        .bufferedReader()
        .useLines { lines -> lines.toList().asSequence() }

    data class Number(var pair: Pair<Number, Number>? = null, var value: Int? = null) {
        constructor(value: Int) : this(pair = null, value = value)

        val magnitude: Int
            get() {
                return pair?.run { first.magnitude * 3 + second.magnitude * 2 }
                    ?: value!!
            }

        infix operator fun plus(other: Number) = Number(this.copy() to other.copy()).apply { reduce() }

        override fun toString() = pair?.toString() ?: value.toString()

        fun copy(): Number = Number(pair?.run { first.copy() to second.copy() }, value)

        private fun reduce() {
            while (true) {
                reducePair(0) ?: reduceNumber() ?: return
            }
        }

        private fun reducePair(nestingLevel: Int): Pair<Int?, Int?>? {
            return pair
                ?.run {
                    return if (nestingLevel == 4) {
                        pair = null
                        value = 0
                        first.value to second.value
                    } else {
                        first.reducePair(nestingLevel + 1)
                            ?.let {
                                it.second?.also(second::addToLeft)
                                it.copy(second = null)
                            }
                            ?: second.reducePair(nestingLevel + 1)
                                ?.let {
                                    it.first?.also(first::addToRight)
                                    it.copy(first = null)
                                }
                    }
                }
        }

        private fun reduceNumber(): Any? {
            return pair
                ?.run {
                    first.reduceNumber()
                        ?: second.reduceNumber()
                }
                ?: value?.takeIf { it > 9 }
                    ?.also {
                        pair = Number(it / 2) to Number((it + 1) / 2)
                        value = null
                    }
        }

        private fun addToLeft(value: Int) {
            pair
                ?.apply { first.addToLeft(value) }
                ?: run { this.value = this.value?.plus(value) }
        }

        private fun addToRight(value: Int) {
            pair
                ?.apply { second.addToRight(value) }
                ?: run { this.value = this.value?.plus(value) }
        }
    }

    fun Scanner.readNumber(): Number = if (hasNext("\\[.+")) {
        skip("[],]*\\[")
        Number(Pair(readNumber(), readNumber()))
    } else {
        Number(nextInt())
    }

    val input = fileInput
        .map { Scanner(it).useDelimiter("[],]+").readNumber() }
        .toList()

    input
        .reduce { a, b -> a + b }
        .also { println(it.magnitude) }

    input
        .asSequence()
        .cartesian(input) { a, b -> a + b }
        .maxOf { it.magnitude }
        .also { println(it) }
}

fun <T, U, R> Sequence<T>.cartesian(other: Collection<U>, transform: (T, U) -> R): Sequence<R> {
    return flatMap { first -> other.map { second -> transform(first, second) } }
}