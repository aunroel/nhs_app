package uclsse.comp0102.nhsx.android.tools

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsx.android.extension.isNumber
import uclsse.comp0102.nhsx.android.extension.plus
import java.net.URI

class JsonGlobalFile(
    onlinePath: URI,
    localPath: URI,
    fileName: String
) : GlobalFile(onlinePath, localPath, fileName) {

    companion object {
        private val gJson = Gson()
    }

    private val dataMap: MutableMap<String, Number> = mutableMapOf<String, Number>()


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

    override fun pull(fromDir: String) {
        super.pull(fromDir)
        val mapType = object : TypeToken<MutableMap<String, Number>>() {}.type
        val map: MutableMap<String, Number> = gJson.fromJson(readText(), mapType)
        dataMap.clear()
        dataMap.putAll(map)
    }

}


