package uclsse.comp0102.nhsxapp.android.demo

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uclsse.comp0102.nhsxapp.api.NhsAPI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AsyncTask.execute{ NhsAPI.getInstance(applicationContext) }
        setContentView(R.layout.main_activity)
    }
}
