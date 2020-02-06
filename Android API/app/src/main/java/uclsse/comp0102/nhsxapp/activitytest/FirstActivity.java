package uclsse.comp0102.nhsxapp.activitytest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import uclsse.comp0102.nhsxapp.android.GlobalModel;
import uclsse.comp0102.nhsxapp.android.GlobalModelService;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    private GlobalModel model;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            model = (GlobalModel) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            model = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button startButton = findViewById(R.id.start_service_button);
        Button stopButton = findViewById(R.id.stop_service_button);
        Button downloadButton = findViewById(R.id.download_button);
        Button uploadButton = findViewById(R.id.upload_button);
        Button trainButton = findViewById(R.id.training_button);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        downloadButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        trainButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_service_button:
                Intent startIntent = new Intent(this, GlobalModelService.class);
                startService(startIntent);
                bindService(startIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.stop_service_button:
                unbindService(connection);
                Intent stopIntent = new Intent(this, GlobalModelService.class);
                stopService(stopIntent);
                break;
            case R.id.download_button:
                if(model == null) return;
                model.download();
                break;
            case R.id.upload_button:
                if(model == null) return;
                model.upload();
                break;
            case R.id.training_button:
                if(model == null) return;
                model.training();
                break;
            default:
                break;
        }
    }
}
