package com.example.clipserver;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ClipServerService extends Service {

    ArrayList <Integer> audioArray = new ArrayList<>(Arrays.asList(R.raw.audio1, R.raw.audio2, R.raw.audio3, R.raw.audio4, R.raw.audio5));
//    private final static int[] audioArray = {R.raw.audio1, R.raw.audio2, R.raw.audio3, R.raw.audio4, R.raw.audio5, R.raw.audio6};
    public static final String CHANNEL_ID = "Audio service Channel";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ClipServerServices", "Service was created!") ;

        Intent intentNotification = new Intent(getApplicationContext(), ClipServerService.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNotification, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID).setContentTitle("Playing music")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true)
                .setContentText("Music player")
                .setTicker("Playing music")
                .build();
        createNotificationChannel();
        startForeground(1, notification);
//        Intent i = new Intent(AudioPlayerService.class.getName());
//        ResolveInfo info = getPackageManager().resolveService(i, PackageManager.MATCH_ALL);
//        i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
//        createNotificationChannel();
//        startForegroundService(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID, "Audio player notification", NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel.setDescription("Player Notification");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stubBinder;
    }

    public void onDestroy() {
        super.onDestroy();
    }


    private final AudioInterface.Stub stubBinder = new AudioInterface.Stub() {

        public void onStartService() throws RemoteException {
        }

        public void playAudio(int position) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
//            mediaPlayer = MediaPlayer.create(getApplicationContext(), audioArray[audioChoice]);
            mediaPlayer = MediaPlayer.create(getApplicationContext(), audioArray.get(position));

            mediaPlayer.start();
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
//                        onStop();
                    }
                });
            }
        }
           //Attempt 1
//        private final AudioInterface.Stub stubBinder = new AudioInterface.Stub() {
//
//            public void onStartService() throws RemoteException{}
//
//            public void playAudio(int audioChoice){
//                if (mediaPlayer == null) {
//                    mediaPlayer = MediaPlayer.create(getApplicationContext(), audioArray[audioChoice]);
//                }
//
//                if (mediaPlayer != null) {
//                    mediaPlayer.setLooping(false);
//
//                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
////                        mediaPlayer.stop();
////                        mediaPlayer.release();
////                        mediaPlayer = null;
//                            onStop();
//                        }
//                    });
//                }
//                mediaPlayer.start();
//            }

        public void pauseAudio() throws RemoteException {
            mediaPlayer.pause();
        }

        public void stopAudio() throws RemoteException {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
//                onStop();
        }

//        public void pauseAudio() throws RemoteException{
//            if (mediaPlayer != null) {
//                mediaPlayer.pause();
//            }
//        }
//
//        public void stopAudio() throws RemoteException{
//            if (mediaPlayer != null) {
//                onStop();
//            }
//        }


        public void onResume() throws RemoteException {
            mediaPlayer.start();
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(false);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
//                         stop();
                    }
                });
            }
        }

//        public void onResume() throws RemoteException {
//            mediaPlayer.start();
//            if(mediaPlayer != null){
//                mediaPlayer.setLooping(false);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        onStop();
//                    }
//                });
//            }
//        }
//        public void onStop() {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }

        public void toStopService() throws RemoteException {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = null;
            stopSelf();
        }

        public int trackStatus() throws RemoteException {
            if (mediaPlayer == null) {
                return 0;
            } else if (mediaPlayer.isPlaying()) {
                return 1;
            } else {
                return 2;
            }
        }
    };



}