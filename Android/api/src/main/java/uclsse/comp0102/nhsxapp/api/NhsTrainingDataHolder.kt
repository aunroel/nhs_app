package uclsse.comp0102.nhsxapp.api

import uclsse.comp0102.nhsxapp.api.files.JsonData
import kotlin.math.abs


class NhsTrainingDataHolder {
    @JsonData(name = "supportCode")
    var supportCode: String = "Therapist"

    @JsonData(name = "postCode")
    var postCode: String = "LL25"

    @JsonData(name = "weeklySteps")
    var weeklySteps: Int = 0

    @JsonData(name = "weeklyCalls")
    var weeklyCalls: Int = 0

    @JsonData(name = "weeklyMessages")
    var weeklyMessages:Int = 0

    @JsonData(name = "errorRate")
    private var errorRate:Int = 0

    private val errorRateFormula = {real:Int, predict:Int ->
        abs(real - predict).times(100).div(real)
    }

    @JsonData(name = "wellBeingScore")
    private var _realWellBeingScore:Int = 1

    @JsonData(name = "predictWellBeingScore")
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