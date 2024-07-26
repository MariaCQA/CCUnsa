package com.example.ccunsa.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InteractiveMapView extends View {
    private Paint paint;
    private Path path;
    private Paint textPaint;
    private Paint backgroundPaint;
    private Paint whitePaint;
    private List<GalleryArea> galleryAreas;

    public InteractiveMapView(Context context) {
        super(context);
        init();
    }

    public InteractiveMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#800000"));  // Guinda para el contorno de las habitaciones
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15f);

        path = new Path();

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);  // Color de fondo para el relleno de las habitaciones
        backgroundPaint.setStyle(Paint.Style.FILL);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);

        galleryAreas = new ArrayList<>();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    for (GalleryArea area : galleryAreas) {
                        if (area.rect.contains(x, y)) {
                            Toast.makeText(getContext(), area.label + " clicked", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float viewWidth = getWidth();
        float viewHeight = getHeight();

        galleryAreas.clear();

        // Draw background
        canvas.drawRect(0, 0, viewWidth, viewHeight, whitePaint);

        // Draw rooms
        drawRoom(canvas, "GALERIA I", 0.34f, 0f, 1f, 0.18f, viewWidth, viewHeight);
        drawRoom(canvas, "GALERIA II", 0.70f, 0.18f, 1f, 0.52f, viewWidth, viewHeight);
        drawRoom(canvas, "GALERIA III", 0.36f, 0.42f, 0.70f, 0.52f, viewWidth, viewHeight);
        drawRoom(canvas, "SALA", 0.36f, 0.68f, 0.70f, 0.78f, viewWidth, viewHeight);
        drawRoom(canvas, "GALERIA IV", 0f, 0f, 0.26f, 0.30f, viewWidth, viewHeight);
        drawRoom(canvas, "GALERIA V", 0f, 0.30f, 0.26f, 0.52f, viewWidth, viewHeight);
        drawRoom(canvas, "GALERIA VI", 0.70f, 0.52f, 1f, 0.78f, viewWidth, viewHeight);
        drawRoom(canvas, "VACIO", 0f, 0.52f, 0.26f, 0.78f, viewWidth, viewHeight, false, Color.parseColor("#800000"));
        drawRoom(canvas, "SS.HH", 0f, 0.78f, 0.24f, 0.85f, viewWidth, viewHeight, true, Color.CYAN);

    }

    private void drawRoom(Canvas canvas, String label, float leftRel, float topRel, float rightRel, float bottomRel, float viewWidth, float viewHeight) {
        drawRoom(canvas, label, leftRel, topRel, rightRel, bottomRel, viewWidth, viewHeight, false, Color.TRANSPARENT);
    }

    private void drawRoom(Canvas canvas, String label, float leftRel, float topRel, float rightRel, float bottomRel, float viewWidth, float viewHeight, boolean fill) {
        drawRoom(canvas, label, leftRel, topRel, rightRel, bottomRel, viewWidth, viewHeight, fill, Color.TRANSPARENT);
    }

    private void drawRoom(Canvas canvas, String label, float leftRel, float topRel, float rightRel, float bottomRel, float viewWidth, float viewHeight, boolean fill, int fillColor) {
        float left = leftRel * viewWidth;
        float top = topRel * viewHeight;
        float right = rightRel * viewWidth;
        float bottom = bottomRel * viewHeight;

        path.reset();
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.close();

        if (fill) {
            backgroundPaint.setColor(fillColor);
            canvas.drawRect(left, top, right, bottom, backgroundPaint);
        } else {
            // Dibuja el relleno transparente
            canvas.drawRect(left, top, right, bottom, whitePaint);
        }

        // Dibuja el contorno con color guinda
        paint.setColor(Color.parseColor("#800000"));
        canvas.drawPath(path, paint);

        float centerX = (left + right) / 2;
        float centerY = (top + bottom) / 2 - ((textPaint.descent() + textPaint.ascent()) / 2);
        canvas.drawText(label, centerX, centerY, textPaint);

        galleryAreas.add(new GalleryArea(label, new RectF(left, top, right, bottom)));
    }

    private static class GalleryArea {
        String label;
        RectF rect;

        GalleryArea(String label, RectF rect) {
            this.label = label;
            this.rect = rect;
        }
    }
}