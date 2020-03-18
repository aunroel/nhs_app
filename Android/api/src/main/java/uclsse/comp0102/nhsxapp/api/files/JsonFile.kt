package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsxapp.api.extension.convertTo
import uclsse.comp0102.nhsxapp.api.extension.isJsonField
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.net.URL

// It is a subclass of the OnlineFile and
// it also encapsulate the google json class,
// so that the class is able to convert instances
// of data class to json string and then, it also
// is able to send these data to server.
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
    override fun uploadCore() {
        val client = HttpClient(hostAddress)
        client.uploadByPost(readStr(), targetDir)
    }

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

    // JsonData annotation is used to specify the
    // json properties in a data class. The data with
    // such annotation will be extracted and stored
    // in a json file. Besides, its name will be
    // rewritten.
    @MustBeDocumented
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FIELD)
    annotation class JsonData(val name: String)
}