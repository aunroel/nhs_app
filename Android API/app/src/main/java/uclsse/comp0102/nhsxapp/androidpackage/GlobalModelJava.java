package uclsse.comp0102.nhsxapp.androidpackage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GlobalModelJava {

    private String globalPath;
    private String localPath;

    public GlobalModelJava(String pathOfGlobalModel, String pathOfLocalModel){
        this.globalPath = pathOfGlobalModel;
        this.localPath = pathOfLocalModel;
    }

    public boolean uploadModel() {
        try {
            URL url = new URL(globalPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data");

            FileInputStream localFileReader = new FileInputStream(localPath);
            DataOutputStream globalFileWriter = new DataOutputStream(connection.getOutputStream());

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int length = -1;
            while ((length = localFileReader.read(buffer)) != -1) {
                globalFileWriter.write(buffer, 0, length);
            }
            globalFileWriter.flush();

            localFileReader.close();
            globalFileWriter.close();

            if (connection.getResponseCode() == 200) {
                System.out.println("Success: the file has already been uploaded to " + localPath);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail： an error occurs when upload " + localPath);
            System.err.println("Error Message：" + e.toString());
        }
        return false;
    }

    public boolean downloadModel() {
        try {
            URL url = new URL(globalPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setConnectTimeout(50000);
            connection.setReadTimeout(50000);

            InputStream globalFileReader = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            ByteArrayOutputStream templeByteWriter = new ByteArrayOutputStream();
            while ((len = globalFileReader.read(buffer)) != -1) {
                templeByteWriter.write(buffer, 0, len);
            }
            templeByteWriter.close();
            byte[] getData = templeByteWriter.toByteArray();

            File localFile = new File(localPath);
            FileOutputStream localFileWriter = new FileOutputStream(localFile);
            localFileWriter.write(getData);
            localFileWriter.flush();

            localFileWriter.close();
            globalFileReader.close();

            System.out.println("Success: the file has already been downloaded to " + localPath);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fail： an error occurs when downloading " + globalPath);
            System.err.println("Error Message：" + e.toString());
            return false;
        }
    }

}
