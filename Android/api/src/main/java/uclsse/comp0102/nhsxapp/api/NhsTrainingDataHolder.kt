package uclsse.comp0102.nhsxapp.api

import uclsse.comp0102.nhsxapp.api.files.JsonFile


/**
 * # 1. Description:
 * The class is used to specify the data will used in the json file, the annotation
 * @JsonFile.JsonData tells the json file class which data need to be stored and their name in the
 * json file.
 */
class NhsTrainingDataHolder {
    @JsonFile.JsonData(name = "supportCode")
    var supportCode: String = ""

    @JsonFile.JsonData(name = "postCode")
    var postCode: String = ""

    @JsonFile.JsonData(name = "weeklySteps")
    var weeklySteps: Int = 0

    @JsonFile.JsonData(name = "weeklyCalls")
    var weeklyCalls: Int = 0

    @JsonFile.JsonData(name = "weeklyMessages")
    var weeklyMessages:Int = 0

    @JsonFile.JsonData(name = "errorRate")
    var errorRate:Int = 0

    private val errorRateFormula = {real:Int, predict:Int -> real - predict }

    @JsonFile.JsonData(name = "wellBeingScore")
    private var _realWellBeingScore:Int = 0

    @JsonFile.JsonData(name = "predictWellBeingScore")
    private var _predictedWellBeingScore:Int = 0


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

    fun mergeWithNewData(newData: NhsTrainingDataHolder){
        this.weeklyMessages += newData.weeklyMessages;
        this.weeklyCalls += newData.weeklyCalls
        this.weeklySteps += newData.weeklySteps
        val newPostCode = newData.postCode
        if (newPostCode != "") this.postCode = newPostCode
        val newSupportCode = newData.supportCode
        if (newSupportCode != "") this.supportCode = newData.supportCode
        val newRealWellBeing = newData.realWellBeingScore
        if (newRealWellBeing != 0) this.realWellBeingScore = newRealWellBeing
        val newPredictWellBeing = newData.predictedWellBeingScore
        if (newPredictWellBeing != 0) this.predictedWellBeingScore = newPredictWellBeing
    }

    fun deeplyClone(): NhsTrainingDataHolder{
        val targetOne = this
        return NhsTrainingDataHolder().apply {
            this.predictedWellBeingScore = targetOne.predictedWellBeingScore
            this.realWellBeingScore = targetOne.realWellBeingScore
            this.weeklySteps = targetOne.weeklySteps
            this.weeklyMessages = targetOne.weeklyMessages
            this.weeklyCalls = targetOne.weeklyCalls
            this.postCode = targetOne.postCode
            this.supportCode = targetOne.supportCode
        }
    }
}