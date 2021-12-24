fun main() {
    val sample = listOf(
        listOf('A', 'B'),
        listOf('D', 'C'),
        listOf('C', 'B'),
        listOf('A', 'D'),
    )

    val fileInput = listOf(
        listOf('A', 'B'),
        listOf('D', 'C'),
        listOf('C', 'B'),
        listOf('A', 'D'),
    )

    val input = sample
        .asSequence()

    input
        .also { println(it) }
}
