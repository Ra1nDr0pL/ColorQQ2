package me.qiwu.colorqq.library.RCLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Checkable;
import android.widget.ImageView;

import me.qiwu.colorqq.util.DensityUtil;

@SuppressLint("AppCompatCustomView")
public class RCImageView extends ImageView implements Checkable, RCAttrs {

    RCHelper mRCHelper;

    public RCImageView(Context context) {
        this(context, null);
    }

    public RCImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RCImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRCHelper = new RCHelper();
        mRCHelper.initAttrs(context, attrs);
        if ("head".equals(getTag())){
            setRadius(DensityUtil.dip2px(getContext(),5));
        } else if ("main".equals(getTag())){
            setRadius(DensityUtil.dip2px(getContext(),20));
        }else if ("circle".equals(getTag())){
            setRoundAsCircle(true);
        } else if ("musicHead".equals(getTag())){
            setRadius(DensityUtil.dip2px(getContext(),5));
            setStrokeWidth(DensityUtil.dip2px(getContext(),1));
            setStrokeColor(Color.parseColor("#efeff0"));
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRCHelper.onSizeChanged(this, w, h);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mRCHelper.mClipBackground) {
            canvas.save();
            canvas.clipPath(mRCHelper.mClipPath);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(mRCHelper.mLayer, null, Canvas.ALL_SAVE_FLAG);
        super.onDraw(canvas);
        mRCHelper.onClipDraw(canvas);
        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN && !mRCHelper.mAreaRegion.contains((int) ev.getX(), (int) ev.getY())) {
            return false;
        }
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            refreshDrawableState();
        } else if (action == MotionEvent.ACTION_CANCEL) {
            setPressed(false);
            refreshDrawableState();
        }
        return super.dispatchTouchEvent(ev);
    }


    //--- 公开接口 ----------------------------------------------------------------------------------

    public void setClipBackground(boolean clipBackground) {
        mRCHelper.mClipBackground = clipBackground;
        invalidate();
    }

    public void setRoundAsCircle(boolean roundAsCircle) {
        mRCHelper.mRoundAsCircle = roundAsCircle;
        invalidate();
    }

    public void setRadius(int radius) {
        for (int i = 0; i < mRCHelper.radii.length; i++) {
            mRCHelper.radii[i] = radius;
        }
        invalidate();
    }

    public void setTopLeftRadius(int topLeftRadius) {
        mRCHelper.radii[0] = topLeftRadius;
        mRCHelper.radii[1] = topLeftRadius;
        invalidate();
    }

    public void setTopRightRadius(int topRightRadius) {
        mRCHelper.radii[2] = topRightRadius;
        mRCHelper.radii[3] = topRightRadius;
        invalidate();
    }

    public void setBottomLeftRadius(int bottomLeftRadius) {
        mRCHelper.radii[6] = bottomLeftRadius;
        mRCHelper.radii[7] = bottomLeftRadius;
        invalidate();
    }

    public void setBottomRightRadius(int bottomRightRadius) {
        mRCHelper.radii[4] = bottomRightRadius;
        mRCHelper.radii[5] = bottomRightRadius;
        invalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        mRCHelper.mStrokeWidth = strokeWidth;
        invalidate();
    }

    public void setStrokeColor(int strokeColor) {
        mRCHelper.mStrokeColor = strokeColor;
        invalidate();
    }

    @Override
    public void invalidate() {
        if (null != mRCHelper)
            mRCHelper.refreshRegion(this);
        super.invalidate();
    }

    public boolean isClipBackground() {
        return mRCHelper.mClipBackground;
    }

    public boolean isRoundAsCircle() {
        return mRCHelper.mRoundAsCircle;
    }

    public float getTopLeftRadius() {
        return mRCHelper.radii[0];
    }

    public float getTopRightRadius() {
        return mRCHelper.radii[2];
    }

    public float getBottomLeftRadius() {
        return mRCHelper.radii[4];
    }

    public float getBottomRightRadius() {
        return mRCHelper.radii[6];
    }

    public int getStrokeWidth() {
        return mRCHelper.mStrokeWidth;
    }

    public int getStrokeColor() {
        return mRCHelper.mStrokeColor;
    }


    //--- Selector 支持 ----------------------------------------------------------------------------

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        mRCHelper.drawableStateChanged(this);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mRCHelper.mChecked != checked) {
            mRCHelper.mChecked = checked;
            refreshDrawableState();
            if (mRCHelper.mOnCheckedChangeListener != null) {
                mRCHelper.mOnCheckedChangeListener.onCheckedChanged(this, mRCHelper.mChecked);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mRCHelper.mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mRCHelper.mChecked);
    }

    public void setOnCheckedChangeListener(RCHelper.OnCheckedChangeListener listener) {
        mRCHelper.mOnCheckedChangeListener = listener;
    }
}
