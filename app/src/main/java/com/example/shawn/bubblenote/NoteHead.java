package com.example.shawn.bubblenote;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.ViewConfiguration;
import android.view.WindowManager.LayoutParams;


public class NoteHead extends Service{
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;


    public void onCreate() {
        super.onCreate();
        floatingFaceBubble = new ImageView(this);
        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.mipmap.ic_launcher);
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final LayoutParams myParams = new WindowManager.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        // add a bubble icon in window
        windowManager.addView(floatingFaceBubble, myParams);
        try{
            //for moving the picture on touch and slide
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    if(System.currentTimeMillis()-touchStartTime>ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
                        windowManager.removeView(floatingFaceBubble);
                        stopSelf();
                        return false;
                    }
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        floatingFaceBubble.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent slut = new Intent(NoteHead.this,Head.class);
                slut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // Starting an Activity outside of the Activity
                startActivity(slut);
            }
        });
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
