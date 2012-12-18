package com.example.circular;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class Shield extends GameElement{

	protected float centerX, centerY;
	protected int  radius, baseAngle;
	protected boolean visible;
	protected Paint paint;
	protected RectF oval;

	public Shield(float centerX, float centerY, boolean visible, int radius, Paint paint, RectF oval, int baseAngle,int opacity) {
		super(centerX, centerY, visible, radius, paint,opacity);
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.visible = visible;
		this.radius = radius;
		this.paint = paint;
		this.oval = oval;
		this.baseAngle = baseAngle % 360;
	}
	

	@Override
	public void draw(Canvas canvas){
		
		canvas.drawArc(oval, baseAngle, 90, false, paint);
		canvas.drawArc(oval, baseAngle + 120, 90, false, paint);
		canvas.drawArc(oval, baseAngle + 240, 90, false, paint);
	}
	
	public void rotate(int velocity){
		baseAngle += velocity;
	}

}
