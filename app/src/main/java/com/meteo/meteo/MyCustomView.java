package com.meteo.meteo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by edouard on 10/11/16.
 */

public class MyCustomView extends View {

    Paint paint;


    //////////////////////////////////
    //                              //
    //          Constructor         //
    //                              //
    //////////////////////////////////
    public MyCustomView(Context context) {
        super(context);
        paint = new Paint();
    }

    public MyCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public MyCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int x = getWidth();
        int y = getHeight();
        int radius = min(x,y)/3;

        //White background
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

        //Red Circle
            paint.setColor(Color.RED);
            canvas.drawCircle(x / 2, y / 2, radius, paint);
    }

    private int min(int x, int y) {
        return ( ( x < y ) ? x : y);

    }

}
