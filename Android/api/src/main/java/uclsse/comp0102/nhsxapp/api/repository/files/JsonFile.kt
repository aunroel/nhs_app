package uclsse.comp0102.nhsxapp.api.repository.files

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsxapp.api.extension.isNumber
import uclsse.comp0102.nhsxapp.api.extension.plus
import uclsse.comp0102.nhsxapp.api.repository.database.BinaryData
import java.net.URL

class JsonFile(onHost: URL, core: BinaryData) : OnlineFile(onHost, core) {

    private val gJson: Gson = Gson()

    private companion object {
        val DEFAULT_CHARSETS = Charsets.UTF_8
    }

    fun storeAndOverwrite(data: Any) {
        val dataMap = mutableMapOf<String, Number>()
        val reflectedClass = data::class.java
        reflectedClass.declaredFields
            .filter { field -> field.type.isNumber() }
            .forEach { numberField ->
                val fieldAccessibilityBackup = numberField.isAccessible
                numberField.isAccessible = true
                dataMap[numberField.name] = numberField.get(data) as Number
                numberField.isAccessible = fieldAccessibilityBackup
            }
        val newDataBytes = gJson.toJson(dataMap).toByteArray(DEFAULT_CHARSETS)
        writeBytes(newDataBytes)
    }

    fun storeAndAccumulate(data: Any) {
        val previousJsonStr = readBytes().toString(DEFAULT_CHARSETS)
        val dataMap = fromJsonStrToNumberMap(previousJsonStr)
        val reflectedClass = data::class.java
        reflectedClass.declaredFields
            .filter { field -> field.type.isNumber() }
            .forEach { numberField ->
                val fieldAccessibilityBackup = numberField.isAccessible
                numberField.isAccessible = true
                val fieldName = numberField.name
                val fieldValue = numberField.get(data) as Number
                if (!dataMap.containsKey(fieldName)) {
                    dataMap[fieldName] = fieldValue
                } else {
                    val currentValue = dataMap[fieldName]!!
                    dataMap[fieldName] = currentValue plus fieldValue
                }
                numberField.isAccessible = fieldAccessibilityBackup
            }
        val newDataBytes = gJson.toJson(dataMap).toByteArray(DEFAULT_CHARSETS)
        writeBytes(newDataBytes)
    }

    fun <T : Any> read(type: Class<T>): T {
        val jsonStr = readBytes().toString(DEFAULT_CHARSETS)
        return gJson.fromJson(jsonStr, type)
    }

    private fun fromJsonStrToNumberMap(jsonStr: String): MutableMap<String, Number> = try {
        val mapType = object : TypeToken<MutableMap<String, Number>>() {}.type
        gJson.fromJson(jsonStr, mapType)
    } catch (e: JsonSyntaxException) {
        Log.d("JsonGlobalFile.fromJsonStrToNumberMap", "$jsonStr(Syntax is incorrect)")
        throw e
    }

}


