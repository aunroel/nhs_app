package uclsse.comp0102.nhsxapp.android.demo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
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

    private lateinit var nhsAPI:  NhsAPI
    private lateinit var appContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_demo, container, false)
        insertButton = binding.findViewById(R.id.insert_button)
        predictButton = binding.findViewById(R.id.predict_button)
        uploadJsonButton = binding.findViewById(R.id.upload_button)
        updateModelButton = binding.findViewById(R.id.update_model)
        return binding.rootView
    }

    override fun onStart() {
        super.onStart()
        appContext = context!!.applicationContext
        nhsAPI = ViewModelProvider(this).get(NhsAPI::class.java)
        insertButton.setOnClickListener(onClickInsertButton)
        predictButton.setOnClickListener(onClickPredictButton)
        uploadJsonButton.setOnClickListener(onClickUploadButton)
        updateModelButton.setOnClickListener(onClickUpdateButton)
    }

    override fun onDestroyView() {
        viewLifecycleOwner.lifecycleScope.cancel()
        super.onDestroyView()
    }

    private val onClickInsertButton = View.OnClickListener {
        InputDialogFragment().show(childFragmentManager, "Input Dialog")
    }

    private val onClickPredictButton = View.OnClickListener {
        Toast.makeText(appContext, "Loading", Toast.LENGTH_SHORT).show()
        nhsAPI.getTrainingScoreFromRecords().observe(viewLifecycleOwner, Observer {result ->
            Toast.makeText(appContext, "Predict Score: $result", Toast.LENGTH_SHORT).show()
        })
    }

    private val onClickUploadButton = View.OnClickListener {
        Toast.makeText(appContext, "Loading", Toast.LENGTH_SHORT).show()
        nhsAPI.uploadJsonNow().observe(viewLifecycleOwner, Observer {isSuccess ->
            val text = if(isSuccess) "Done" else "FAIL"
            Toast.makeText(appContext, "Upload Result: $text", Toast.LENGTH_SHORT).show()
        })
    }

    private val onClickUpdateButton = View.OnClickListener {
        Toast.makeText(appContext, "Loading", Toast.LENGTH_SHORT).show()
        nhsAPI.updateTfModelNow().observe(viewLifecycleOwner, Observer { isSuccess ->
            val text = if(isSuccess) "Done" else "FAIL"
            Toast.makeText(appContext, "Update Result: $text", Toast.LENGTH_SHORT).show()
        })
    }
}
