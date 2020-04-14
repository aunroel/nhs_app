package uclsse.comp0102.nhsxapp.android.demo.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private lateinit var confirmButton: Button

    private val data: NhsTrainingDataHolder
        get() = NhsTrainingDataHolder().apply {
            supportCode = supportCodeText.text.toString()
            realWellBeingScore = wellBeingScoreNum.text.toString().toInt()
            weeklySteps = weeklyStepsNum.text.toString().toInt()
            weeklyCalls = weeklyCallsNum.text.toString().toInt()
            weeklyMessages = weeklyTextsNum.text.toString().toInt()
            postCode = postCodeText.text.toString()
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val binding = inflater.inflate(R.layout.fragement_input_dialog, null)
        supportCodeText = binding.findViewById(R.id.support_code_text)
        wellBeingScoreNum = binding.findViewById(R.id.wellbeing_score_number)
        weeklyStepsNum = binding.findViewById(R.id.weekly_step_number)
        weeklyCallsNum = binding.findViewById(R.id.weekly_calls_number)
        weeklyTextsNum = binding.findViewById(R.id.weekly_texts_number)
        postCodeText = binding.findViewById(R.id.post_code_text)
        confirmButton = binding.findViewById(R.id.confirm_button)
        dialogBuilder.setView(binding)
        return dialogBuilder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        confirmButton.setOnClickListener(onClickSubmitButton)
        return view
    }

    private val onClickSubmitButton = View.OnClickListener {
        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
        val nhsAPI = ViewModelProvider(this).get(NhsAPI::class.java)
        nhsAPI.record(data).observe(this as LifecycleOwner, Observer{
            val appContext = context?.applicationContext
            val text = "Insert Result: ${if (it) "Success" else "Failure"}"
            Toast.makeText(appContext, text, Toast.LENGTH_LONG).show()
            this.dismiss()
        })
    }
}