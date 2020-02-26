package uclsse.comp0102.nhsxapp.api.background.tasks

class TaskHolder {
    var innerTask: () -> Unit = {}
}