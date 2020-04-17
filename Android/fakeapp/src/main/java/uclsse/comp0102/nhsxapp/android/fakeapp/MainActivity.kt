package uclsse.comp0102.nhsxapp.android.fakeapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import uclsse.comp0102.nhsxapp.api.NhsAPI
import uclsse.comp0102.nhsxapp.api.NhsTrainingDataHolder

class MainActivity : AppCompatActivity() {

    private lateinit var stepsCountText: EditText
    private lateinit var callsCountText: EditText
    private lateinit var txtsCountText: EditText
    private lateinit var scoreText: TextView
    private lateinit var scoreSeekBar:SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepsCountText = findViewById(R.id.step_value)
        callsCountText = findViewById(R.id.text_value)
        txtsCountText = findViewById(R.id.text_value)
        scoreText = findViewById(R.id.score_view)
        scoreSeekBar = findViewById(R.id.seekBar)
        scoreSeekBar.setOnSeekBarChangeListener(scoreUpdater)
        findViewById<Button>(R.id.submit_button).setOnClickListener(onclickSubmitButton)
    }

    private val onclickSubmitButton = View.OnClickListener{
        Toast.makeText(applicationContext, "Inserting", Toast.LENGTH_SHORT).show()
        val tmpData = NhsTrainingDataHolder()
        tmpData.weeklySteps = stepsCountText.text.toString().toInt()
        tmpData.weeklyCalls = callsCountText.text.toString().toInt()
        tmpData.weeklyMessages = txtsCountText.text.toString().toInt()
        tmpData.realWellBeingScore = scoreSeekBar.progress
        val nhsAPI = NhsAPI(application)
        nhsAPI.record(tmpData).observe(this, Observer{recordResult->
            val text = "Inserting is ${if (recordResult) "success" else "failure"}"
            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, "Uploading", Toast.LENGTH_SHORT).show()
            nhsAPI.uploadJsonNow().observe(this, Observer {uploadResult->
                val tmpText = "Uploading is ${if (uploadResult) "success" else "failure"}"
                Toast.makeText(applicationContext, tmpText, Toast.LENGTH_LONG).show()
            })
        })
    }

    private val scoreUpdater = object : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            scoreText.text = (progress/10).toString()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }


}
