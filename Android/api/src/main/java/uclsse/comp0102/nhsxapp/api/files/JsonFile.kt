package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.net.URL
import uclsse.comp0102.nhsxapp.api.extension.*

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

    // Fields about Json Converter
    private val jGson:Gson = Gson()
    private val utf8Charset = Charsets.UTF_8
    private val mapType = object : TypeToken<MutableMap<String, Any>>() {}.type


    init {
        if (readBytes().isEmpty())
            writeStr("{}")
    }

    /**
     * upload the json file to server
     */
    override fun uploadCore() {
        val client = HttpClient(hostAddress)
        client.uploadByPost(readStr(), targetDir)
    }

    /**
     * update the local json file
     */
    override fun updateCore() {
        throw NoSuchMethodException("A Json file cannot be update from online!")
    }

    /**
     * implementation for converting between
     * different classes and json string
     */
    fun readStr(): String{
        return readBytes().toString(utf8Charset)
    }

    fun writeStr(strData: String){
        writeBytes(strData.toByteArray(utf8Charset))
    }


    fun <T : Any> readObject(type: Class<T>): T {
        return fromStrToObject(readStr(), type)
    }

    fun writeObject(objData:Any) {
        writeStr(fromObjectToStr(objData))
    }


    fun mergeObject(inputObjData:Any){
        val curJsonStr = readStr()
        val inputJsonStr = fromObjectToStr(inputObjData)
        val curDataMap = jGson.fromJson(curJsonStr, mapType) as Map<String, Any>
        val inputDataMap = jGson.fromJson(inputJsonStr, mapType) as Map<String, Any>
        val newMap = (curDataMap.keys + inputDataMap.keys).associateWith {
            val newValue = jsonPropertyMerge(curDataMap[it], inputDataMap[it])
            it to newValue
        }
        val newMapJsonStr = jGson.toJson(newMap, mapType)
        writeStr(newMapJsonStr)
    }

    private fun <T:Any>jsonPropertyMerge(first: T?, second: T?): T?{
        if (first == null) return second
        if (second == null) return null
        @Suppress("UNCHECKED_CAST")
        return when {
            first.isString -> if(second == "") first else second
            first.isNumber -> ((first as Number) plus (second as Number)) as T
            else -> throw UnsupportedClassVersionError("only for string and number")
        }
    }

    private fun <T : Any> fromStrToObject(dataStr: String, type: Class<T>): T{
        val dataMap = jGson.fromJson(dataStr, mapType) as Map<String, Any>
        val tmpObject = type.getConstructor().newInstance()
        type.declaredFields.forEachWithAccess {
            val annotation = it.getDeclaredAnnotation(JsonData::class.java)
            val value = if (annotation != null) dataMap[annotation.name] else null
            if (value != null) it.set(tmpObject, value.convertTo(type))
        }
        return tmpObject
    }

    private fun fromObjectToStr(objData:Any):String{
        val extractedMap = mutableMapOf<String, Any>()
        objData::class.java.declaredJsonFields.forEachWithAccess {
            val annotation = it.getDeclaredAnnotation(JsonData::class.java)!!
            val name = annotation.name
            extractedMap[name] = it.get(objData)!!
        }
        return jGson.toJson(extractedMap)
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