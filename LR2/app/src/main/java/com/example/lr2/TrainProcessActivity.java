package com.example.lr2;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lr2.Adapters.StagesAdapter;
import com.example.lr2.Models.Train;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class TrainProcessActivity extends AppCompatActivity
{
    Train trainItem;
    ArrayList<String> arrayList;
    StagesAdapter stagesAdapter;
    ConstraintLayout mainSpace;
    MediaPlayer mPlayer;
    MyService myService;

    private TextView time;
    private TextView stageText;
    private ListView lv;
    private Button buttonPlayPause;
    private CountDownTimer countDownTimer;
    private ServiceConnection sConn;
    private Intent intent;

    private boolean isTimerRunning = false;
    private boolean bound = false;
    private boolean needService;

    private long timeLeftInMills;

    private int prepTime;
    private int workTime;
    private int freeTime;
    private int cycleNum;
    private int setNum;
    private int freeOfSet;
    private int currentStage = 0;
    private int currentSet = 0;
    private int color;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainprocess);
        getSupportActionBar().setTitle(getString(R.string.trainProcessActionBar));

        lv = (ListView) findViewById(R.id.stagesList);

        Bundle extras = getIntent().getExtras();
        trainItem = extras.getParcelable("item");
        intent = new Intent(this, MyService.class);

        prepTime = trainItem.getPrepTime() * 1000;
        workTime = trainItem.getWorkTime() * 1000;
        freeTime = trainItem.getFreeTime() * 1000;
        cycleNum = trainItem.getCycleNum();
        setNum = trainItem.getSetNum();
        freeOfSet = trainItem.getFreeOfSet() * 1000;

        needService = true;

        ShowStagesInListView();

        buttonPlayPause = findViewById(R.id.playPause);
        time = findViewById(R.id.time);
        stageText = findViewById(R.id.currentStage);
        mainSpace = findViewById(R.id.mainSpace);
        mPlayer = MediaPlayer.create(this, R.raw.sound);

        sConn = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder)
            {
                myService = ((MyService.MyBinder) binder).getService();
                bound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                bound = false;
            }
        };

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

        SetData();

        buttonPlayPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isTimerRunning)
                {
                    PauseTimer();
                }
                else
                {
                    StartTimer();
                }
            }
        });
        UpdateTime();
    }

    public void StartTimer()
    {

        countDownTimer = new CountDownTimer(timeLeftInMills, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timeLeftInMills = millisUntilFinished;
                UpdateTime();
            }

            @Override
            public void onFinish()
            {
                mPlayer.start();
                currentStage++;
                if (currentStage >= stagesAdapter.getCount())
                {
                    isTimerRunning = false;
                    buttonPlayPause.setText(getString(R.string.finishTrain));
                    time.setText("00:00");
                    stageText.setText(getString(R.string.finishTrain));
                }
                else
                    {
                        SetData();
                        StartTimer();
                    }
            }
        }.start();

        isTimerRunning = true;
        buttonPlayPause.setText(getString(R.string.pauseTrain));
    }

    private void UpdateTime()
    {
        int minutes = (int) (timeLeftInMills / 1000) / 60;
        int seconds = (int) (timeLeftInMills / 1000) % 60;
        String timeLeftFormat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        time.setText(timeLeftFormat);
    }

    public void PauseTimer()
    {
        isTimerRunning = false;
        countDownTimer.cancel();
        buttonPlayPause.setText(getString(R.string.continueTrain));
    }

    public void ShowStagesInListView()
    {
        arrayList = new ArrayList<>();
        arrayList.add(getString(R.string.prepTime) + ": " + prepTime / 1000);

        for (int i = 0; i < setNum; i++)
        {
            for (int j = 0; j < cycleNum; j++)
            {
                arrayList.add(getString(R.string.workTime) + ": " + workTime / 1000);
                arrayList.add(getString(R.string.freeTime) + ": " + freeTime / 1000);
            }

            arrayList.add(getString(R.string.freeOfSet) + ": " + freeOfSet / 1000);
        }

        stagesAdapter = new StagesAdapter(this, arrayList);
        lv.setAdapter(stagesAdapter);
        stagesAdapter.notifyDataSetChanged();
    }

    public void GenerateColor()
    {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        color = Color.rgb(r, g, b);
    }

    public void SetData()
    {
        if (currentStage == 0)
        {
            stageText.setText(getString(R.string.prepTime));
            GenerateColor();
            mainSpace.setBackgroundColor(color);
            timeLeftInMills = prepTime;
            UpdateTime();
        }
        else if (currentStage >= stagesAdapter.getCount())
        {
            timeLeftInMills = 0;
            UpdateTime();
        }
        else
        {
            int temp = currentStage;

            for (int i = 0; i < setNum; i++)
            {
                if (temp == stagesAdapter.getCount() - 1)
                {
                    stageText.setText(getString(R.string.freeOfSet));
                    GenerateColor();
                    mainSpace.setBackgroundColor(color);
                    timeLeftInMills = freeOfSet;
                    UpdateTime();
                    currentSet++;
                    return;
                }

                temp += currentStage;
            }

            if (currentSet % 2 == 0)
            {
                if (currentStage % 2 != 0)
                {
                    stageText.setText(getString(R.string.workTime));
                    GenerateColor();
                    mainSpace.setBackgroundColor(color);
                    timeLeftInMills = workTime;
                }
                else
                {
                    stageText.setText(getString(R.string.freeTime));
                    GenerateColor();
                    mainSpace.setBackgroundColor(color);
                    timeLeftInMills = freeTime;
                }
            }
            else
            {
                if (currentStage % 2 == 0)
                {
                    stageText.setText(getString(R.string.workTime));
                    GenerateColor();
                    mainSpace.setBackgroundColor(color);
                    timeLeftInMills = workTime;
                }
                else
                {
                    stageText.setText(getString(R.string.freeTime));
                    GenerateColor();
                    mainSpace.setBackgroundColor(color);
                    timeLeftInMills = freeTime;
                }
            }
            UpdateTime();
        }
    }

    public void NextStageClick(View view)
    {
        currentStage++;
        SetData();
        buttonPlayPause.callOnClick();
        buttonPlayPause.callOnClick();
    }

    public void PrevStageClick(View view)
    {
        if (currentStage != 0)
        {
            currentStage--;
            SetData();
            buttonPlayPause.callOnClick();
            buttonPlayPause.callOnClick();
        }
    }

    public void DestroyTimer()
    {
        isTimerRunning = false;

        if (countDownTimer != null)
            countDownTimer.cancel();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putLong("millsLeft", timeLeftInMills);
        outState.putBoolean("isTimerRunning", isTimerRunning);
        outState.putInt("color", color);
        outState.putInt("currentStage", currentStage);
        outState.putCharSequence("stageText", stageText.getText());
        outState.putInt("currentSet", currentSet);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        isTimerRunning = savedInstanceState.getBoolean("isTimerRunning");
        currentStage = savedInstanceState.getInt("currentStage");
        mainSpace.setBackgroundColor(savedInstanceState.getInt("color"));
        timeLeftInMills = savedInstanceState.getLong("millsLeft");
        stageText.setText(savedInstanceState.getCharSequence("stageText"));
        currentSet = savedInstanceState.getInt("currentSet");
        UpdateTime();

        if (isTimerRunning)
        {
            StartTimer();
        }
        else
        {
            buttonPlayPause.setText(getString(R.string.continueTrain));
        }

    }

    @Override
    public void onBackPressed()
    {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.trainExitTitle))
                .setMessage(getString(R.string.trainExitMessage))
                .setPositiveButton(getString(R.string.trainExitPositive), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        DestroyTimer();
                        dialog.dismiss();
                        needService = false;
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.trainExitNegative), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (needService && (!bound) && (isTimerRunning))
        {
            intent.putExtra("isTimerRunning", true);

            if (countDownTimer != null)
                countDownTimer.cancel();

            intent.putExtra("prepTime", prepTime);
            intent.putExtra("workTime", workTime);
            intent.putExtra("freeTime", freeTime);
            intent.putExtra("cycleNum", cycleNum);
            intent.putExtra("setNum", setNum);
            intent.putExtra("freeOfSet", freeOfSet);
            intent.putExtra("currentSet", currentSet);

            intent.putExtra("stagesCount", stagesAdapter.getCount());
            intent.putExtra("currentStage", currentStage);
            intent.putExtra("timeLeftInMills", timeLeftInMills);
            intent.putExtra("color", color);

            startService(intent);
            bindService(intent, sConn, BIND_AUTO_CREATE);
            bound = true;
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (bound)
        {
            currentStage = myService.GetCurrentStage();
            isTimerRunning = myService.GetIsTimerRunning();

            SetData();

            currentSet = myService.GetCurrentSet();
            mainSpace.setBackgroundColor(myService.GetColor());
            timeLeftInMills = myService.GetTImeLeftInMills();
            UpdateTime();

            unbindService(sConn);
            stopService(intent);
            bound = false;

            if (isTimerRunning)
                StartTimer();
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (countDownTimer != null)
            countDownTimer.cancel();

        if (bound)
        {
            unbindService(sConn);
            stopService(intent);
            bound = false;
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }
}

