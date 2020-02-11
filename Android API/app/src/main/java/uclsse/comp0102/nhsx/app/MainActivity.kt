package uclsse.comp0102.nhsx.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import uclsse.comp0102.nhsx.android.NhsAPI
import uclsse.comp0102.nhsx.android.tools.net.DownloadManager
import uclsse.comp0102.nhsxapp.nhsapp.R
import java.io.File
import java.net.URL

class MainActivity : AppCompatActivity() {

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nhsApi = NhsAPI.getInstance(this)
        val file = File(this.filesDir, "test.txt")
        val url = URL("https://raw.githubusercontent.com/aooXu/Lecture/master/")
        val manager = DownloadManager(url, file)
        manager.download()
        println(file.inputStream().readBytes().decodeToString())    }



}
