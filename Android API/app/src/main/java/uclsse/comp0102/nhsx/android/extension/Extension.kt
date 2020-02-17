package uclsse.comp0102.nhsx.android.extension


infix fun Number.plus(other: Number): Number {
    when (this::class.java) {
        Float::class.java,
        Double::class.java -> {
            return this.toFloat() + other.toFloat()
        }
        Long::class.java -> {
            return this.toLong() + other.toLong()
        }
        Byte::class.java -> {
            return this.toByte() + other.toByte()
        }
        else -> {
            return this.toInt() + other.toInt()
        }
    }
}

fun Class<*>.isNumber(): Boolean {
    if (!isPrimitive) return false
    val numberClassTypes = listOf(
        Double::class.java, Float::class.java,
        Short::class.java, Int::class.java, Long::class.java,
        Byte::class.java, Char::class.java
    )
    return this in numberClassTypes
}
