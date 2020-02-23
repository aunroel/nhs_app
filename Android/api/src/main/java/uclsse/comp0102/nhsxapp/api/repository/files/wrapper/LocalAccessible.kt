package uclsse.comp0102.nhsxapp.api.repository.files.wrapper


interface LocalAccessible {
    fun writeBytes(bytes: ByteArray)
    fun readBytes(): ByteArray
    fun flush()
}
