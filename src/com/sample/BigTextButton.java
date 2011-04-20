package com.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class BigTextButton extends ImageButton {
	String mText = "";
	Paint mTextPaint;
	
	int mViewWidth;
	int mViewHeight;
	int mTextBaseline;
	
	public BigTextButton(Context context) {
		super(context);
		init();
	}

	public BigTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttrs(attrs);
		init();
	}

	public BigTextButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		parseAttrs(attrs);
		init();
	}
	
	/**
	 * Dig out Attributes to find text setting
	 * 
	 * This could be expanded to pull out settings for
	 * textColor, etc if desired
	 * 
	 * @param attrs
	 */
	
	private void parseAttrs(AttributeSet attrs) {
		for (int i =0; i<attrs.getAttributeCount();i++) {
			String s = attrs.getAttributeName(i);
			if (s.equalsIgnoreCase("text")) {
				mText = attrs.getAttributeValue(i);
			}
		}
	}
    
	
	/**
	 * initialize Paint for text, it will be modified when
	 * the view size is set
	 */
	
	private void init() {
		mTextPaint = new Paint();
		mTextPaint.setColor(0xFF000000);
		mTextPaint.setTypeface(Typeface.SERIF);
		mTextPaint.setTextAlign(Paint.Align.CENTER);
		mTextPaint.setAntiAlias(true);
	}
	
	/**
	 * set the scale of the text Paint objects so that the
	 * text will draw and take up the full screen width
	 */
	void adjustTextScale() {	
		// do calculation with scale of 1.0 (no scale)
		mTextPaint.setTextScaleX(1.0f);
		Rect bounds = new Rect();
		// ask the paint for the bounding rect if it were to draw this
		// text.  
		mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
		
		// determine the width
		int w = bounds.right - bounds.left;
		
		// calculate the baseline to use so that the
		// entire text is visible including the descenders
		int text_h = bounds.bottom-bounds.top;
		mTextBaseline=bounds.bottom+((mViewHeight-text_h)/2);
		
		// determine how much to scale the width to fit the view
		float xscale = ((float) (mViewWidth-getPaddingLeft()-getPaddingRight())) / w;
		
		// set the scale for the text paint
		mTextPaint.setTextScaleX(xscale);
	}
	
	/**
	 * determine the proper text size to use to fill the full height
	 */
	void adjustTextSize() {
		mTextPaint.setTextSize(100);
		mTextPaint.setTextScaleX(1.0f);
		Rect bounds = new Rect();
		// ask the paint for the bounding rect if it were to draw this
		// text
		mTextPaint.getTextBounds(mText, 0, mText.length(), bounds);
		
		// get the height that would have been produced
		int h = bounds.bottom - bounds.top;

		// make the text text up 70% of the height
		float target = (float)mViewHeight*.7f;
		
		// figure out what textSize setting would create that height
		// of text 
		float size  = ((target/h)*100f);
		
		// and set it into the paint
		mTextPaint.setTextSize(size);
	}
	
	
	/**
	 * When the view size is changed, recalculate the paint settings
	 * to have the text on the fill the view area
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// save view size
		mViewWidth = w;
		mViewHeight = h;
		
		// first determine font point size
		adjustTextSize();
		// then determine width scaling
		// this is done in two steps in case the 
		// point size change affects the width boundary
		adjustTextScale();
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		// let the ImageButton paint background as normal
		super.onDraw(canvas);
		
		// draw the text
		// position is centered on width
		// and the baseline is calculated to be positioned from the 
		// view bottom
		canvas.drawText(mText, mViewWidth/2, mViewHeight-mTextBaseline, mTextPaint);
	}
		
}
