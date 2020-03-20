package com.horovod.android.merchandiserdemo.view;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ImageMoveListener implements View.OnTouchListener {

    private GestureDetector moveDetector;
    private View view;

    ScaleGestureDetector scaleDetector;
    float scaleFactor = 1.0f;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private final int SENSITIVITY = 7;


    public ImageMoveListener(View view) {
        this.view = view;
        moveDetector = new GestureDetector(view.getContext(), gListener);
        scaleDetector = new ScaleGestureDetector(view.getContext(), new ScaleListener());
    }

    public View getView() {
        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        moveDetector.onTouchEvent(event);
        return true;
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float factor = detector.getScaleFactor();
            float oldFactor = 1;

            oldFactor = scaleFactor;
            scaleFactor *=factor;
            scaleFactor = Math.max(0.8f, Math.min(scaleFactor, 10.0f));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            return false;
        }
    }

    private GestureDetector.OnGestureListener gListener = new GestureDetector.SimpleOnGestureListener() {
        private float downX, downY;

        @Override
        public boolean onDown(MotionEvent e) {
            downX = e.getRawX() - view.getTranslationX();
            downY = e.getRawY() - view.getTranslationY();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float x = e2.getRawX();
            float y = e2.getRawY();
            view.setTranslationX( x - downX);
            view.setTranslationY(y - downY);
            return true;
        }
    };


}
