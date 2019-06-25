package net.nitorac.landscapeeditor.colorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by Nitorac.
 */
public class ColorView extends View {

    public static final HashMap<String, Integer> colors = new HashMap<String, Integer>(){{
        put("Ciel", Color.rgb(156, 238, 221));
        put("Arbre", Color.rgb(168, 200, 50));
        put("Nuage", Color.rgb(105, 105, 105));
        put("Montagne", Color.rgb(134, 150, 100));
        put("Herbe", Color.rgb(123, 200, 0));
        put("Mer", Color.rgb(154, 198, 218));
        put("Rivière", Color.rgb(147, 100, 200));
        put("Roche", Color.rgb(149, 100, 50));
        put("Plante", Color.rgb(141, 230, 30));
        put("Sable", Color.rgb(153, 153, 0));
        put("Neige", Color.rgb(158, 158, 170));
        put("Eau", Color.rgb(177, 200, 255));
        put("Colline", Color.rgb(126, 200, 100));
        put("Terre", Color.rgb(110, 110, 40));
        put("Route", Color.rgb(148, 110, 40));
        put("Pierre", Color.rgb(161, 161, 100));
        put("Buisson", Color.rgb(96, 110, 50));
        put("Gravier", Color.rgb(124, 50, 200));
    }};

    public Paint mPaint;
    public int currentColor = colors.values().iterator().next();

    public ColorView(Context context) {
        this(context, null, 0);
    }

    public ColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    public Integer setRandomColor(){
        ArrayList<Integer> arr = new ArrayList<>(colors.values());
        int color = arr.get(new Random().nextInt(arr.size()));
        setColor(color);
        return color;
    }

    public int setColorIndex(int index){
        int color = new ArrayList<>(colors.values()).get(index);
        setColor(color);
        return color;
    }

    public void setColor(int color){
        currentColor = color;
        invalidate();
    }

    public int getColor(){
        return currentColor;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mPaint.setColor(currentColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShadowLayer(0.0f, 0.0f, 0.0f, Color.BLACK);
        canvas.drawPath(roundedRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), 10.0f, 10.0f), mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(45.0f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(2.0f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setShadowLayer(7.0f, 2.0f, 2.0f, Color.BLACK);

        String name = getColorName(currentColor);

        if(name != null){
            float textHeight = mPaint.descent() - mPaint.ascent();
            float textOffset = (textHeight / 2) - mPaint.descent();
            canvas.drawText(name, getWidth() / 2f, getHeight() / 2f + textOffset, mPaint);
        }
    }

    public String getColorName(int color){
        for(String s : colors.keySet()){
            if(colors.get(s) == color){
                return s;
            }
        }
        return null;
    }

    public void update(){
        invalidate();
    }

    static public Path roundedRect(float left, float top, float right, float bottom, float rx, float ry) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width/2) rx = width/2;
        if (ry > height/2) ry = height/2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        path.rLineTo(widthMinusCorners, 0);
        path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        return path;
    }
}
