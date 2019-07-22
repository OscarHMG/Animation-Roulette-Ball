package rayzem.com.animation_ball_roulette;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.Random;

public class BallAnimation extends Animation {

    private static final Sector[] sectorsWheel = {
            new Sector(0, ""),
            new Sector(32, "red"), new Sector(15, "black"),
            new Sector(19, "red"), new Sector(4, "black"),
            new Sector(21,"red") , new Sector(2,"black"),
            new Sector(25, "red"), new Sector(17, "black"),
            new Sector(34, "red"), new Sector(6, "black"),
            new Sector(27, "red"), new Sector(13, "black"),
            new Sector(36, "red"), new Sector(11, "black"),
            new Sector(30, "red"), new Sector(8, "black"),
            new Sector(23, "red"), new Sector(10, "black"),
            new Sector(5, "red") , new Sector(24, "black"),
            new Sector(16, "red"), new Sector(33, "black"),
            new Sector(1, "red") , new Sector(20, "black"),
            new Sector(14, "red"), new Sector(31, "black"),
            new Sector(9, "red") , new Sector(22, "black"),
            new Sector(18, "red"), new Sector(29, "black"),
            new Sector(7, "red") , new Sector(28, "black"),
            new Sector(12, "red"), new Sector(35, "black"),
            new Sector(3, "red"),  new Sector(26, "black"),



    };



    private View view;
    private float centerX, centerY; // center x,y position of circular path
    private float prevX, prevY;     // previous x,y position of image during animation
    private float radius;           // radius of circle
    private float prevDx, prevDy;
    private float degree;
    int index;                      //Index of the sector randomly generated

    private boolean isInfiniteAnim;

    private static final float HALF_SECTOR = 360f/ 37f / 2f;


    public BallAnimation(View view, float radius, boolean isInfiniteAnim){
        this.view = view;
        this.radius = radius;
        this.isInfiniteAnim = isInfiniteAnim;
        //If the animation when ball stop
        if(!isInfiniteAnim) {
            index = (new Random().nextInt(38));
            degree = (HALF_SECTOR * 2) * index;
        }else{
            index = 0;
            degree = 360f;
        }

    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        // calculate position of image center
        int cxImage = width / 2;
        int cyImage = height / 2;
        centerX = view.getLeft() + cxImage;
        centerY = view.getTop() + cyImage;

        // set previous position to center
        prevX = centerX;
        prevY = centerY;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if(interpolatedTime == 0){
            t.getMatrix().setTranslate(prevDx, prevDy);
            return;
        }



        //float angleDeg = (interpolatedTime * 360f + 90) % 360;

        Log.i("DEGREE", ""+degree);

        //float angleDeg = (interpolatedTime  * 360f + 90) % 360;

        //Trick to make the ball start at the top.
        float angleDeg = (interpolatedTime  * degree + 90) % 360;


        //float angleDeg = (interpolatedTime  * degree );

        float angleRad = (float) Math.toRadians(angleDeg);

        // r = radius, centerX and centerY = center point, a = angle (radians)
        float x = (float) (centerX + radius * Math.cos(angleRad));
        float y = (float) (centerY + radius * Math.sin(angleRad));


        float dx = prevX - x;
        float dy = prevY - y;

        /*prevX = x;
        prevY = y;*/

        prevDx = dx;
        prevDy = dy;


        t.getMatrix().setTranslate(dx, dy);

        


    }

    public static Sector[] getSectorsWheel() {
        return sectorsWheel;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;

    }

    public Sector getSectorWheelResult(){
        return sectorsWheel[index];
    }


}
