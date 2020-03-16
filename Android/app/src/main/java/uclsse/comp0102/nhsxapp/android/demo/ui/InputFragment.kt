package uclsse.comp0102.nhsxapp.android.demo.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uclsse.comp0102.nhsxapp.android.demo.R
import uclsse.comp0102.nhsxapp.api.NhsAPI
import uclsse.comp0102.nhsxapp.api.NhsTrainingDataHolder


class InputFragment : Fragment() {

    private lateinit var supportCodeText: EditText
    private lateinit var wellBeingScoreNum: EditText
    private lateinit var weeklyStepsNum: EditText
    private lateinit var weeklyCallsNum: EditText
    private lateinit var weeklyTextsNum: EditText
    private lateinit var postCodeText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_input, container, false)
        supportCodeText = binding.findViewById(R.id.support_code_text)
        wellBeingScoreNum = binding.findViewById(R.id.wellbeing_score_number)
        weeklyStepsNum = binding.findViewById(R.id.weekly_step_number)
        weeklyCallsNum = binding.findViewById(R.id.weekly_calls_number)
        weeklyTextsNum = binding.findViewById(R.id.weekly_texts_number)
        postCodeText = binding.findViewById(R.id.post_code_text)
        submitButton = binding.findViewById(R.id.submit_input_button)
        submitButton.setOnClickListener{
            AsyncTask.execute { submitData() }
        }
        return binding.rootView
    }

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
        findNavController().navigate(R.id.action_inputFragment_to_demonFragment)
    }



}
