package com.example.audioclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clipserver.AudioInterface;

public class MainActivity extends AppCompatActivity {

    Button playButton, pauseButton, stopButton, resumeButton, startService, stopService;
    EditText editText;
    boolean isBound = false;
    private static AudioInterface audioInterface;
    int checkService = -1;
    final String TAG = "Audio Client";

//    int audio_position;
//    final static int PLAY_AUDIO = 0;
//    final static int PAUSE_AUDIO = 1;
//    final static int RESUME_AUDIO = 2;
//    final static int MUSIC_STOP=4;


    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            audioInterface = AudioInterface.Stub.asInterface(service);
            isBound = true;
            Log.i(TAG, "Your service has been connected.");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioInterface = null;
            isBound = false;
            Log.i(TAG, "Your service has been disconnected.");
        }
    };

    private void resolveInfo() {
        Intent intent = new Intent(AudioInterface.class.getName());
        Log.e("Client", intent.toString());

        ResolveInfo resolveInfo = getPackageManager().resolveService(intent, PackageManager.MATCH_ALL);
        intent.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
        startForegroundService(intent);
        boolean b = bindService(intent, this.serviceConnection, Context.BIND_AUTO_CREATE);

        if (b) {
            Log.i(TAG, "Ugo says bindService() succeeded!");
        } else {
            Log.i(TAG, "Ugo says bindService() failed!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.playButton);
        pauseButton = findViewById(R.id.pauseButton);
        stopButton = findViewById(R.id.stopButton);
        resumeButton = findViewById(R.id.resumeButton);
        startService = findViewById(R.id.startService);
        stopService = findViewById(R.id.stopService);
        editText = findViewById(R.id.editTextTextPersonName);

        defaultButton();

        startService.setOnClickListener(v -> {
            if (!isBound) {
//                boolean b;
//                Intent intent = new Intent(AudioInterface.class.getName());
//
//                ResolveInfo resolveInfo = getPackageManager().resolveService(intent, PackageManager.MATCH_ALL);
//                intent.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
//                startForegroundService(intent);
//                b = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                resolveInfo();
            }
            checkService = 0;
            enableButtons(checkService);
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    int audioPlay = Integer.parseInt(editText.getText().toString()) - 1;
                    if (audioPlay > -1 && audioPlay < 5) {
                        checkService = 1;
                        if (!isBound) {
                            resolveInfo();
                        } try {
                            audioInterface.playAudio(audioPlay);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        enableButtons(checkService);
                    } else {
                        Toast.makeText(MainActivity.this, "Enter a number", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Enter a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pauseButton.setOnClickListener(v -> {
            try {
                audioInterface.pauseAudio();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            checkService = 2;
            enableButtons(checkService);
        });

        resumeButton.setOnClickListener(v -> {
            try {
                audioInterface.onResume();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            checkService = 1;
            enableButtons(checkService);
        });

        stopButton.setOnClickListener(v -> {
            try {
                audioInterface.stopAudio();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (isBound) {
                unbindService(this.serviceConnection);
                isBound = false;
            }

            checkService = 0;
            enableButtons(checkService);
        });

        stopService.setOnClickListener(v -> {
            try {
                audioInterface.toStopService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (isBound) {
                unbindService(this.serviceConnection);
                isBound = false;
            }
            defaultButton();
        });
    }

    private void enableButtons(int checkService) {
        if (checkService == 0) {
            startService.setEnabled(false);
            startService.setClickable(false);

            stopService.setEnabled(true);
            stopService.setClickable(true);

            playButton.setEnabled(true);
            playButton.setClickable(true);

            pauseButton.setEnabled(false);
            pauseButton.setClickable(false);

            resumeButton.setEnabled(false);
            resumeButton.setClickable(false);

            stopButton.setEnabled(false);
            stopButton.setClickable(false);
        } else if (checkService == 1){
            pauseButton.setEnabled(true);
            pauseButton.setClickable(true);

            resumeButton.setEnabled(false);
            resumeButton.setClickable(false);

            stopButton.setEnabled(true);
            stopButton.setClickable(true);
        } else {
            pauseButton.setEnabled(false);
            pauseButton.setClickable(false);

            resumeButton.setEnabled(true);
            resumeButton.setClickable(true);
        }
    }

    public void defaultButton() {
        startService.setEnabled(true);
        startService.setClickable(true);

        stopService.setEnabled(false);
        stopService.setClickable(false);

        playButton.setEnabled(false);
        playButton.setClickable(false);

        pauseButton.setEnabled(false);
        pauseButton.setClickable(false);

        resumeButton.setEnabled(false);
        resumeButton.setClickable(false);

        stopButton.setEnabled(false);
        stopButton.setClickable(false);
    }
//    public void disableButtons(){
//        switch (audio_position){
//            case PLAY_AUDIO:{
//                playButton.setEnabled(false);
//                resumeButton.setEnabled(true);
//                stopButton.setEnabled(true);
//                //radioTrack.setEnabled(false);
////                disableRadioButton(audioRadio,false);
//                break;
//            }
//            case PAUSE_AUDIO:{
//                playButton.setEnabled(false);
//                resumeButton.setText("Resume");
//                stopButton.setEnabled(true);
//                //radioTrack.setEnabled(false);
////                disableRadioButton(audioRadio,false);
//                break;
//            }
//            case RESUME_AUDIO:{
//                playButton.setEnabled(false);
//                resumeButton.setText("Pause");
//                stopButton.setEnabled(true);
//                //radioTrack.setEnabled(false);
////                disableRadioButton(audioRadio,false);
//                break;
//            }
//            default:{
//                playButton.setEnabled(true);
//                resumeButton.setEnabled(false);
//                stopButton.setEnabled(false);
//                //radioTrack.setEnabled(true);
////                disableRadioButton(audioRadio,true);
//                break;
//            }
//        }
//    }
}