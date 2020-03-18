package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import uclsse.comp0102.nhsxapp.api.files.online.HttpClient
import java.math.BigInteger
import java.net.URL
import java.nio.charset.Charset
import java.security.MessageDigest
import kotlin.random.Random

/** Registration File manage the unique id of the user,
  * it will check if the user has already registered
  * on the server. If not, it will randomly generate a
  * uID by the SHA256 algorithm and sent the uID to
  * the server for registration.
 */
class RegistrationFile(
    onHost: URL, subDirWithName: String, appContext: Context
) : AbsOnlineFile(onHost, subDirWithName, appContext) {

    private val hostAddress: URL = onHost
    private val dirWithName: String = subDirWithName

    val uID: String
        get() = readBytes().toString(Charset.defaultCharset())

    init {
        if(lastUpdateTime == 0L){
            registerUtilSuccessOrThrowException()
        }
    }

    /** Register a randomly generated UID
     * Repeat the code 10 time to avoid possible collision of UID
     */
    private fun registerUtilSuccessOrThrowException(){
        repeat(10){
            val randomID = createRandomID()
            writeBytes(randomID.toByteArray(Charsets.UTF_8))
            if(upload()) return
        }
        throw IllegalStateException("CANNOT REGISTRATION!")
    }

    /** Randomly generate a UID by SHA-256 Algorithm
     */
    private fun createRandomID(): String{
        val randomNum = Random.nextLong(Long.MAX_VALUE)
        val randomStr = "uID:${randomNum}"
        val md = MessageDigest.getInstance("SHA-256")
        md.update(randomStr.toByteArray(Charsets.UTF_8))
        val digest = md.digest()
        return java.lang.String.format("%064x", BigInteger(1, digest))
    }

    override fun uploadCore() {
        val httpClient = HttpClient(hostAddress)
        val uID = readBytes().toString(Charsets.UTF_8)
        val jsonStr = """{"uid": "$uID"}"""
        httpClient.uploadByPost(jsonStr, dirWithName)
    }

    override fun updateCore() {
        TODO("Not yet implemented")
    }
}