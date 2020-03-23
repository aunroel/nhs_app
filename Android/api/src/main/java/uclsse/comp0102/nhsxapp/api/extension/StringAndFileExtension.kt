package uclsse.comp0102.nhsxapp.api.extension

import java.io.File
import java.net.URL
import kotlin.text.Regex.Companion.escapeReplacement

fun String.formatSubDir(): String {
    val validStr = this.replace("//", "/").removeSurrounding("/")
    return escapeReplacement(validStr)
}

fun String.toURL(): URL {
    return URL(escapeReplacement(this))
}

fun File.createNewFileWithDirIfNotExist() {
    if (this.exists()) return
    val parentFile = this.parentFile
    if (parentFile != null && !parentFile.exists())
        parentFile.mkdirs()
    this.createNewFile()
}