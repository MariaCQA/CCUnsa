package com.example.ccunsa.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ccunsa.R;
import com.example.ccunsa.model.Pintura;

import java.util.ArrayList;
import java.util.List;

public class PaintingCanvasView extends View {
    private Paint paint;
    private Paint textPaint;
    private List<Pintura> pinturas;
    private List<Rect> paintingRects;

    public PaintingCanvasView(Context context) {
        super(context);
        init();
    }

    public PaintingCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        pinturas = new ArrayList<>();
        paintingRects = new ArrayList<>();
    }

    public void setPinturas(List<Pintura> pinturas) {
        this.pinturas = pinturas;
        invalidate(); // Redraw the view with new data
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (pinturas == null || pinturas.isEmpty()) {
            return;
        }

        float viewWidth = getWidth();
        float viewHeight = getHeight();
        float padding = 20f;
        float rectSize = (viewWidth - padding * 5) / 4;
        float textHeight = 50f;

        paintingRects.clear();
        for (int i = 0; i < pinturas.size() && i < 8; i++) {
            float left = padding + (i % 4) * (rectSize + padding);
            float top = padding + (i / 4) * (rectSize + padding);
            float right = left + rectSize;
            float bottom = top + rectSize;

            Rect rect = new Rect((int) left, (int) top, (int) right, (int) bottom);
            paintingRects.add(rect);
            canvas.drawRect(rect, paint);

            Pintura pintura = pinturas.get(i);
            int resId = getResources().getIdentifier(pintura.getIconPath(), "drawable", getContext().getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, null, rect, null);
            }

            // Dibujar nombre de la pintura y autor
            canvas.drawText(pintura.getPaintingName(), rect.centerX(), bottom + textHeight, textPaint);
            canvas.drawText(pintura.getAuthorName(), rect.centerX(), bottom + textHeight * 2, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            for (int i = 0; i < paintingRects.size(); i++) {
                if (paintingRects.get(i).contains((int) x, (int) y)) {
                    Pintura pintura = pinturas.get(i);
                    navigateToPinturaDetail(pintura.getId());
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void navigateToPinturaDetail(int pinturaId) {
        FragmentActivity activity = (FragmentActivity) getContext();
        NavHostFragment navHostFragment = (NavHostFragment) activity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            Bundle args = new Bundle();
            args.putInt("pinturaId", pinturaId);
            navController.navigate(R.id.action_roomDetailFragment_to_pinturaFragment, args);
        }
    }
}
