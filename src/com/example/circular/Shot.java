package com.example.circular;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Shot extends GameElement {

	protected float startX, startY, endX, endY;
	protected int opacity;
	
	public Shot(float centerX, float centerY, float startX, float startY,
			float endX, float endY, boolean visible,
			int radius, Paint paint, int opacity) {
		super(centerX, centerY, visible, radius, paint, opacity);

		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.paint = paint;
		this.opacity = opacity;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(centerX, centerY, radius, paint);
	}

	
	public double calculateAngle(float startX, float startY, float endX, float endY){
		double xchange = this.endX - this.startX;
		double ychange = this.endY - this.startY;
		double angleInDegrees = 90 + (Math.atan2(ychange, xchange)*(180/Math.PI));
		
		return Math.toRadians(angleInDegrees);
	}
	
}
