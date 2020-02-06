package uclsse.comp0102.nhsxapp.android.asyncTasks.network;

import uclsse.comp0102.nhsxapp.android.asyncTasks.TaskState;

public interface DownloadListener {
    void onProgress(int progress);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}
