import java.util.ArrayDeque
import kotlin.io.path.Path
import kotlin.io.path.bufferedReader
import kotlin.math.absoluteValue

fun main() {
    val sample = sequenceOf(
        listOf(
            "404,-588,-901",
            "528,-643,409",
            "-838,591,734",
            "390,-675,-793",
            "-537,-823,-458",
            "-485,-357,347",
            "-345,-311,381",
            "-661,-816,-575",
            "-876,649,763",
            "-618,-824,-621",
            "553,345,-567",
            "474,580,667",
            "-447,-329,318",
            "-584,868,-557",
            "544,-627,-890",
            "564,392,-477",
            "455,729,728",
            "-892,524,684",
            "-689,845,-530",
            "423,-701,434",
            "7,-33,-71",
            "630,319,-379",
            "443,580,662",
            "-789,900,-551",
            "459,-707,401"),
        listOf(
            "686,422,578",
            "605,423,415",
            "515,917,-361",
            "-336,658,858",
            "95,138,22",
            "-476,619,847",
            "-340,-569,-846",
            "567,-361,727",
            "-460,603,-452",
            "669,-402,600",
            "729,430,532",
            "-500,-761,534",
            "-322,571,750",
            "-466,-666,-811",
            "-429,-592,574",
            "-355,545,-477",
            "703,-491,-529",
            "-328,-685,520",
            "413,935,-424",
            "-391,539,-444",
            "586,-435,557",
            "-364,-763,-893",
            "807,-499,-711",
            "755,-354,-619",
            "553,889,-390"),
        listOf(
            "649,640,665",
            "682,-795,504",
            "-784,533,-524",
            "-644,584,-595",
            "-588,-843,648",
            "-30,6,44",
            "-674,560,763",
            "500,723,-460",
            "609,671,-379",
            "-555,-800,653",
            "-675,-892,-343",
            "697,-426,-610",
            "578,704,681",
            "493,664,-388",
            "-671,-858,530",
            "-667,343,800",
            "571,-461,-707",
            "-138,-166,112",
            "-889,563,-600",
            "646,-828,498",
            "640,759,510",
            "-630,509,768",
            "-681,-892,-333",
            "673,-379,-804",
            "-742,-814,-386",
            "577,-820,562",
        ),
        listOf(
            "-589,542,597",
            "605,-692,669",
            "-500,565,-823",
            "-660,373,557",
            "-458,-679,-417",
            "-488,449,543",
            "-626,468,-788",
            "338,-750,-386",
            "528,-832,-391",
            "562,-778,733",
            "-938,-730,414",
            "543,643,-506",
            "-524,371,-870",
            "407,773,750",
            "-104,29,83",
            "378,-903,-323",
            "-778,-728,485",
            "426,699,580",
            "-438,-605,-362",
            "-469,-447,-387",
            "509,732,623",
            "647,635,-688",
            "-868,-804,481",
            "614,-800,639",
            "595,780,-596"
        ),
        listOf(
            "727,592,562",
            "-293,-554,779",
            "441,611,-461",
            "-714,465,-776",
            "-743,427,-804",
            "-660,-479,-426",
            "832,-632,460",
            "927,-485,-438",
            "408,393,-506",
            "466,436,-512",
            "110,16,151",
            "-258,-428,682",
            "-393,719,612",
            "-211,-452,876",
            "808,-476,-593",
            "-575,615,604",
            "-485,667,467",
            "-680,325,-822",
            "-627,-443,-432",
            "872,-547,-609",
            "833,512,582",
            "807,604,487",
            "839,-516,451",
            "891,-625,532",
            "-652,-548,-490",
            "30,-46,-14"
        )
    )

    val fileInput = Path("Day19/input.txt")
        .bufferedReader()
        .run {
            generateSequence {
                lineSequence().drop(1).takeWhile { it.isNotEmpty() }.toList()
            }.takeWhile { it.isNotEmpty() }
        }

    operator fun List<Int>.times(matrix: List<List<Int>>) = (0..2).map { j ->
        indices.sumOf { get(it) * matrix[it][j] }
    }

    data class Direction(val matrix: List<List<Int>>) {
        operator fun times(m: List<List<Int>>) = Direction(matrix.map { it * m })
    }

    data class Point(val x: Int, val y: Int, val z: Int) {
        operator fun times(direction: Direction): Point {
            return (listOf(x, y, z) * direction.matrix)
                .let { (x, y, z) -> Point(x, y, z) }
        }

        operator fun minus(point: Point) = listOf(x - point.x, y - point.y, z - point.z)

        operator fun plus(delta: List<Int>) = Point(x + delta[0], y + delta[1], z + delta[2])
    }

    fun directions(): Sequence<Direction> {
        val initial = Direction(listOf(listOf(1, 0, 0), listOf(0, 1, 0), listOf(0, 0, 1)))
        val rotX = listOf(listOf(1, 0, 0), listOf(0, 0, 1), listOf(0, -1, 0))
        val rotY = listOf(listOf(0, 0, -1), listOf(0, 1, 0), listOf(1, 0, 0))
        val rotZ = listOf(listOf(0, 1, 0), listOf(-1, 0, 0), listOf(0, 0, 1))
        return sequenceOf(initial)
            .flatMap { dir -> generateSequence(dir) { it * rotX }.take(4) }
            .flatMap { dir -> generateSequence(dir) { it * rotY }.take(4) }
            .flatMap { dir -> generateSequence(dir) { it * rotZ }.take(4) }
            .distinct()
    }

    val directions = directions().toList()

    fun intersect(a: Set<Point>, b: Set<Point>) =
        directions.asSequence()
            .map { dir ->
                b.map { it * dir }
            }.flatMap { cur ->
                a.asSequence().flatMap { o -> cur.map { c -> o - c } }
                    .groupingBy { it }
                    .eachCount()
                    .filter { it.value >= 12 }
                    .map { (delta, _) -> cur.map { it + delta }.toSet() to Point(0, 0, 0) + delta }
            }.firstOrNull()

    fun intersects(a: Set<Point>, b: Set<Point>) = intersect(a, b) != null

    val input = fileInput
        .map { points ->
            points.map { line ->
                line.split(",").map { it.toInt() }.run { Point(get(0), get(1), get(2)) }
            }.toSet()
        }
        .toList()

    val conns = input.flatMapIndexed { i, a ->
        input.filterIndexed { j, b -> j > i && intersects(a, b) }
            .flatMap { b -> sequenceOf(a to b, b to a) }
    }.groupBy({ it.first }, { it.second })

    val map = mutableSetOf<Point>()
    val scanners = mutableSetOf<Point>()
    val used = mutableSetOf<Set<Point>>()
    val q = ArrayDeque(listOf(input[0] to input[0]))
    while (q.isNotEmpty()) {
        val (cur, rotated) = q.pop()
        conns.getValue(cur)
            .filter { it !in used }
            .forEach {
                used.add(it)
                val (beacons, scanner) = intersect(rotated, it)!!
                q.add(it to beacons)
                map.addAll(beacons)
                scanners.add(scanner)
            }
    }

    map.also { println(it.size) }
    scanners
        .flatMap { a -> scanners.map { b -> (a - b).sumOf { it.absoluteValue } } }
        .also { println(it.maxOrNull()) }
}
