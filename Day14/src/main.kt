import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.useLines

fun main() {
    val sample = "NNCB" to mapOf(
        "CH" to "B",
        "HH" to "N",
        "CB" to "H",
        "NH" to "C",
        "HB" to "C",
        "HC" to "B",
        "HN" to "C",
        "NN" to "C",
        "BH" to "H",
        "NC" to "B",
        "NB" to "B",
        "BN" to "B",
        "BB" to "N",
        "BC" to "B",
        "CC" to "N",
        "CN" to "C",
    )
    val fileInput = Path("Day14/input.txt")
        .bufferedReader()
        .run {
            readLine() to
                lineSequence().drop(1).map { it.split(" -> ") }.associate { it.first() to it.last() }
        }

    val (template, rules) = fileInput

    val countMap = template.windowedSequence(2)
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
    (1..40)
        .fold(countMap) { acc, _ ->
            acc.asSequence()
                .flatMap { (key, value) ->
                    sequenceOf(key[0] + rules.getValue(key), rules.getValue(key) + key[1])
                        .map { it to value }
                }
                .groupBy({ it.first }, { it.second })
                .mapValues { it.value.sum() }
        }
        .asSequence()
        .flatMap { (key, value) -> key.map { it to value } }
        .groupBy({ it.first }, { it.second })
        .mapValues {
            if (template.startsWith(it.key) || template.endsWith(it.key))
                (it.value.sum() + 1) / 2
            else it.value.sum() / 2
        }
        .values
        .also { println(it.maxOrNull()!! - it.minOrNull()!!) }
}
