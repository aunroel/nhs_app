package uclsse.comp0102.nhsxapp.api

import uclsse.comp0102.nhsxapp.api.files.JsonFile
import kotlin.math.abs


class NhsTrainingDataHolder {
    @JsonFile.JsonData(name = "supportCode")
    var supportCode: String = "Therapist"

    @JsonFile.JsonData(name = "postCode")
    var postCode: String = "LL25"

    @JsonFile.JsonData(name = "weeklySteps")
    var weeklySteps: Int = 0

    @JsonFile.JsonData(name = "weeklyCalls")
    var weeklyCalls: Int = 0

    @JsonFile.JsonData(name = "weeklyMessages")
    var weeklyMessages:Int = 0

    @JsonFile.JsonData(name = "errorRate")
    private var errorRate:Int = 0

    private val errorRateFormula = {real:Int, predict:Int ->
        abs(real - predict).times(100).div(real)
    }

    @JsonFile.JsonData(name = "wellBeingScore")
    private var _realWellBeingScore:Int = 1

    @JsonFile.JsonData(name = "predictWellBeingScore")
    private var _predictedWellBeingScore:Int = 1


    var realWellBeingScore: Int
        get() = _realWellBeingScore
        set(value) {
            _realWellBeingScore = value
            errorRate = errorRateFormula(realWellBeingScore, _predictedWellBeingScore)
        }

    var predictedWellBeingScore: Int
        get() = _predictedWellBeingScore
        set(value) {
            _predictedWellBeingScore = value
            errorRate = errorRateFormula(_realWellBeingScore, _predictedWellBeingScore)
        }
}