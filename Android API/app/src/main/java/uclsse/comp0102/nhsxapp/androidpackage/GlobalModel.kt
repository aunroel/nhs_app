package uclsse.comp0102.nhsxapp.androidpackage

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class GlobalModelKotlin(private val globalPath: String, private val localPath: String) {
    fun uploadModel(): Boolean {
        try {
            val url = URL(globalPath)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false
            connection.connectTimeout = 50000
            connection.readTimeout = 50000
            connection.requestMethod = "POST"
            connection.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty("Content-Type", "multipart/form-data")
            val localFileReader = FileInputStream(localPath)
            val globalFileWriter =
                DataOutputStream(connection.outputStream)
            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)
            var length = -1
            while (localFileReader.read(buffer).also { length = it } != -1) {
                globalFileWriter.write(buffer, 0, length)
            }
            globalFileWriter.flush()
            localFileReader.close()
            globalFileWriter.close()
            if (connection.responseCode == 200) {
                println("Success: the file has already been uploaded to $localPath")
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Fail： an error occurs when upload $localPath")
            System.err.println("Error Message：$e")
        }
        return false
    }

    fun downloadModel(): Boolean {
        return try {
            val url = URL(globalPath)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false
            connection.connectTimeout = 50000
            connection.readTimeout = 50000
            val globalFileReader = connection.inputStream
            val buffer = ByteArray(1024)
            var len: Int
            val templeByteWriter = ByteArrayOutputStream()
            while (globalFileReader.read(buffer).also { len = it } != -1) {
                templeByteWriter.write(buffer, 0, len)
            }
            templeByteWriter.close()
            val getData = templeByteWriter.toByteArray()
            val localFile = File(localPath)
            val localFileWriter = FileOutputStream(localFile)
            localFileWriter.write(getData)
            localFileWriter.flush()
            localFileWriter.close()
            globalFileReader.close()
            println("Success: the file has already been downloaded to $localPath")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            println("Fail： an error occurs when downloading $globalPath")
            System.err.println("Error Message：$e")
            false
        }
    }

}