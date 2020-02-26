package uclsse.comp0102.nhsxapp.api

class NhsDataClass {

    var stepNum: Int = 0
    var callNum: Int = 0
    var txtNum: Int = 0
    var realScore: Int = 0
    var predictedScore: Int = 0
    var errorRate: Double = 0.0


    class Builder {
        private var stepNum: Int = 0
        private var callNum: Int = 0
        private var txtNum: Int = 0
        private var realScore: Int = 0
        private var predictedScore: Int = 0
        private var errorRate: Double = 0.0

        fun setStepNumber(expected: Int): Builder {
            stepNum = expected
            return this
        }

        fun setCallNumber(expected: Int): Builder {
            callNum = expected
            return this
        }

        fun setTextNumber(expected: Int): Builder {
            txtNum = expected
            return this
        }

        fun setRealScore(expected: Int): Builder {
            realScore = expected
            return this
        }

        fun setPredictedScore(expected: Int): Builder {
            predictedScore = expected
            return this
        }

        fun build(): NhsDataClass {
            return NhsDataClass().apply {
                stepNum = this@Builder.stepNum
                callNum = this@Builder.callNum
                txtNum = this@Builder.txtNum
                realScore = this@Builder.realScore
            }
        }
    }
}