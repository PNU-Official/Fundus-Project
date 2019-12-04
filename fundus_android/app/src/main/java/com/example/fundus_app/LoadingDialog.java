package com.example.fundus_app;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;

public class LoadingDialog extends Dialog {


    public LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명
        setContentView(R.layout.dialog_loading);


        LottieAnimationView lottie = (LottieAnimationView) findViewById(R.id.lottie);
        lottie.playAnimation();
        lottie.loop(true);



    }



}


