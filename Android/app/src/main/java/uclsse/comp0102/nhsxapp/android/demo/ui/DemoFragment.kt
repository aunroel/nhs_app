package uclsse.comp0102.nhsxapp.android.demo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancel
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
        return binding.rootView
    }

    override fun onDestroyView() {
        viewLifecycleOwner.lifecycleScope.cancel()
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        val appContext = context!!.applicationContext
        val nhsAPI = ViewModelProvider(this).get(NhsAPI::class.java)
        insertButton.setOnClickListener {
            InputDialogFragment().show(childFragmentManager, "Input Dialog")
        }
        predictButton.setOnClickListener {
            val trainingScore = nhsAPI.getTrainingScore()
            trainingScore.observe(viewLifecycleOwner, Observer {result ->
                val text = result ?: "Loading"
                Toast.makeText(appContext, "Predict Score: $text", Toast.LENGTH_SHORT).show()
            })
            nhsAPI.updateTrainingScore()
        }
        uploadJsonButton.setOnClickListener {
            nhsAPI.uploadJsonNow().observe(viewLifecycleOwner, Observer {isSuccess ->
                val text = if (isSuccess == null) "Loading" else if(isSuccess) "Done" else "FAIL"
                Toast.makeText(appContext, "Upload Result: $text", Toast.LENGTH_SHORT).show()
            })
        }
        updateModelButton.setOnClickListener{
            nhsAPI.uploadJsonNow().observe(viewLifecycleOwner, Observer { isSuccess ->
                val text = if (isSuccess == null) "Loading" else if(isSuccess) "Done" else "FAIL"
                Toast.makeText(appContext, "Upload Result: $text", Toast.LENGTH_SHORT).show()
            })
        }
    }
}
