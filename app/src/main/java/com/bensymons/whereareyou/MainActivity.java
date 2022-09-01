package com.bensymons.whereareyou;

import static android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private AudioManager audioManager;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                int ringerMode = audioManager.getRingerMode();
                String modeString = "";
                if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                    modeString = "Vibrate";
                } else if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
                    modeString = "Ringer";
                } else if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
                    modeString = "Silent";
                }
                TextView tv = findViewById(R.id.mode_textView);
                tv.setText(modeString);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(receiver, filter);

        Button vibrate = findViewById(R.id.vibrate_btn);
        Button ring = findViewById(R.id.ring_btn);
        Button mode = findViewById(R.id.mode_btn);
        Button silent = findViewById(R.id.silent_btn);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        vibrate.setOnClickListener(view -> {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            Toast.makeText(MainActivity.this, "Now in Vibrate Mode", Toast.LENGTH_LONG).show();
        });

        ring.setOnClickListener(view -> {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(MainActivity.this, "Now in Ringing Mode", Toast.LENGTH_LONG).show();
        });

        silent.setOnClickListener(view -> {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Toast.makeText(MainActivity.this, "Now in Silent Mode", Toast.LENGTH_LONG).show();
        });

        mode.setOnClickListener(v -> {
            int ringerMode = audioManager.getRingerMode();
            if (ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                Toast.makeText(MainActivity.this, "Now in Vibrate Mode",
                        Toast.LENGTH_LONG).show();
            } else if (ringerMode == AudioManager.RINGER_MODE_NORMAL) {
                Toast.makeText(MainActivity.this, "Now in Ringing Mode",
                        Toast.LENGTH_LONG).show();
            } else if (ringerMode == AudioManager.RINGER_MODE_SILENT) {
                Toast.makeText(MainActivity.this, "Now in Silent Mode",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}