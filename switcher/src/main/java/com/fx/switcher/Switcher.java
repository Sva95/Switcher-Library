package com.fx.switcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Switcher extends View {

    private Paint paintRoundRect;
    private Paint paintCornerRoundRect;
    private Paint cirlePaint;

    private float getViewWidth;
    private float getViewHeight;

    private float roundSize;
    private float circleDrawRadius = 0f;
    private float paddingCorner = 3f;
    private float rigthCircleRadius;
    private float leftCircleRadius;
    private float widthPaddingCircle;

    private RectF drawRoundRect;

    @ColorInt
    private int switchBackgroundColor;
    private int colorRigthCircle;
    private int colorLeftCircle;
    private int quarterView;
    private int animDuration = 300;

    private boolean firstShow = true;
    private boolean state;

    private OnClickSwitch onClickSwitch;

    public interface OnClickSwitch {
        void switchOn();

        void switchOff();
    }

    public Switcher(Context context) {
        super(context);
        init(null);
    }

    public Switcher(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Switcher(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.Switcher, 0, 0);
        switchBackgroundColor = typedArray.getColor(R.styleable.Switcher_switchBackgroundColor, getResources().getColor(R.color.colorPrimary));
        state = typedArray.getBoolean(R.styleable.Switcher_switchState, false);
        typedArray.recycle();

        colorRigthCircle = getResources().getColor(R.color.color_paintRoundRect);
        colorLeftCircle = getResources().getColor(R.color.color_paintCornerRoundRect);

        paintRoundRect = new Paint();
        paintRoundRect.setAntiAlias(true);
        paintRoundRect.setStyle(Paint.Style.FILL);
        paintRoundRect.setColor(getResources().getColor(R.color.color_paintRoundRect));

        paintCornerRoundRect = new Paint();
        paintCornerRoundRect.setAntiAlias(true);
        paintCornerRoundRect.setStyle(Paint.Style.STROKE);
        paintCornerRoundRect.setStrokeWidth(6);
        paintCornerRoundRect.setColor(getResources().getColor(R.color.color_paintCornerRoundRect));

        cirlePaint = new Paint();
        cirlePaint.setAntiAlias(true);
        cirlePaint.setStyle(Paint.Style.FILL);

        drawRoundRect = new RectF();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        getViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        getViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        quarterView = (int) (getViewWidth / 4);
        widthPaddingCircle = (int) (getViewWidth / 4);

        roundSize = getViewHeight / 1.6f;
        rigthCircleRadius = getViewHeight / 3.2f;
        leftCircleRadius = getViewHeight / 11;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawRoundRect.set(paddingCorner, paddingCorner, getWidth() - paddingCorner, getHeight() - paddingCorner);

        if (firstShow) {
            drawSwitch(canvas);

        } else {
            drawAnimSwitch(canvas);
        }
    }


    private void drawSwitch(Canvas canvas) {
        if (state) {
            paintRoundRect.setColor(switchBackgroundColor);
            paintCornerRoundRect.setColor(switchBackgroundColor);
            cirlePaint.setColor(colorRigthCircle);

            canvas.drawRoundRect(drawRoundRect, roundSize, roundSize, paintRoundRect);
            canvas.drawRoundRect(drawRoundRect, roundSize, roundSize, paintCornerRoundRect);
            canvas.drawCircle(getViewWidth - widthPaddingCircle, getViewHeight / 2, rigthCircleRadius, cirlePaint);

        } else {
            paintRoundRect.setColor(colorLeftCircle);
            paintCornerRoundRect.setColor(colorLeftCircle);
            cirlePaint.setColor(colorLeftCircle);

            canvas.drawRoundRect(drawRoundRect, roundSize, roundSize, paintRoundRect);
            canvas.drawRoundRect(drawRoundRect, roundSize, roundSize, paintCornerRoundRect);
            canvas.drawCircle(widthPaddingCircle, getViewHeight / 2, leftCircleRadius, cirlePaint);
        }
    }

    private void drawAnimSwitch(Canvas canvas) {
        canvas.drawRoundRect(drawRoundRect, roundSize, roundSize, paintRoundRect);
        canvas.drawRoundRect(drawRoundRect, roundSize, roundSize, paintCornerRoundRect);
        canvas.drawCircle(widthPaddingCircle, getViewHeight / 2, circleDrawRadius, cirlePaint);
    }

    private void switchOff() {
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator animRadius = ValueAnimator.ofFloat(rigthCircleRadius, leftCircleRadius);
        animRadius.addUpdateListener(animation -> {
            circleDrawRadius = (float) animation.getAnimatedValue();
        });

        ValueAnimator animWidth = ValueAnimator.ofFloat(getViewWidth - quarterView, quarterView);
        animWidth.addUpdateListener(animation -> {
            widthPaddingCircle = (float) animation.getAnimatedValue();
        });

        ValueAnimator animColorCircle = new ValueAnimator();
        animColorCircle.setIntValues(colorRigthCircle, colorLeftCircle);
        animColorCircle.setEvaluator(new ArgbEvaluator());
        animColorCircle.addUpdateListener(animation -> {
            cirlePaint.setColor((int) animation.getAnimatedValue());
        });

        ValueAnimator animBackgroundColor = new ValueAnimator();
        animBackgroundColor.setIntValues(switchBackgroundColor, colorRigthCircle);
        animBackgroundColor.setEvaluator(new ArgbEvaluator());
        animBackgroundColor.addUpdateListener(animation -> {
            paintRoundRect.setColor((int) animation.getAnimatedValue());
        });

        ValueAnimator animCornerColor = new ValueAnimator();
        animCornerColor.setIntValues(switchBackgroundColor, colorLeftCircle);
        animCornerColor.setEvaluator(new ArgbEvaluator());
        animCornerColor.addUpdateListener(animation -> {
            paintCornerRoundRect.setColor((int) animation.getAnimatedValue());
            invalidate();
        });

        animatorSet.playTogether(animRadius, animWidth, animColorCircle, animBackgroundColor, animCornerColor);
        animatorSet.setDuration(animDuration);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                state = false;
            }
        });
        animatorSet.start();
    }

    private void switchOn() {
        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator animRadius = ValueAnimator.ofFloat(leftCircleRadius, rigthCircleRadius);
        animRadius.addUpdateListener(animation -> {
            circleDrawRadius = (float) animation.getAnimatedValue();
        });

        ValueAnimator animWidth = ValueAnimator.ofFloat(quarterView, getViewWidth - quarterView);
        animWidth.addUpdateListener(animation -> {
            widthPaddingCircle = (float) animation.getAnimatedValue();
        });

        ValueAnimator animColorCircle = new ValueAnimator();
        animColorCircle.setIntValues(colorLeftCircle, colorRigthCircle);
        animColorCircle.setEvaluator(new ArgbEvaluator());
        animColorCircle.addUpdateListener(animation -> {
            cirlePaint.setColor((int) animation.getAnimatedValue());
        });

        ValueAnimator animBackgroundColor = new ValueAnimator();
        animBackgroundColor.setIntValues(colorRigthCircle, switchBackgroundColor);
        animBackgroundColor.setEvaluator(new ArgbEvaluator());
        animBackgroundColor.addUpdateListener(animation -> {
            paintRoundRect.setColor((int) animation.getAnimatedValue());
        });

        ValueAnimator animCornerColor = new ValueAnimator();
        animCornerColor.setIntValues(colorLeftCircle, switchBackgroundColor);
        animCornerColor.setEvaluator(new ArgbEvaluator());
        animCornerColor.addUpdateListener(animation -> {
            paintCornerRoundRect.setColor((int) animation.getAnimatedValue());
            invalidate();
        });

        animatorSet.playTogether(animRadius, animWidth, animColorCircle, animBackgroundColor, animCornerColor);
        animatorSet.setDuration(animDuration);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                state = true;
            }
        });
        animatorSet.start();
    }

    public boolean onTouchEvent(MotionEvent event) {
        firstShow = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                if (state) {
                    switchOff();
                    if (onClickSwitch != null) {
                        onClickSwitch.switchOff();
                    }
                } else {
                    switchOn();
                    if (onClickSwitch != null) {
                        onClickSwitch.switchOn();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }

        return true;
    }

    public void setSwitchBackgroundColor(@ColorInt int switchBackgroundColor) {
        this.switchBackgroundColor = switchBackgroundColor;
    }

    public void setOnClickSwitch(OnClickSwitch onClickSwitch) {
        this.onClickSwitch = onClickSwitch;
    }

    public void setState(boolean state) {
        this.state = state;
    }


}