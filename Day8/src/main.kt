import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.io.path.useLines
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

fun main() {
    val sample = sequenceOf(
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"
    )
    val fileInput = Path("Day8/input.txt")
        .useLines { lines -> lines.toList() }
        .asSequence()
    val input = fileInput
        .map { it.split(" | ").run { first().split(' ') to last().split(' ') } }
    input
        .map { (inp, out) -> out.count { it.length in setOf(2, 3, 4, 7) } }
        .sum()
        .also { println(it) }
    input
        .map {
            (inp, out) ->
            val one = inp.single { it.length == 2 }.toSet()
            val seven = inp.single { it.length == 3 }.toSet()
            val four = inp.single { it.length == 4 }.toSet()
            val eight = inp.single { it.length == 7 }.toSet()
            val cde = inp.filter { it.length == 6 }
                .flatMap { it.asSequence() }
                .groupingBy { it }
                .eachCount()
                .filterValues { it == 2 }
                .keys
            val bmap = four.minus(one).minus(cde).single()
            val dmap = four.minus(one).minus(bmap).single()
            val zero = inp.filter { it.length == 6 }.single { !it.contains(dmap) }.toSet()
            val five = inp.filter { it.length == 5 }.single { it.contains(bmap) }.toSet()
            val nine = one.plus(five)
            val six = inp.filter { it.length == 6 }.map { it.toSet() }.filterNot { it == zero || it == nine }.single()
            val three = inp.filter { it.length == 5 }.map { it.toSet() }.single { it.containsAll(seven) }
            val two = inp.filter { it.length == 5 }.map { it.toSet() }.filterNot { it == three || it == five }.single()
            val digits = sequenceOf(zero, one, two, three, four, five, six, seven, eight, nine)
                .mapIndexed { index, chars -> chars to index }
                .toMap()
            out.map { digits[it.toSet()] }.joinToString("").toInt()
        }
        .sum()
        .also { println(it) }
}
