package com.example.lr2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;


import java.io.IOException;


public class MyService extends Service
{

    MyBinder binder = new MyBinder();

    private CountDownTimer countDownTimer;

    private long timeLeftInMills;
    private int currentStage;
    private int prepTime;
    private int workTime;
    private int freeTime;
    private int setNum;
    private int freeOfSet;
    private int stagesCount;
    private int color;
    private int currentSet;

    private boolean isTimerRunning;

    MediaPlayer mPlayer;

    public void onCreate()
    {
        super.onCreate();

        mPlayer = MediaPlayer.create(this, R.raw.sound);

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mPlayer.stop();
                try
                {
                    mPlayer.prepare();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                mPlayer.seekTo(0);
            }

        });
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        timeLeftInMills = intent.getLongExtra("timeLeftInMills", 0);
        currentStage = intent.getIntExtra("currentStage", 0);
        prepTime = intent.getIntExtra("prepTime", 0);
        workTime = intent.getIntExtra("workTime", 0);
        freeTime = intent.getIntExtra("freeTime", 0);
        setNum = intent.getIntExtra("setNum", 0);
        freeOfSet = intent.getIntExtra("freeOfSet", 0);
        currentSet = intent.getIntExtra("currentSet", 0);

        isTimerRunning = intent.getBooleanExtra("isTimerRunning", false);
        stagesCount = intent.getIntExtra("stagesCount", 0);
        color = intent.getIntExtra("color", 0);

        if (isTimerRunning)
            StartTimer();

        return super.onStartCommand(intent, flags, startId);
    }


    public IBinder onBind(Intent arg0)
    {
        return binder;
    }

    public boolean onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }

    public void onDestroy()
    {
        super.onDestroy();

        if (countDownTimer != null)
            countDownTimer.cancel();
    }


    public void StartTimer()
    {

        countDownTimer = new CountDownTimer(timeLeftInMills, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timeLeftInMills = millisUntilFinished;
            }

            @Override
            public void onFinish()
            {
                mPlayer.start();
                currentStage++;
                if (currentStage >= stagesCount)
                {
                    countDownTimer.cancel();
                }
                else
                {
                    SetData();
                    StartTimer();
                }
            }
        }.start();
    }

    public int GetCurrentStage()
    {
        return currentStage;
    }

    public long GetTImeLeftInMills()
    {
        return timeLeftInMills;
    }

    public boolean GetIsTimerRunning()
    {
        return isTimerRunning;
    }

    public int GetColor()
    {
        return color;
    }

    public int GetCurrentSet()
    {
        return currentSet;
    }

    class MyBinder extends Binder
    {
        MyService getService()
        {
            return MyService.this;
        }
    }

    public void SetData()
    {
        if (currentStage == 0)
        {
            timeLeftInMills = prepTime;
        }
        else if (currentStage >= stagesCount)
        {
            timeLeftInMills = 0;
        }
        else
        {
            int temp = currentStage;

            for (int i = 0; i < setNum; i++)
            {
                if (temp == stagesCount - 1)
                {
                    timeLeftInMills = freeOfSet;
                    currentSet++;
                    return;
                }

                temp += currentStage;
            }

            if (currentSet % 2 == 0)
            {
                if (currentStage % 2 != 0)
                {
                    timeLeftInMills = workTime;
                }
                else
                {
                    timeLeftInMills = freeTime;
                }
            }
            else
            {
                if (currentStage % 2 == 0)
                {
                    timeLeftInMills = workTime;
                }
                else
                {
                    timeLeftInMills = freeTime;
                }
            }
        }
    }
}