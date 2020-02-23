package uclsse.comp0102.nhsxapp.api.repository.files.wrapper

interface OnlineAccessible {
    fun update(): Boolean
    fun upload(): Boolean
}
