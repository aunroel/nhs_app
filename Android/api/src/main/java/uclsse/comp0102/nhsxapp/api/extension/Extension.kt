package uclsse.comp0102.nhsxapp.api.extension

import com.google.gson.internal.LazilyParsedNumber
import uclsse.comp0102.nhsxapp.api.files.JsonData
import java.io.File
import java.lang.reflect.Field
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

fun <T: Any> Any.convertTo(type: Class<T>): Any {

    fun toNumber(): Number {
        this as Number
        return when (type) {
            Float::class.java -> this.toFloat()
            Double::class.java -> this.toDouble()
            Short::class.java -> this.toShort()
            Int::class.java -> this.toInt()
            Long::class.java -> this.toLong()
            Byte::class.java -> this.toByte()
            LazilyParsedNumber::class -> LazilyParsedNumber(this.toString())
            else -> throw UnsupportedOperationException("A unsupported number type $type")
        }
    }

    return when (type) {
        in numberClassTypesList -> toNumber()
        stringClassType -> this.toString()
        else -> throw UnsupportedOperationException("A unsupported data type $type")
    }
}

val numberClassTypesList = listOf(
    Double::class.java, Float::class.java,
    Short::class.java, Int::class.java, Long::class.java,
    Byte::class.java, LazilyParsedNumber::class
)

val Field.isNumberType: Boolean
    get() = this.type in numberClassTypesList

val stringClassType = String::class.java
val Field.isStringType: Boolean
get() = type == stringClassType

fun Field.isJsonField():Boolean{
    return this.getDeclaredAnnotation(JsonData::class.java) != null
}
var accessibleBak: Boolean = false
fun Field.activateAccessible(){

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