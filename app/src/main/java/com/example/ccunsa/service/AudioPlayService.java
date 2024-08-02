package com.example.ccunsa.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    private String currentFilename;

    private BroadcastReceiver notificationReceiver;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        handlerThread = new HandlerThread("AudioPlayServiceHandler");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        // Register BroadcastReceiver to handle notification actions
        notificationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                Log.d("AudioPlayService", "Received command: " + command); // Log para verificar la recepción del comando
                handleCommand(command);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(PLAY);
        filter.addAction(PAUSE);
        filter.addAction(RESUME);
        filter.addAction(STOP);
        registerReceiver(notificationReceiver, filter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            Log.e("AudioPlayService", "Intent is null in onStartCommand");
            stopSelf();
            return START_NOT_STICKY;
        }

        String filename = intent.getStringExtra(FILENAME);
        String command = intent.getStringExtra(COMMAND);

        if (command == null) {
            Log.e("AudioPlayService", "Command is null in onStartCommand");
            stopSelf();
            return START_NOT_STICKY;
        }

        currentFilename = filename;
        handler.post(() -> handleCommand(command));

        return START_STICKY;
    }

    private void handleCommand(String command) {
        switch (command) {
            case PLAY:
                startForegroundService();
                playAudio(currentFilename);
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
    }

    private void playAudio(String filename) {
        if (filename == null || filename.isEmpty()) {
            Log.e("AudioPlayService", "Filename is null or empty");
            stopSelf();
            return;
        }

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            String assetFileName = filename.endsWith(".mp3") ? filename : filename + ".mp3";
            String assetPath = "2/" + assetFileName;
            AssetFileDescriptor afd = getAssets().openFd(assetPath);

            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("AudioPlayService", "Error loading audio file", e);
        }

        Log.d("AudioPlayService", "Playing audio: " + filename);
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d("AudioPlayService", "Audio paused");
        }
    }

    private void resumeAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d("AudioPlayService", "Audio resumed");
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            stopForeground(true);
            Log.d("AudioPlayService", "Audio stopped");
        }
    }

    private void startForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent playIntent = new Intent(this, AudioPlayService.class);
        playIntent.setAction(PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 1, playIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, AudioPlayService.class);
        pauseIntent.setAction(PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 2, pauseIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent resumeIntent = new Intent(this, AudioPlayService.class);
        resumeIntent.setAction(RESUME);
        PendingIntent resumePendingIntent = PendingIntent.getBroadcast(this, 3, resumeIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent stopIntent = new Intent(this, AudioPlayService.class);
        stopIntent.setAction(STOP);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 4, stopIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Reproducción de Audio")
                .setContentText("Reproduciendo audio en segundo plano")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.play_button, "Play", playPendingIntent)
                .addAction(R.drawable.pause_button, "Pause", pausePendingIntent)
                .addAction(R.drawable.resume_button, "Resume", resumePendingIntent)
                .addAction(R.drawable.stop_button, "Stop", stopPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2, 3))
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
        unregisterReceiver(notificationReceiver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
