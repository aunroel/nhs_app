package uclsse.comp0102.nhsxapp.api.extension

import com.google.gson.internal.LazilyParsedNumber


@Suppress("UNCHECKED_CAST")
fun <T: Any> Any.convertTo(type: Class<T>): T {
    val value = this.toString()
    return when (type) {
        String::class.java -> value
        Double::class.java -> value.toDouble()
        Float::class.java -> value.toFloat()
        Short::class.java -> value.toShort()
        Int::class.java -> value.toInt()
        Long::class.java -> value.toLong()
        Byte::class.java -> value.toByte()
        LazilyParsedNumber::class -> LazilyParsedNumber(value)
        else -> throw UnsupportedOperationException("A unsupported number type ${type::class}")
    } as T
}

infix fun Number.plus(other: Number): Number {
    return when (this) {
        is Float,
        is Double -> this.toFloat() + other.toFloat()
        is Short,
        is Int -> this.toInt() + other.toInt()
        is Long -> this.toLong() + other.toLong()
        is Byte -> this.toByte() + other.toByte()
        is LazilyParsedNumber -> this.toFloat() + other.toFloat()
        else -> throw UnsupportedOperationException("A unsupported number type ${this::class.java}")
    }
}

val numberClassTypesList = listOf(
    Double::class.java, Float::class.java,
    Short::class.java, Int::class.java, Long::class.java,
    Byte::class.java, LazilyParsedNumber::class
)
val Any.isNumber: Boolean
    get() = this::class.java in numberClassTypesList

val stringClassType = String::class.java
val Any.isString: Boolean
    get() = this::class.java == stringClassType
