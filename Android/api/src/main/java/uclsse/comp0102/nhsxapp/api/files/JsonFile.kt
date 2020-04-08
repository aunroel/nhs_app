package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uclsse.comp0102.nhsxapp.api.extension.convertTo
import uclsse.comp0102.nhsxapp.api.extension.declaredJsonFields
import uclsse.comp0102.nhsxapp.api.extension.forEachWithAccess
import uclsse.comp0102.nhsxapp.api.extension.plus
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.net.URL

/**
 * The class is A subclass of the OnlineFile and it also encapsulates the google json class,
 * so that the class is able to convert instances of data class to json string, as well as
 * to send these data to server.
 */
class JsonFile(
    onHost: URL, subDirWithName: String, appContext: Context
) : AbsOnlineFile(onHost, subDirWithName, appContext) {

    // Basic fields for posting
    private val hostAddress: URL = onHost
    private val targetDir: String = subDirWithName

    // Fields about Json Converter
    private val jGson:Gson = Gson()
    private val utf8Charset = Charsets.UTF_8
    private val mapType = object : TypeToken<MutableMap<String, Any>>() {}.type

    private var isDifferentialPrivacyRequired: Boolean = false


    init {
        if (readBytes().isEmpty())
            writeStr("{}")
    }

    fun useDifferentialPrivacy(value: Boolean){
        isDifferentialPrivacyRequired = value
    }

    /**
     * upload the json file to server
     */
    override fun uploadCore() {
        val client = HttpClient(hostAddress)
        val curJsonStr = readStr()
        if (! isDifferentialPrivacyRequired)
            client.uploadByPost(curJsonStr, targetDir)
        else readJsonWithDifferentialPrivacy(0.2).forEach{
            client.uploadByPost(it, targetDir)
        }
    }

    private fun readJsonWithDifferentialPrivacy(range:Double): List<String>{
        val resMapList = mutableListOf<String>()
        val curDataMap = jGson.fromJson(readStr(), mapType) as Map<String, Any>
        repeat(10){
            val differentialPrivacyMap = curDataMap.map { (key, value) ->
                if(value is Int){
                    val maxVal = (value * (1-range)).toInt()
                    val minVal = (value * (1+range)).toInt()
                    key to (minVal .. maxVal).random()
                }else{
                    (key to value)
                }
            }
            resMapList.add(jGson.toJson(differentialPrivacyMap, mapType))
        }
        return resMapList
    }

    /**
     * update the local json file
     */
    override fun updateCore() {
        throw NoSuchMethodException("A Json file cannot be update from online!")
    }

    /**
     * read json file from local and convert it to string.
     **/
    fun readStr(): String{
        return readBytes().toString(utf8Charset)
    }

    /**
     * write the json string into local .
     **/
    fun writeStr(strData: String){
        writeBytes(strData.toByteArray(utf8Charset))
    }


    /**
     * The method will return an instance of expected class according to the data stored in
     * the json file.
     **/
    fun <T : Any> readObject(type: Class<T>): T {
        return fromStrToObject(readStr(), type)
    }

    /**
     * The method will store an object into the json file. It will overwrite current data in the
     * file
     **/
    fun writeObject(objData:Any) {
        writeStr(fromObjectToStr(objData))
    }


    /**
     * The method will store an object into a json file. Compared with writeObject, it will merger
     * the current data into the file with the data in the object.
     */
    fun mergeObject(inputObjData:Any){
        val curJsonStr = readStr()
        val inputJsonStr = fromObjectToStr(inputObjData)
        val curDataMap = jGson.fromJson(curJsonStr, mapType) as Map<String, Any>
        val inputDataMap = jGson.fromJson(inputJsonStr, mapType) as Map<String, Any>
        val newMap = (curDataMap.keys + inputDataMap.keys).associateWith {
            jsonPropertyMerge(curDataMap[it], inputDataMap[it])
        }
        val newMapJsonStr = jGson.toJson(newMap, mapType)
        writeStr(newMapJsonStr)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T:Any>jsonPropertyMerge(first: T?, second: T?): T?{
        if (first == null) return second
        if (second == null) return null
        return when(first) {
            is String -> if(first != "") first else second
            is Number -> ((first as Number) plus (second as Number)) as T
            else -> throw UnsupportedClassVersionError("only for string and number")
        }
    }

    private fun <T : Any> fromStrToObject(dataStr: String, type: Class<T>): T{
        val dataMap = jGson.fromJson(dataStr, mapType) as Map<String, Any>
        val tmpObject = type.getConstructor().newInstance()
        type.declaredFields.forEachWithAccess {
            val annotation = it.getDeclaredAnnotation(JsonData::class.java)
            val value = if (annotation != null) dataMap[annotation.name] else null
            if (value != null) it.set(tmpObject, value.convertTo(it.type))
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