package com.example.circular;

import android.graphics.Canvas;
import android.graphics.Paint;

public class BaseCamp extends GameElement {

	protected float centerX, centerY;
	protected int radius;
	protected boolean visible;
	protected Paint footprintPaint, centerPaint;
	protected int initialRadius;

	public BaseCamp(float centerX, float centerY, boolean visible, int radius,
			Paint paint, Paint footprintPaint, Paint centerPaint, int opacity) {
		super(centerX, centerY, visible, radius, paint, opacity);

		this.centerX = centerX;
		this.centerY = centerY;
		this.visible = visible;
		this.radius = radius;
		this.paint = paint;
		this.footprintPaint = footprintPaint;
		this.centerPaint = centerPaint;
		this.initialRadius = radius;
	}

	
	@Override
	public void draw(Canvas canvas) {
		//draw footprint with initial radius
		canvas.drawCircle(centerX, centerY, initialRadius, footprintPaint);
		//basecamp & center knockout circle
		canvas.drawCircle(centerX, centerY, radius, paint);
		canvas.drawCircle(centerX, centerY, radius, centerPaint);
	}
	
	
	public void setRadius(float r){
		this.radius = (int)r;
	}

}
