package uclsse.comp0102.nhsx.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uclsse.comp0102.nhsx.android.tools.net.GlobalFile
import uclsse.comp0102.nhsxapp.nhsapp.R
import java.net.URI


class MainActivity : AppCompatActivity() {


    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val onlineUrl = URI.create("https://raw.githubusercontent.com/aooXu/Lecture/master/")
        val localUri = this.filesDir.toURI()
        val name = "README.md"
        val file = GlobalFile(onlineUrl, localUri, name)

    }
}
