package co.edu.udea.compumovil.gr08_20171.lab2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class Splash extends AppCompatActivity {
    private static final int segundos=4;
    private static final int mili=segundos*1000;
    private static final int delay=2;
    private ProgressBar barProgreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        barProgreso=(ProgressBar)findViewById(R.id.progressBar);
        barProgreso.setMax(maxProgress());
        barProgreso.setProgressTintList(ColorStateList.valueOf(Color.CYAN));
        beginAnimation();
    }

    public void beginAnimation(){
        new CountDownTimer(mili,segundos){

            @Override
            public void onTick(long millisUntilFinished) {
                barProgreso.setProgress(ordenProgreso(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(Splash.this,Inicio.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    public int maxProgress(){
        return segundos-delay;
    }

    public int ordenProgreso(long miliseconds){
       return (int)((mili-miliseconds)/1000);
    }
}
