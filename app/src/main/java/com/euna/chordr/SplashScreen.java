package com.euna.chordr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * This Class handles everything pertaining to playing
 * sound files
 */

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        final Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent PageTransition = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(PageTransition);
                    finish();

                } catch (InterruptedException e) {
                    Intent PageTransition = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(PageTransition);
                    finish();
                }
            }
        };

        splashThread.start();
    }


}
