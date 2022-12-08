package com.example.sistemagestual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PopUp extends AppCompatActivity {
    private ArrayList<PointF> touchPoints = null;
    private Intent intent;
    private boolean keep;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Etsiit's Map");
        setContentView(R.layout.activity_pop_up);
        mImageView = findViewById(R.id.plano);

        intent = this.getIntent();
        boolean keep = intent.getExtras().getBoolean("keep");
        if(keep)
        {
            touchPoints = new ArrayList<>();
            DisplayMetrics medidasVentana = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);
            int ancho  = medidasVentana.widthPixels;
            int alto = medidasVentana.heightPixels;
            getWindow().setLayout((int) (ancho * 0.9), (int) (alto * 0.4));

            if(GlobalData.getLevel().equals(1)) {
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.baja));
            }
            else if(GlobalData.getLevel().equals(2)) {
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.primera));
            }
            else if (GlobalData.getLevel().equals(3)){
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.segunda));
            }
            else if (GlobalData.getLevel().equals(-1)){
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.sotano));
            }
            else if (GlobalData.getLevel().equals(0)){
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.completa));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        keep = intent.getExtras().getBoolean("keep");
        if(!keep)
        {
            this.finish();
        }
    }
}