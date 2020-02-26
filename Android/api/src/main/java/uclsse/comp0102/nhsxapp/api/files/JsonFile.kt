package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsxapp.api.extension.isNumber
import uclsse.comp0102.nhsxapp.api.extension.plus
import uclsse.comp0102.nhsxapp.api.files.core.OnlineFile
import java.net.URL

class JsonFile(onHost: URL, subDirWithName: String, appContext: Context) :
    OnlineFile(onHost, subDirWithName, appContext) {

    private val gJson: Gson = Gson()
    private val utf8Charset = Charsets.UTF_8

    init {
        if (readBytes().isEmpty())
            writeBytes("{}".toByteArray())
    }

    fun storeAndOverwrite(data: Any) {
        val dataMap = mutableMapOf<String, Number>()
        val fields = data::class.java.declaredFields
        val numFields = fields.filter { it.type.isNumber() }
        for (eachField in numFields) {
            val accessibleBackup = eachField.isAccessible
            eachField.isAccessible = true
            dataMap[eachField.name] = eachField.get(data) as Number
            eachField.isAccessible = accessibleBackup
        }
        val newDataBytes = gJson.toJson(dataMap).toByteArray(utf8Charset)
        writeBytes(newDataBytes)
    }

    fun storeAndAccumulate(data: Any) {
        val previousJsonStr = readBytes().toString(utf8Charset)
        val dataMap = fromJsonStrToNumberMap(previousJsonStr)
        val fields = data::class.java.declaredFields
        val numFields = fields.filter { it.type.isNumber() }
        for (eachField in numFields) {
            val accessibleBackup = eachField.isAccessible
            eachField.isAccessible = true
            val currentValue = dataMap[eachField.name] ?: 0
            val fieldValue = eachField.get(data) as Number
            dataMap[eachField.name] = currentValue plus fieldValue
            eachField.isAccessible = accessibleBackup
        }
        val newDataBytes = gJson.toJson(dataMap).toByteArray(utf8Charset)
        writeBytes(newDataBytes)
    }

    fun <T : Any> to(type: Class<T>): T {
        val jsonStr = readBytes().toString(utf8Charset)
        return gJson.fromJson(jsonStr, type)
    }

    private fun fromJsonStrToNumberMap(jsonStr: String): MutableMap<String, Number> {
        val mapType = object : TypeToken<MutableMap<String, Number>>() {}.type
        return gJson.fromJson(jsonStr, mapType)
    }

}


