package uclsse.comp0102.nhsxapp.android.demo.ui

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import uclsse.comp0102.nhsxapp.android.demo.R
import uclsse.comp0102.nhsxapp.api.NhsAPI


class DemoFragment : Fragment() {
    private lateinit var resultText: TextView
    private lateinit var predictButton: Button
    private lateinit var uploadJsonButton: Button
    private lateinit var updateModelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_demo, container, false)
        resultText = binding.findViewById<Button>(R.id.result_textview)
        predictButton = binding.findViewById<Button>(R.id.predict_button)
        uploadJsonButton = binding.findViewById<Button>(R.id.upload_button)
        updateModelButton = binding.findViewById<Button>(R.id.update_model)
        val appContext = context!!.applicationContext
        val nhsAPI = NhsAPI.getInstance(appContext)
        predictButton.setOnClickListener {
            resultText.text = """{"weeklySteps": 1000, "weeklyCalls": 1000, "weeklyTexts": 1000 }"""
            val result = nhsAPI.getTrainingScore(1000, 1000, 1000)
            resultText.text = "Result: $result"

        }
        uploadJsonButton.setOnClickListener {
            AsyncTask.execute{
                nhsAPI.uploadJsonNow()
            }
        }
        updateModelButton.setOnClickListener {
            AsyncTask.execute{
                nhsAPI.updateTfModelNow()
            }
        }
        return binding.rootView
    }


}
