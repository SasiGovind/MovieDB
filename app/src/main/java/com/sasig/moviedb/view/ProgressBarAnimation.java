package com.sasig.moviedb.view;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sasig.moviedb.controller.CallbackSplash;

public class ProgressBarAnimation extends Animation {
    private Context context;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private TextView textView;
    private ImageView imageView;
    private float from;
    private float to;
    private CallbackSplash callbackSplash;

    public ProgressBarAnimation(Context context, ProgressBar progressBar1, ProgressBar progressBar2, ImageView imageView, TextView textView, float from, float to, CallbackSplash callbackSplash) {
        this.context = context;
        this.progressBar1 = progressBar1;
        this.progressBar2 = progressBar2;
        this.imageView = imageView;
        this.textView = textView;
        this.from = from;
        this.to = to;
        this.callbackSplash = callbackSplash;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar1.setProgress((int)value);
        progressBar2.setProgress((int)value);
        textView.setText((int)value+" %");

        //imageView.setImageAlpha();
        if(value == to){
            callbackSplash.onSuccess();
            //context.startActivity(new Intent(context, MainActivity.class));
        }
    }
}
