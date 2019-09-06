package rayzem.com.animation_ball_roulette;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ImageView ball;
    BallAnimation anim;

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;

    private long mRotationTime = 0;
    private static final int ROTATION_WAIT_TIME_MS = 100;

    private LinearLayout container_logo, container_roulette;
    private TextView winnerSector;



    private BallAnimation infiniteAnim, stoppedAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        container_logo = findViewById(R.id.container_logo);
        container_roulette = findViewById(R.id.container_roulette);
        ball = findViewById(R.id.ball);
        winnerSector = findViewById(R.id.winnerSector);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);





    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

        Toast.makeText(this, "Spin your phone", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    private void initInfiniteAnim(){
        infiniteAnim = new BallAnimation(ball, 340, true);
        infiniteAnim.setDuration(2000);
        infiniteAnim.setFillAfter(true);

        //check this
        infiniteAnim.setRepeatCount(Animation.INFINITE);
        infiniteAnim.setInterpolator(new LinearInterpolator());

        infiniteAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //ball.clearAnimation();
                infiniteAnim = null;
                Log.i("OSCAR", "INIFNOTO STOP!!!");
                initStoppedAnimation();


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i("OSCAR", "REPEAT...");
            }
        });

        ball.startAnimation(infiniteAnim);

    }


    public void initStoppedAnimation(){


        stoppedAnim = new BallAnimation(ball, 340, false);
        stoppedAnim.setDuration(3000);
        stoppedAnim.setFillAfter(true);


        stoppedAnim.setInterpolator(new DecelerateInterpolator());
        stoppedAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                winnerSector.setText("Winner: "+ stoppedAnim.getSectorWheelResult().toString());
                //ball.clearAnimation();

                /*container_roulette.setVisibility(View.GONE);
                container_logo.setVisibility(View.VISIBLE);*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ball.startAnimation(stoppedAnim);

    }

    private void rouletteBallRotate(){
        container_logo.setVisibility(View.GONE);
        container_roulette.setVisibility(View.VISIBLE);

        anim = new BallAnimation(ball, 340, false);
        anim.setDuration(2000);
        anim.setFillAfter(true);

        anim.setInterpolator(new DecelerateInterpolator());

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                winnerSector.setText("Winner: "+anim.getSectorWheelResult().toString());

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        ball.startAnimation(anim);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mRotationTime) > ROTATION_WAIT_TIME_MS){
            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){

                if(Math.abs(event.values[2]) > 3){
                    winnerSector.setText("");
                    container_roulette.setVisibility(View.GONE);
                    container_logo.setVisibility(View.VISIBLE);


                }else if(Math.abs(event.values[2])>= 1 && Math.abs(event.values[2]) <=2){
                    container_roulette.setVisibility(View.VISIBLE);
                    container_logo.setVisibility(View.GONE);
                    winnerSector.setText("");


                    if(infiniteAnim == null){
                        initInfiniteAnim();
                    }


                }else if(Math.abs(event.values[2])<= 0.4){
                    //1st when is quiet
                    if(infiniteAnim != null){
                        //infiniteAnim.setRepeatCount(0);

                        //CHECK THIS LINES
                        infiniteAnim.cancel();


                        //ball.clearAnimation();
                        //ball.getAnimation().cancel();
                        //initStoppedAnimation();
                    }


                }
            }
        }





    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
