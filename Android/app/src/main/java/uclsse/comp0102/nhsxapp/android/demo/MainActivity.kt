package uclsse.comp0102.nhsxapp.android.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uclsse.comp0102.nhsxapp.android.demo.ui.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}
