import kotlin.io.path.Path
import kotlin.io.path.bufferedReader

fun main() {
    val sample = "9C0141080250320F1802104A08"
    val fileInput = Path("Day16/input.txt")
        .bufferedReader()
        .readLine()

    val input = fileInput

    data class Packet(
        val version: Int,
        val type: Int,
        val longValue: Long? = null,
        val subPackets: List<Packet> = listOf(),
    ) {
        fun sumVersions(): Int = version + subPackets.sumOf { it.sumVersions() }
        val value: Long
            get() {
                with(subPackets.asSequence().map { it.value })
                {
                    return when (type) {
                        0 -> sum()
                        1 -> reduce(Long::times)
                        2 -> minOrNull()!!
                        3 -> maxOrNull()!!
                        4 -> longValue!!
                        5 -> if (first() > last()) 1 else 0
                        6 -> if (first() < last()) 1 else 0
                        7 -> if (first() == last()) 1 else 0
                        else -> 0
                    }
                }
            }
    }

    fun String.readBlock(offset: Int, len: Int): Int {
        val fromChar = offset / 4
        val toChar = (offset + len - 1) / 4
        return substring(fromChar..toChar).toInt(16)
            .let { it shr ((-offset - len - 3) % 4 + 3) }
            .let { it and ((1 shl len) - 1) }
    }

    var offset = 0
    fun readBlock(len: Int) = input.readBlock(offset, len).also { offset += len }

    fun readPacket(): Packet {
        val version = readBlock(3)
        val type = readBlock(3)
        if (type == 4) {
            val value = generateSequence { readBlock(5).toLong() }
                .takeWhile { it and (1 shl 4) > 0 }
                .map { it and (1 shl 4) - 1 }
                .reduce { a, b -> (a shl 4) + b }
            return Packet(version, type, value)
        }
        val lenId = readBlock(1)
        val subPackets = if (lenId == 0) {
            val length = readBlock(15)
            val limit = offset + length
            generateSequence { readPacket() }.takeWhile { offset < limit }
        } else {
            val count = readBlock(11)
            generateSequence { readPacket() }.take(count)
        }
        return Packet(version, type, subPackets = subPackets.toList())
    }
    readPacket()
        .also { println(it.sumVersions()) }
        .also { println(it.value) }
}
