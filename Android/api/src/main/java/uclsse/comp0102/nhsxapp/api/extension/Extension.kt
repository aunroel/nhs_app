package uclsse.comp0102.nhsxapp.api.extension

import com.google.gson.internal.LazilyParsedNumber
import java.io.File
import java.net.URL
import java.util.*


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
        is LazilyParsedNumber -> {
            this.toFloat() + other.toFloat()
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

fun String.formatSubDir(): String {
    return this.replace("//", "/")
        .removeSurrounding("/")
}

fun String.toURL(): URL {
    return URL(this)
}

fun File.createNewFileWithDirIfNotExist() {
    if (this.exists()) return
    val parentFile = this.parentFile
    if (parentFile != null && !parentFile.exists())
        parentFile.mkdirs()
    this.createNewFile()
}

const val TASK_PARAMETER_NAME = "task"

fun Calendar.getDay(): Int {
    return this[Calendar.DAY_OF_MONTH]
}