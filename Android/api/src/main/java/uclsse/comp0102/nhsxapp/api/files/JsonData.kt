package uclsse.comp0102.nhsxapp.api.files

// JsonData annotation is used to specify the
// json properties in a data class. The data with
// such annotation will be extracted and stored
// in a json file. Besides, its name will be
// rewritten.
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class JsonData(val name: String)