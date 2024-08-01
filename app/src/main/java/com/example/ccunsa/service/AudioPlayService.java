package com.example.ccunsa.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.ccunsa.R;
import com.example.ccunsa.view.MainActivity;

import java.io.IOException;

public class AudioPlayService extends Service {
    private static final String CHANNEL_ID = "AudioPlayServiceChannel";
    public static final String FILENAME = "FILENAME";
    public static final String COMMAND = "COMMAND";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String RESUME = "RESUME";
    public static final String STOP = "STOP";

    private MediaPlayer mediaPlayer;
    private HandlerThread handlerThread;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        handlerThread = new HandlerThread("AudioPlayServiceHandler");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String filename = intent.getStringExtra(FILENAME);
        String command = intent.getStringExtra(COMMAND);

        handler.post(() -> {
            switch (command) {
                case PLAY:
                    startForegroundService();
                    playAudio(filename);
                    break;
                case PAUSE:
                    pauseAudio();
                    break;
                case RESUME:
                    resumeAudio();
                    break;
                case STOP:
                    stopAudio();
                    break;
            }
        });

        return START_STICKY;
    }

    private void playAudio(String filename) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            // Utiliza la ruta correcta para los archivos en assets
            mediaPlayer.setDataSource(getAssets().openFd(filename).getFileDescriptor(), getAssets().openFd(filename).getStartOffset(), getAssets().openFd(filename).getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("AudioPlayService", "Playing audio: " + filename);
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void resumeAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            stopForeground(true);
        }
    }

    private void startForegroundService() {
        createNotificationChannel();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Reproducción de Audio")
                .setContentText("Reproduciendo audio en segundo plano")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Audio Play Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handlerThread.quitSafely();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
