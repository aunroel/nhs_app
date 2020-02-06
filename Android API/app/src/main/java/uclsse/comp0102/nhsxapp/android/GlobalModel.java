package uclsse.comp0102.nhsxapp.android;

import android.os.Binder;
import android.util.Log;

import uclsse.comp0102.nhsxapp.android.asyncTasks.network.AsyncFileDownloadTask;

public class GlobalModel extends Binder {
    public boolean download(){
        Log.d("TEST:GlobalModel", "DOWNLOADING START");
        Log.d("TEST:GlobalModel", "DOWNLOADING END");
        return false;
    }

    public boolean upload(){
        Log.d("TEST:GlobalModel", "UPLOADING START");
        return false;
    }

    public boolean training(){
        Log.d("TEST:GlobalModel", "TRAINING  START");
        return false;
    }
}
