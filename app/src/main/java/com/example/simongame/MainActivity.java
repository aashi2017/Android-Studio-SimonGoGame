package com.example.simongame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button btnGreen,btnYellow,btnRed,btnBlue,start;
    private static final String LEVEL = "level_info";
    public boolean mBlinking = false;
    private Animation mAnim;
    private static final String NAME = "userName";
    private TextView level,txt;
    private int levelNo = 0;
    private Button[] btnArray = new Button[4];
    //private MediaPlayer red, blue, green, yellow, wrong;
    ArrayList<Integer> gamePattern = new ArrayList<Integer>();
    ArrayList<Integer> userClickedPattern = new ArrayList <Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBlue = findViewById(R.id.btnBlue);
        btnGreen = findViewById(R.id.btnGreen);
        btnYellow = findViewById(R.id.btnYellow);
        btnRed = findViewById(R.id.btnRed);
        start = findViewById(R.id.start);
        level = findViewById(R.id.level);
        txt = findViewById(R.id.txt);

        btnYellow.setOnClickListener(this);
        btnBlue.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnArray[0] = btnYellow;
        btnArray[1] = btnGreen;
        btnArray[2] = btnRed;
        btnArray[3] = btnBlue;

        //Setting Default values
        SharedPreferences getSharedData = getSharedPreferences(LEVEL,MODE_PRIVATE);
        SharedPreferences getSharedData1 = getSharedPreferences(NAME,MODE_PRIVATE);
        int levelNo = getSharedData.getInt("lvl",0);
        String name = getSharedData1.getString("nm","Aashi");
        level.setText("Previous Level: "+levelNo);

        //Alert1
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final EditText input  = new EditText(this);
        builder.setMessage("Welcome: "+name)
              .setPositiveButton("New User", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      //AlertDialog with EditText
                      AlertDialog.Builder builderInput = new AlertDialog.Builder(MainActivity.this);

                      builderInput.setMessage("Enter Your Name")
                              .setView(input)
                              .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      SharedPreferences sharedPreferences1 = getSharedPreferences(NAME,MODE_PRIVATE);
                                      SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                      editor1.putString("nm",input.getText().toString());
                                      editor1.apply();
                                  }
                              });

                      AlertDialog alert1 = builderInput.create();
                      alert1.show();

                  }
              })
              .setNegativeButton("Continue", null) ;
            AlertDialog alert = builder.create();
            alert.show();

            //blinking Effect
       setBlinkingText(txt);





        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeBlinkingText(txt);
                start.setEnabled(false);
                randomTile();

            }
        });










    }

    protected void setBlinkingText(TextView txt) {
        mAnim = new AlphaAnimation(0.0f, 1.0f);
        mAnim.setDuration(100); // Time of the blink
        mAnim.setStartOffset(20);
        mAnim.setRepeatMode(Animation.REVERSE);
        mAnim.setRepeatCount(Animation.INFINITE);
        txt.startAnimation(mAnim);
    }
    protected void removeBlinkingText(TextView txt) {
        txt.clearAnimation();
    }


    public void randomTile(){
        final int random = new Random().nextInt(4);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                btnAnimate(random);
                playSound(random);
            }
        },500);
       gamePattern.add(random);
        userClickedPattern.clear();
    }
    private void checkAns(int val){
        if(gamePattern.get(val) == userClickedPattern.get(val)){
            if(gamePattern.size() == userClickedPattern.size()){
               levelNo++;
              level.setText("Level: "+levelNo);
                SharedPreferences sharedPreferences = getSharedPreferences(LEVEL,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("lvl",levelNo);
                editor.apply();
                randomTile();
            }
        }
        else{

            AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
            builder2.setMessage("Game OVER! ")
                    .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog gameOver = builder2.create();
            gameOver.show();
            setBlinkingText(txt);




            level.setText("Game Over!");
            levelNo = 0;
            gamePattern.clear();
            userClickedPattern.clear();
            start.setEnabled(true);




        }
    }
    private void btnAnimate(int random){
        AlphaAnimation animationG = new AlphaAnimation(0.2f, 1.0f);
        animationG.setDuration(500);
        btnArray[random].setAlpha(1.0f);
        btnArray[random].startAnimation(animationG);
    }
    private void playSound(int val){
        final MediaPlayer red = MediaPlayer.create(this, R.raw.red);
        final MediaPlayer green = MediaPlayer.create(this, R.raw.green);
        final MediaPlayer yellow = MediaPlayer.create(this, R.raw.yellow);
        final MediaPlayer blue = MediaPlayer.create(this, R.raw.blue);
        final MediaPlayer wrong = MediaPlayer.create(this, R.raw.wrong);

        switch (val){
            case 0:
                yellow.start();
                break;
            case 1:
                green.start();
                break;
            case 2:
                red.start();
                break;
            case 3:
                blue.start();
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBlue:
                final Animation animation = AnimationUtils.loadAnimation(this,R.anim.bounce);
                btnBlue.startAnimation( animation);
                playSound(3);
                btnAnimate(3);
                userClickedPattern.add(3);
                checkAns(userClickedPattern.size()-1);
                break;

            case R.id.btnGreen:
                final Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.bounce);
                btnGreen.startAnimation( animation1);
                playSound(1);
               // btnAnimate(1);
                userClickedPattern.add(1);
                checkAns(userClickedPattern.size()-1);
                break;
            case R.id.btnRed:
                final Animation animation2 = AnimationUtils.loadAnimation(this,R.anim.bounce);
                btnRed.startAnimation( animation2);
                playSound(2);
                //btnAnimate(2);
                userClickedPattern.add(2);
                checkAns(userClickedPattern.size()-1);
                break;
            case R.id.btnYellow:
                final Animation animation3 = AnimationUtils.loadAnimation(this,R.anim.bounce);
                btnYellow.startAnimation( animation3);
                playSound(0);
                //btnAnimate(0);
                userClickedPattern.add(0);
                checkAns(userClickedPattern.size()-1);
                break;
        }

    }
}
