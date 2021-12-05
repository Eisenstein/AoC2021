import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    data class Point(val x: Int, val y: Int)
    val sample = sequenceOf(
        Point(0,9) to Point(5,9),
        Point(8,0) to Point(0,8),
        Point(9,4) to Point(3,4),
        Point(2,2) to Point(2,1),
        Point(7,0) to Point(7,4),
        Point(6,4) to Point(2,0),
        Point(0,9) to Point(2,9),
        Point(3,4) to Point(1,4),
        Point(0,0) to Point(8,8),
        Point(5,5) to Point(8,2),
    )
    val fileInput = Path("Day5/input.txt")
        .useLines { lines -> lines
            .map { it.split(" -> ", ",")
                .let { p -> Point(p[0].toInt(), p[1].toInt()) to Point(p[2].toInt(), p[3].toInt()) }
            }
            .toList()
        }
        .asSequence()
    val input = fileInput
    println(
        input
            .filter { it.first.x == it.second.x || it.first.y == it.second.y }
            .flatMap {
                range(it.first.x, it.second.x)
                    .flatMap { x ->
                        range(it.first.y, it.second.y).map { y -> Point(x, y) }
                    }
            }
            .groupingBy { it }
            .eachCount()
            .count {it.value > 1}
    )
    println(
        input
            .flatMap {
                when {
                    it.first.x == it.second.x -> range(it.first.y, it.second.y).map { y -> Point(it.first.x, y) }
                    it.first.y == it.second.y -> range(it.first.x, it.second.x).map { x -> Point(x, it.first.y) }
                    else -> range(it.first.x, it.second.x).zip(range(it.first.y, it.second.y)) {
                        x, y -> Point(x, y)
                    }
                }
            }
            .groupingBy { it }
            .eachCount()
            .count {it.value > 1}
    )
}

fun range(a: Int, b: Int) =
    if (a < b)
        a..b
    else
        b downTo a

