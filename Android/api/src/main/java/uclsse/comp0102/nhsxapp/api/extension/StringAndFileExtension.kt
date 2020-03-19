package uclsse.comp0102.nhsxapp.api.extension

import java.io.File
import java.net.URL

fun String.formatSubDir(): String {
    return this.replace("//", "/")
        .removeSurrounding("/")
}

fun String.toURL(): URL {
    return URL(this)
}

fun File.createNewFileWithDirIfNotExist() {
    if (this.exists()) return
    val parentFile = this.parentFile
    if (parentFile != null && !parentFile.exists())
        parentFile.mkdirs()
    this.createNewFile()
}