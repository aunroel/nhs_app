package uclsse.comp0102.nhsx.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uclsse.comp0102.nhsxapp.nhsapp.R


class MainActivity : AppCompatActivity() {


    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
