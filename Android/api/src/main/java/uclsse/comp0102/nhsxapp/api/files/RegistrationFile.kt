package uclsse.comp0102.nhsxapp.api.files

import android.content.Context
import java.net.URL
import java.nio.charset.Charset

class RegistrationFile(
    onHost: URL, subDirWithName: String, appContext: Context
) : AbsOnlineFile(onHost, subDirWithName, appContext) {

    val uID: String
        get() = readBytes().toString(Charset.defaultCharset())

    init {
        if(lastUpdateTime == 0L) {
            register()
        }
    }

    private fun register(){
        //TODO Registration
        writeBytes("e01a469f-abfd-48a6-864f-4f796613b7c4".toByteArray(Charset.defaultCharset()))
    }

    override fun updateCore() {
        TODO("Not yet implemented")
    }

    override fun uploadCore() {
        TODO("Not yet implemented")
    }
}