package com.example.ccunsa.view;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ccunsa.R;

public class STARActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_staractivity);

        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        TextView letraText = findViewById(R.id.letras);
        ImageView imgView = findViewById(R.id.logo);

        letraText.setAnimation(animacion2);
        imgView.setAnimation(animacion1);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run (){
                Intent intent = new Intent(STARActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}








































