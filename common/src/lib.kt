inline fun <T, K> Grouping<T, K>.sumEach(valueSelector: (T) -> Int): Map<K, Int> =
    fold(0) { acc, e -> acc + valueSelector(e)}

inline fun <T, K> Grouping<T, K>.sumEachByLong(valueSelector: (T) -> Long): Map<K, Long> =
    fold(0L) { acc, e -> acc + valueSelector(e)}

inline fun <T, K> Grouping<T, K>.sumEachByDouble(valueSelector: (T) -> Double): Map<K, Double> =
    fold(0.0) { acc, e -> acc + valueSelector(e)}