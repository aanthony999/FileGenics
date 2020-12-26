package com.example.filetransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView bgapp, clover;
    TextView text_splash;
    Button button_splash;
    Animation frombottom, fromtop, bganim, clovernim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        bgapp = (ImageView) findViewById(R.id.bgapp);
        text_splash = (TextView) findViewById(R.id.text_splash);
        clover = (ImageView) findViewById(R.id.clover);
        button_splash = (Button) findViewById(R.id.button_splash);

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this,R.anim.fromtop);
        bganim = AnimationUtils.loadAnimation(this,R.anim.bganim);
        clovernim = AnimationUtils.loadAnimation(this,R.anim.clovernim);

        button_splash.setAnimation(frombottom);
        text_splash.setAnimation(fromtop);
        bgapp.setAnimation(bganim);
        clover.setAnimation(clovernim);

        button_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMain();
            }
        });


    }

    public void openMain(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}
