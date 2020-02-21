package uclsse.comp0102.nhsxapp.api.repository.files

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsxapp.api.extension.isNumber
import uclsse.comp0102.nhsxapp.api.extension.plus
import java.net.URI

class JsonGlobalFile(
    onlinePath: URI,
    localPath: URI,
    fileName: String
) : GlobalFile(onlinePath, localPath, fileName) {

    private val gJson: Gson = Gson()
    private val dataMap: MutableMap<String, Number> = mutableMapOf()

    init {
        if (exists()) {
            val data = fromJsonStrToNumberMap(readText())
            dataMap.putAll(data)
        }
    }


    fun storeDataAndOverwriteDuplication(data: Any) {
        dataMap.clear()
        val reflectedClass = data::class.java
        reflectedClass.declaredFields
            .filter { field -> field.type.isNumber() }
            .forEach { numberField ->
                val fieldAccessibilityBackup = numberField.isAccessible
                numberField.isAccessible = true
                dataMap[numberField.name] = numberField.get(data) as Number
                numberField.isAccessible = fieldAccessibilityBackup
            }
        writeText(gJson.toJson(dataMap))
    }

    fun storeDataAndAccumulateDuplication(data: Any) {
        val reflectedClass = data::class.java
        reflectedClass.declaredFields
            .filter { field ->
                field.type.isNumber()
            }.forEach { numberField ->
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
        writeText(gJson.toJson(dataMap))
    }

    override fun downloadOnlineVersion(fromDir: String) {
        super.downloadOnlineVersion(fromDir)
        dataMap.clear()
        dataMap.putAll(fromJsonStrToNumberMap(readText()))
    }


    fun toMap(): Map<String, Number> {
        return dataMap
    }

    private fun fromJsonStrToNumberMap(jsonStr: String): MutableMap<String, Number> = try {
        val mapType = object : TypeToken<MutableMap<String, Number>>() {}.type
        gJson.fromJson(jsonStr, mapType)
    } catch (e: JsonSyntaxException) {
        Log.d("JsonGlobalFile.fromJsonStrToNumberMap", "$jsonStr(Syntax is incorrect)")
        throw e
    }

}


