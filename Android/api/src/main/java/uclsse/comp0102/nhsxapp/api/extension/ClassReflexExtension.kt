package uclsse.comp0102.nhsxapp.api.extension

import uclsse.comp0102.nhsxapp.api.files.JsonFile
import java.lang.reflect.Field

fun Array<Field>.forEachWithAccess(doAction:(Field)->Unit){
    this.forEach {
        val accessibleBak = it.isAccessible
        it.isAccessible = true
        doAction(it)
        it.isAccessible = accessibleBak
    }
}

val Class<out Any>.declaredJsonFields: Array<Field>
    get() = this.declaredFields.filter {
        it.getDeclaredAnnotation(JsonFile.JsonData::class.java) != null
    }.toTypedArray()