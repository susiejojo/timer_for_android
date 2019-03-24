package com.example.stopwatch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.app.NotificationManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    TextView timer;
    SeekBar timersetter;
    Button btn;
    Button btn2;
    Boolean counterisactive = false;
    CountDownTimer countdowntimer;
    Vibrator v;
    AudioManager audioManager;
    Intent activityIntent;
    long[] pattern={0,1000,1000};
    private final String CHANNEL_ID= "personal notifications";

    MediaPlayer mp;
    int i=0;

    public void snooze(View view)
    {
        btn2= (Button) findViewById(R.id.button3);
        mp.stop();
        v.cancel();
        i=0;
        countdowntimer = new CountDownTimer(120 * 1000 + 10, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                UpdateTimer((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                Log.i("Finished!", "All done!");
                btn.setEnabled(true);
                mp = MediaPlayer.create(getApplicationContext(), R.raw.loud);
                mp.start();
                mp.setLooping(true);
                v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(pattern,0);
                timer.setText(("00 : 00"));
                i=1;
                btn2.setEnabled(true);
                audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);

                notifyme();
            }
        }.start();
        btn2.setEnabled(false);
    }

    public void resetTimer()
    {
        timer.setText("00 : 30");
        timersetter.setProgress(30);
        timersetter.setEnabled(true);
        countdowntimer.cancel();
        btn.setText("GO!");
        counterisactive=false;
        i=0;
    }
    public void resetTimerFinish()
    {
        timer.setText("00 : 30");
        timersetter.setProgress(30);
        timersetter.setEnabled(true);
        countdowntimer.cancel();
        btn2.setEnabled(false);
        btn.setText("GO!");
        counterisactive=false;
        mp.stop();
        v.cancel();
        i=0;
    }
    public boolean onKeyDown(int keycode, KeyEvent event)
    {
        if (keycode==KeyEvent.KEYCODE_VOLUME_DOWN || keycode== KeyEvent.KEYCODE_VOLUME_UP)
        {if(i==1){
            audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
            return true;
        }
        else{
            return super.onKeyDown(keycode,event);
        }
        }
        else {
            return super.onKeyDown(keycode,event);

        }
    }
    public void OnclickFunc(View view)
    {
        if (counterisactive)
        {
            if(i==1)
            {resetTimerFinish();}
            else
            {resetTimer();}
        }
        else {


            counterisactive = true;
            timersetter.setEnabled(false);
            btn = (Button) findViewById(R.id.button2);
            btn2= (Button) findViewById(R.id.button3);
            btn.setText("STOP!");
            countdowntimer = new CountDownTimer(timersetter.getProgress() * 1000 + 10, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    UpdateTimer((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    Log.i("Finished!", "All done!");

                    mp = MediaPlayer.create(getApplicationContext(), R.raw.loud);
                    mp.start();
                    mp.setLooping(true);
                    v=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(pattern,0);
                    timer.setText(("00 : 00"));
                    i=1;
                    btn2.setEnabled(true);
                    audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),AudioManager.FLAG_SHOW_UI);
                    notifyme();

                }
            }.start();
        }
    }
    public void notifyme(){
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this,CHANNEL_ID).setSmallIcon(R.drawable.timer).setContentTitle("Timer").setContentText("Time Up!").setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(001,notif.build());
        activityIntent = new Intent(this, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(activityIntent);
 }
    public void UpdateTimer(int timeleft)
    {
        int minutes=timeleft/60;
        int seconds=timeleft-(60*minutes);
        String firststring = Integer.toString(minutes);
        String secondstring = Integer.toString(seconds);
        if (firststring.length()==1)
            firststring="0"+firststring;
        if (secondstring.length()==1)
            secondstring="0"+secondstring;
        if (secondstring.equals("0")) {
            secondstring = "00";
        }
        timer.setText(firststring+" : "+secondstring);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        timersetter = findViewById(R.id.myseekBar);
        timer= findViewById(R.id.stopwatch);
        timersetter.setMax(18000);
        timersetter.setProgress(30);
        timersetter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                UpdateTimer(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
