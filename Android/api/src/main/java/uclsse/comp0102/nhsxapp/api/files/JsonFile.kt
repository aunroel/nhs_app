package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsxapp.api.extension.convertTo
import uclsse.comp0102.nhsxapp.api.extension.isJsonField
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.net.URL

/** A subclass of the OnlineFile
  * also encapsulate the google json class,
  * so that the class is able to convert instances of data class to json string,
  * able to send these data to server.
 */
class JsonFile(onHost: URL, subDirWithName: String, appContext: Context) :
    AbsOnlineFile(onHost, subDirWithName, appContext) {

    // Basic fields for posting
    private val hostAddress: URL = onHost
    private val targetDir: String = subDirWithName

    // fields about GSON
    private val gJson: Gson = Gson()
    private val utf8Charset = Charsets.UTF_8

    init {
        if (readBytes().isEmpty())
            writeStr("{}")
    }

    // implementation of the abstract functions

    /** upload the json file to server
     */
    override fun uploadCore() {
        val client = HttpClient(hostAddress)
        client.post(readStr(), targetDir)
    }

    /** update the local json file
     */
    override fun updateCore() {
        val newData = "{}"
        writeStr(newData)
    }

    // implementation for converting between
    // different classes and json string
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
        for (field in type.declaredFields) {
            val annotation = field.getDeclaredAnnotation(JsonData::class.java) ?: continue
            val name = annotation.name
            val value = dataMap[name] ?: continue
            val type = field.type
            val accessibleBak = field.isAccessible
            field.isAccessible = true
            field.set(tmpObject, value.convertTo(field.type))
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