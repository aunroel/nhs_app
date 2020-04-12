package uclsse.comp0102.nhsxapp.api.extension

import com.google.gson.internal.LazilyParsedNumber


@Suppress("UNCHECKED_CAST")
fun <T: Any> Any.convertTo(type: Class<T>): T {
    if (type == String::class.java) return this.toString() as T 
    this as Number
    return when (type) {
        Double::class.java -> this.toDouble()
        Float::class.java -> this.toFloat()
        Short::class.java -> this.toShort()
        Int::class.java -> this.toInt()
        Long::class.java -> this.toLong()
        Byte::class.java -> this.toByte()
        LazilyParsedNumber::class -> LazilyParsedNumber(this.toString())
        else -> throw UnsupportedOperationException("A unsupported number type ${type::class}")
    } as T
}