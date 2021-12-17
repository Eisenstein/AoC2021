import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.math.sign

fun main() {
    val sample = "target area: x=20..30, y=-10..-5"
    val fileInput = Path("Day17/input.txt")
        .bufferedReader()
        .readLine()

    data class Vector(val x: Int, val y: Int) {
        infix operator fun plus(other: Vector): Vector {
            return Vector(x + other.x, y + other.y)
        }
    }

    class Range(val xRange: IntRange, val yRange: IntRange) {
        operator fun contains(p: Vector) = xRange.contains(p.x) && yRange.contains(p.y)
    }

    val target = fileInput
        .split(": ")[1]
        .split(", ")
        .map { it.split("=")[1] }
        .map { it.split("..").map(String::toInt).run { first()..last() } }
        .run { Range(first(), last()) }

    fun Pair<Vector, Vector>.step() =
        first + second to Vector(second.x - second.x.sign, second.y - 1)

    fun fire(dir: Vector) =
        generateSequence(Vector(0, 0) to dir) { it.step() }
            .takeWhile { (position, velocity) -> velocity.y >= 0 || position.y >= target.yRange.first }
            .map { (position, _) -> position }
            .toList()
            .takeIf { points -> points.any { it in target } }

    val possibleDirs = (1..300)
        .flatMap { x ->
            (-200..200).map { y -> Vector(x, y) }
        }
        .associateWith { dir -> fire(dir) }
        .filterValues { it != null }
        .mapValues { it.value!! }
    possibleDirs
        .also { println(it.size) }
        .mapValues { (_, value) -> value.maxOf { it.y } }
        .also { println(it.maxByOrNull { (_, value) -> value }) }
}
