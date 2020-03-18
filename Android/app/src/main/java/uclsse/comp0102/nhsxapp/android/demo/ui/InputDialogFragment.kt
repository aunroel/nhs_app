package uclsse.comp0102.nhsxapp.android.demo.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import uclsse.comp0102.nhsxapp.android.demo.R
import uclsse.comp0102.nhsxapp.api.NhsAPI
import uclsse.comp0102.nhsxapp.api.NhsTrainingDataHolder

class InputDialogFragment : DialogFragment() {

    private lateinit var supportCodeText: EditText
    private lateinit var wellBeingScoreNum: EditText
    private lateinit var weeklyStepsNum: EditText
    private lateinit var weeklyCallsNum: EditText
    private lateinit var weeklyTextsNum: EditText
    private lateinit var postCodeText: EditText

    private fun submitData(){
        val api = NhsAPI.getInstance(context!!.applicationContext)
        val data = NhsTrainingDataHolder().apply {
            supportCode = supportCodeText.text.toString()
            realWellBeingScore = wellBeingScoreNum.text.toString().toInt()
            weeklySteps = weeklyStepsNum.text.toString().toInt()
            weeklyCalls = weeklyCallsNum.text.toString().toInt()
            weeklyMessages = weeklyTextsNum.text.toString().toInt()
            postCode = postCodeText.text.toString()
        }
        api.record(data)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (activity == null) throw IllegalStateException("Acticvity is empty.")
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val binding = inflater.inflate(R.layout.fragement_input_dialog, null)
        supportCodeText = binding.findViewById(R.id.support_code_text)
        wellBeingScoreNum = binding.findViewById(R.id.wellbeing_score_number)
        weeklyStepsNum = binding.findViewById(R.id.weekly_step_number)
        weeklyCallsNum = binding.findViewById(R.id.weekly_calls_number)
        weeklyTextsNum = binding.findViewById(R.id.weekly_texts_number)
        postCodeText = binding.findViewById(R.id.post_code_text)
        dialogBuilder.setView(binding)
        dialogBuilder.setPositiveButton("Confirm"){ dialog, id ->
            AsyncTask.execute{submitData()}
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Data insert")
            builder.setMessage(
            """{ 
                "supportCode":${supportCodeText.text},
                "wellBeingScore":${wellBeingScoreNum.text},
                "weeklyStepsNum":${weeklyStepsNum.text},
                "weeklyCallsNum":${weeklyCallsNum.text},
                "weeklyTextsNum":${weeklyTextsNum.text}, 
                "postCodeText":${postCodeText.text}
            }""".trimIndent())
            builder.setPositiveButton("Okay", null)
            builder.show()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel"){ dialog, id ->
            dialog.dismiss()
        }
        return dialogBuilder.create()
    }



}
