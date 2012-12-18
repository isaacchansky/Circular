package com.example.circular;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class GameElement extends Drawable {

	protected float centerX, centerY;
	protected int radius;
	protected boolean visible;
	protected Paint paint;
	protected int opacity;
	
	public GameElement(float centerX, float centerY,boolean visible, int radius, Paint paint,int opacity){
		this.centerX = centerX;
		this.centerY = centerY;
		this.visible = visible;
		this.radius = radius;
		this.paint = paint;
		this.opacity = opacity;
	}
	
	//all game elements will fade out before they get destroyed, if still visible. 
	public void fadeOutAnimation(){
		
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}

	public int getItemOpacity() {
		return this.opacity;
	}

	public void setItemOpacity(int o) {
		this.opacity = o;
	}
	
	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
