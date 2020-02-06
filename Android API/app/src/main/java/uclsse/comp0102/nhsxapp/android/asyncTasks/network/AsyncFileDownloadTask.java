package uclsse.comp0102.nhsxapp.android.asyncTasks.network;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uclsse.comp0102.nhsxapp.android.asyncTasks.TaskState;

public class AsyncFileDownloadTask extends AsyncTask<Void, Integer, TaskState> {

    private Boolean isPaused;
    private Boolean isCancelled;
    private String url;
    private File file;
    private int lastProgress;
    private DownloadListener listener;


    public AsyncFileDownloadTask(DownloadListener listener, File file, String url){
        isPaused = false;
        isCancelled = false;

        this.url = url;
        this.file = file;
        this.listener = listener;
    }

    @Override
    protected TaskState doInBackground(Void ... v) {
        InputStream inStream = null;
        RandomAccessFile savedFile = null;
        try {
            long downloadedLength = file.length();
            long contentLength = getContentLength();
            if(contentLength == 0) return TaskState.FAILED;
            if(contentLength == downloadedLength) return TaskState.SUCCESS;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes="+downloadedLength+"-")
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.body() != null) return TaskState.FAILED;
            inStream = response.body().byteStream();
            savedFile = new RandomAccessFile(file, "rw");
            savedFile.seek(downloadedLength);
            byte[] bytesList = new byte[1024];
            int total = 0;
            int len;
            while ((len = inStream.read(bytesList)) != -1){
                if (isCancelled) return TaskState.CANCELED;
                if (isPaused) return TaskState.PAUSED;
                total += len;
                savedFile.write(bytesList, 0, len);
                int progress = (int) (100*(total+downloadedLength)/contentLength);
                publishProgress(progress);
            }
            response.body().close();
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                if(inStream != null) inStream.close();
                if(savedFile != null) savedFile.close();
                if(isCancelled && file != null) file.delete();
            } catch (IOException ignore){}
        }
        return TaskState.SUCCESS;
    }

    private int getContentLength(){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if(response.body()==null || !response.isSuccessful()) return 0;
            long contentLength = response.body().contentLength();
            response.body().close();
            return (int) contentLength;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values){
        int progress = values[0];
        if(progress>lastProgress){
            listener.onProgress(progress);
            lastProgress =  progress;
        }
    }

    @Override
    protected void onPostExecute(TaskState state){
        switch (state){
            case SUCCESS:
                listener.onSuccess();
                break;
            case FAILED:
                listener.onFailed();
                break;
            case PAUSED:
                listener.onPaused();
                break;
            case CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload(){
        isPaused = true;
    }

    public void cancelDownlod(){
        isCancelled = true;
    }
}
