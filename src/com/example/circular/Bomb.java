package com.example.circular;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Bomb extends GameElement{

	protected float centerX, centerY;
	protected boolean visible;
	protected Paint paint;
	
	public Bomb(float centerX, float centerY, boolean visible, int radius, Paint paint, int opacity) {
		super(centerX, centerY, visible, radius, paint, opacity);
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
		this.paint = paint;
	}
	
	
	@Override
	public void draw(Canvas canvas){
		canvas.drawCircle(centerX, centerY, radius, paint);
	}
	
	
	public void setRadius(int r){
		this.radius = r;
	}
	
	
	public void incrementRadius(int velocity){
		this.radius += velocity;
	}

}
