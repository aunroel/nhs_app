package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import uclsse.comp0102.nhsxapp.api.extension.isJsonField
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.lang.Exception
import java.net.URL

class JsonFile(onHost: URL, subDirWithName: String, appContext: Context) :
    AbsOnlineFile(onHost, subDirWithName, appContext) {

    private val hostAddress: URL = onHost
    private val targetDir: String = subDirWithName

    private val gJson: Gson = Gson()
    private val utf8Charset = Charsets.UTF_8

    init {
        if (readBytes().isEmpty())
            writeStr("{}")
    }

    override fun uploadCore() {
        val client = HttpClient(hostAddress)
        client.post(readStr(), targetDir)
    }

    override fun updateCore() {
        val newData = "{}"
        writeStr(newData)
    }

    fun writeObject(data:Any) {
        val dataReflex = data::class.java
        val fields = dataReflex.declaredFields.filter {it.isJsonField()}
        val dataMap = fields.associate {
            val accessBak = it.isAccessible
            it.isAccessible = true
            val value = it.get(data)!!
            it.isAccessible = accessBak
            val annotation = it.getDeclaredAnnotation(JsonData::class.java)!!
            val name = annotation.name
            name to value
        }
        val newDataStr = gJson.toJson(dataMap)
        writeStr(newDataStr)
    }

    fun <T : Any> readObject(type: Class<T>): T {
        val jsonStr = readStr()
        val dataMap = fromJsonStrToNumberMap(jsonStr)
        val tmpObject = type.getConstructor().newInstance()
        dataMap.forEach { (fieldName, fieldValue) ->
            val field = type.getDeclaredField(fieldName)
            val accessibleBak = field.isAccessible
            field.isAccessible = true
            field.set(tmpObject, fieldValue)
            field.isAccessible = accessibleBak
        }
        return return tmpObject
    }

    private fun readStr(): String{
        return readBytes().toString(utf8Charset)
    }

    private fun writeStr(value: String){
        writeBytes(value.toByteArray(utf8Charset))
    }

    private fun fromJsonStrToNumberMap(jsonStr: String): MutableMap<String, Any> {
        val mapType = object : TypeToken<MutableMap<String, Any>>() {}.type
        return gJson.fromJson(jsonStr, mapType)
    }
}