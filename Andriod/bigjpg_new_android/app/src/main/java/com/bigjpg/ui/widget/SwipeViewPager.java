
package com.bigjpg.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 描述:可控制能否滑动的ViewPager
 * 
 * @author mfx
 */
public class SwipeViewPager extends ViewPager {

	private OnMoveIntercept mOnMoveIntercept;
	
    /**
     * 是否能滑动
     */
    private boolean mIsCanScroll = true;

    /**
     * 是否是滑动换页
     */
    private boolean mIsPageSmoothChanged = true;

    public SwipeViewPager(Context context) {
        super(context);
    }

    public SwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

	public void setOnMoveIntercept(OnMoveIntercept onMoveIntercept) {
		mOnMoveIntercept = onMoveIntercept;
	}
    
    public boolean isPageSmoothChanged() {
        return mIsPageSmoothChanged;
    }

    public void setPageSmoothChanged(boolean isPageSmoothChanged) {
        mIsPageSmoothChanged = isPageSmoothChanged;
    }

    public void setCanScroll(boolean isCanScroll) {
        mIsCanScroll = isCanScroll;
    }

    public boolean isCanScroll() {
        return mIsCanScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mIsPageSmoothChanged) {
            mIsPageSmoothChanged = true;
        }
        if (mIsCanScroll) {
            try {
                return super.onTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsCanScroll) {
            try {
            	
            	if(mOnMoveIntercept!=null && !mOnMoveIntercept.onIntercept(ev)){
        			return false;
        		}
            	
            	if(mOnMoveIntercept!=null && mOnMoveIntercept.onIntercept(ev)){
            		return super.onInterceptTouchEvent(ev);
            	}
            	
            	return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return false;

    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	  final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    	  if(heightMode != MeasureSpec.EXACTLY){
    		    int height = 0;
    		    for (int i = 0; i < getChildCount(); i++) {
    		      View child = getChildAt(i);
    		      child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    		      int h = child.getMeasuredHeight();
    		      if (h > height)
    		        height = h;
    		    }
    		    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    	  }
	      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
	public interface OnMoveIntercept{
		/**
		 * 返回结果为true则截止Move事件只在该View上执行，否则继续分发
		 * @param ev
		 * @return 
		 */
		boolean onIntercept(MotionEvent ev);
	}
}
