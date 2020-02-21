package uclsse.comp0102.nhsxapp.api.extension

import java.net.URI


infix fun Number.plus(other: Number): Number {
    return when (this) {
        is Float,
        is Double -> {
            this.toFloat() + other.toFloat()
        }
        is Short,
        is Int -> {
            this.toInt() + other.toInt()
        }
        is Long -> {
            this.toLong() + other.toLong()
        }
        is Byte -> {
            this.toByte() + other.toByte()
        }
        else -> {
            throw UnsupportedOperationException("A unsupported number type ${this::class.java}")
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

fun URI.merge(subDir: String, fileName: String = ""): URI {
    val tmpUri = this.toString().removeSuffix("/")
    val tmpSubDir = subDir.removeSurrounding("/")
    val tmpFileName = fileName.removeSurrounding("/")
    return URI("$tmpUri/$tmpSubDir/$tmpFileName")
}

fun String.formatUriSubDir(): String {
    return this.replace("//", "/")
        .removeSurrounding("/")
}
