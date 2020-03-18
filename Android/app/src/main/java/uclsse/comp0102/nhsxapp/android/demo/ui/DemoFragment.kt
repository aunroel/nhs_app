package uclsse.comp0102.nhsxapp.android.demo.ui

import android.app.AlertDialog
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
    private lateinit var insertButton: Button
    private lateinit var predictButton: Button
    private lateinit var uploadJsonButton: Button
    private lateinit var updateModelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_demo, container, false)
        insertButton = binding.findViewById(R.id.insert_button)
        predictButton = binding.findViewById<Button>(R.id.predict_button)
        uploadJsonButton = binding.findViewById<Button>(R.id.upload_button)
        updateModelButton = binding.findViewById<Button>(R.id.update_model)
        val appContext = context!!.applicationContext
        val nhsAPI = NhsAPI.getInstance(appContext)
        insertButton.setOnClickListener {
            InputDialogFragment().show(childFragmentManager, "Input Dialog")
        }
        predictButton.setOnClickListener {
            val traniningScore = nhsAPI.getTrainingScore(1000, 1000,1000)
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Predict Result")
            builder.setMessage("$traniningScore")
            builder.setPositiveButton("Okay", null)
            builder.show()
        }
        uploadJsonButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Upload Result")
            builder.setMessage("Success")
            builder.setPositiveButton("Okay", null)
            builder.show()
            AsyncTask.execute{
                nhsAPI.uploadJsonNow()
            }
        }
        updateModelButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Upload Result")
            builder.setMessage("SUCCESS")
            builder.setPositiveButton("Okay", null)
            builder.show()
            AsyncTask.execute{
                nhsAPI.updateTfModelNow()
            }
        }
        return binding.rootView
    }


}
