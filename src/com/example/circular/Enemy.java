package com.example.circular;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class Enemy extends GameElement{

	//reward type: 0 -> regular, 1 -> life, 2 -> shield, 3 -> bomb
	protected int rewardType, rewardValue, radius;
	protected double velocity;
	protected Paint paint;
	protected float centerX, centerY, startX, startY, endX, endY;
	protected int opacity;
	protected Paint symbol;
	protected RectF oval;
	
	public Enemy(float centerX, float centerY, int color, double velocity, boolean visible, int radius, float startX, float startY, int endX, int endY, int rewardType, int rewardValue, Paint paint, int opacity) {
		super(centerX, centerY, visible, radius,paint,opacity);
		this.rewardType = rewardType;
		this.rewardValue = rewardValue;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.centerX = centerX;
		this.centerY = centerY;
		this.paint = paint;
		this.velocity = velocity;
		this.radius = radius;
		this.opacity = opacity;
		oval = new RectF(this.centerX - (int)(this.radius/1.5), centerY - (int)(this.radius/1.5),
				centerX + (int)(this.radius/1.5), centerY + (int)(this.radius/1.5));
		symbol = GameView.symbolPaint;
	}
	
	
	public boolean intersectsWithBaseCamp(BaseCamp basecamp){
		
		int biggerRadius = basecamp.radius > this.radius ? basecamp.radius : this.radius;
		if(distanceBetweenCenters(basecamp.centerX,basecamp.centerY) < biggerRadius){
			return true;
		}else{
			return false;
		}
	}
	
	
	public boolean intersectsWithShot(Shot shot){
		
		if(distanceBetweenCenters(shot.centerX, shot.centerY) < shot.radius/2 + this.radius){
			return true;
		}else{
			return false;
		}
	}
	
	
	public boolean intersectsWithShield(Shield shield){
		
		if(distanceBetweenCenters(shield.centerX, shield.centerY) < shield.radius + this.radius){
			return true;
		}else{
			return false;
		}
	}
	
	
	public boolean intersectsWithBomb(Bomb bomb){
		
		if(distanceBetweenCenters(bomb.centerX, bomb.centerY) < bomb.radius){
			return true;
		}else{
			return false;
		}
	}
	
	
	public double distanceBetweenCenters(float x, float y){
		return  Math.sqrt(Math.pow(Math.abs(this.centerX-x),2) +  Math.pow(Math.abs(this.centerY-y),2));
	}

	
	public double calculateAngle(float startX, float startY, float endX, float endY){
		double xchange = this.endX - this.startX;
		double ychange = this.endY - this.startY;
		double angleInDegrees = 90 + (Math.atan2(ychange, xchange)*(180/Math.PI));
		
		return Math.toRadians(angleInDegrees);
	}
	
	
	@Override
	public void draw(Canvas canvas){
		//CHANGE add differences based on reward type
		
		canvas.drawCircle(centerX, centerY, radius, paint);
		
		//draw unique symbol
		switch(this.rewardType){
		case 1:
			//+life
			
			canvas.drawLine(this.centerX, centerY+(this.radius/3), centerX, centerY-(this.radius/3), this.symbol);
			canvas.drawLine(this.centerX+(this.radius/3), centerY, centerX-(this.radius/3), centerY, this.symbol);
			break;
		case 2:
			//+shield
			oval.set(this.centerX - (int)(this.radius/2), centerY - (int)(this.radius/2),
					centerX + (int)(this.radius/2), centerY + (int)(this.radius/2));
			canvas.drawArc(oval, 0, 90, false, this.symbol);
			canvas.drawArc(oval, 0 + 120, 90, false, this.symbol);
			canvas.drawArc(oval, 0 + 240, 90, false, this.symbol);
			break;
		case 3:
			//+bomb
			canvas.drawCircle(this.centerX, this.centerY, this.radius/2, symbol);
			break;
		default:
			break;
		}
		
	}

}
