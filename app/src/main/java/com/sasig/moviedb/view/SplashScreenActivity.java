package com.sasig.moviedb.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sasig.moviedb.R;
import com.sasig.moviedb.controller.CallbackSplash;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar progressBar1;
    ProgressBar progressBar2;
    TextView textView;
    Animation rotateAnimation;
    ImageView imageView;
    //android:theme="@style/SplashScreenTheme" // put this just under (activity > SplashScreenActivity) on AndroidManifest to use the old splash_screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressBar1 = findViewById(R.id.progress_bar2);
        progressBar2 = findViewById(R.id.progress_bar1);
        textView = findViewById(R.id.splash_text);
        imageView = findViewById(R.id.splash_image);

        progressBar1.setMax(100);
        progressBar1.setScaleY(3f);
        progressBar1.setRotation(180);
        progressBar2.setMax(100);
        progressBar2.setScaleY(3f);
        imageView.setImageAlpha(-23);
        rotateAnimation();
        progressAnimation();
        /*Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        startActivity(intent);
        finish();*/
    }

    public void progressAnimation(){
        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar1, progressBar2, imageView, textView, 0f, 100f, new CallbackSplash() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(SplashScreenActivity.this,
                        MainActivity.class);
                // BETTER WITH THOSE 3 FLAGS AT MAX
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        anim.setDuration(1600);
        progressBar1.setAnimation(anim);
        progressBar2.setAnimation(anim);
    }

    private void rotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imageView.startAnimation(rotateAnimation);
    }
}
