package uclsse.comp0102.nhsx.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import uclsse.comp0102.nhsx.android.NhsAPI
import uclsse.comp0102.nhsxapp.nhsapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nhsApi = NhsAPI.getInstance(this)

        Log.d("TEST_MAIN_ACTIVITY", "FINISH")
    }
}
