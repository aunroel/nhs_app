package uclsse.comp0102.nhsxapp.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import uclsse.comp0102.nhsxapp.activitytest.FirstActivity;
import uclsse.comp0102.nhsxapp.activitytest.R;

public class GlobalModelService extends Service {

    private GlobalModel model = new GlobalModel();

    public GlobalModelService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("TEST: GlobalModelService", "HAS CREATED");
        Intent intent = new Intent(this, FirstActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationChannel channel = new NotificationChannel(
                "comp0102", "NHSX_BACKGROUND", NotificationManager.IMPORTANCE_DEFAULT
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification notification= new NotificationCompat.Builder(this, "comp0102")
                .setAutoCancel(true)
                .setContentTitle("NHSX ")
                .setContentText("Background Serve is Running")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d("TEST: GlobalModelService", "HAS STARTED");
        return super.onStartCommand(intent, flags,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("TEST: GlobalModelService", "HAS DESTROYED");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return model;
    }
}
