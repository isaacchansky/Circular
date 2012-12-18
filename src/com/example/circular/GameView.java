package com.example.circular;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{

	
	public interface gameOverDialog{
		public void bringUpEndGameDialog();
	}
	
	
	protected GameThread thread;
	private Activity theActivity;

	/*
	 * =====================================================================
	 * UI stuff
	 * =====================================================================
	 */
	// Colors! Glorious Colors!
	private int backgroundColor = Color.argb(255, 247, 247, 237);
	private int basecampColor = Color.argb(255, 1, 176, 243);
	private int shotColor = Color.argb(255, 255, 202, 50);
	private int enemyColor = Color.argb(255, 171, 0, 70);
	private int shieldColor = Color.argb(255, 86, 210, 245);
	private int bombColor = Color.argb(100, 100, 100, 120);
	private int textColor = Color.argb(100, 0, 0, 0);
	private int basecampFootprintColor = Color.argb(50, 0, 0, 0);
	
	private int enemyFootprintColor = Color.argb(50, 171, 0, 70);
	private int shotFootprintColor = Color.argb(50, 0, 0, 0);


	// dimensions
	private int screenWidth;
	private int screenHeight;
	private int centerX;
	private int centerY;
	
	private int basecampRadius;
	private int basecampFootprintRadius;
	private int shieldRadius;
	private int bombRadius;
	private int shotRadius;
	private int enemyRadius;
	private int gestureFieldRadius;

	private int shotVelocity;
	private double enemyVelocity;
	private int shieldVelocity;
	private int bombVelocity;

	private double enemyFrequency;
	
	// gameplay UI info
	private Paint bombBar;
	private Paint shieldBar;
	private Paint scoreText;

	// game UI objects
	protected static Paint symbolPaint;
	private Paint backgroundPaint;
	private Paint basecampPaint;
	private Paint basecampCenterPaint;
	private Paint shieldPaint;
	private Paint bombPaint;
	private Paint shotPaint;
	private Paint basecampFootprintPaint;
	private Paint fieldMarker;
	private Paint enemyPaint;
	
	private RectF oval;
	
	

	/*
	 * Background game data
	 */
	private float lifeAmt;
	private float bombAmt;
	private float shieldAmt;
	private double initialTime;
	private double timeElapsed;
	private double shieldTimeElapsed;
	private double shieldInitialTime;
	private int score;
	private int scoreThreshold;

	// multi touch point variables
	private float touchStartX, touchStartY, touchEndX, touchEndY;
	private float multiTouchStartX, multiTouchStartY, multiTouchEndX,
			multiTouchEndY;

	// game state info
	private boolean GAME_OVER;
	private boolean GAME_RUNNING;
	private boolean GAME_ACTIVE;
	private boolean SHIELD_UP;
	private boolean BOMB_DROPPED;
	private boolean SHOW_ENDGAME_DIALOG;
	
	protected boolean hasBeenPutAside = false;
	
	private int DIFFICULTY_LEVEL = 1;

	// game object managers
	private List<Shot> activeShots;
	private ArrayList<Enemy> activeEnemies;
	private HashMap<Shot, Enemy> inactivePairs;

	private ArrayList<GameElement> removalList;
	
	// static objects
	private BaseCamp basecamp;
	private Shield shield;
	private Bomb bomb;
	
	// thread stuff
	private boolean threadStarted = false;

	private Context gameContext;
	
	
	
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		theActivity = (Activity) context;
		Log.e(this.toString(), "GameView constructor");
		getHolder().addCallback(this);
		
		gameContext = context;

		backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		symbolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		basecampPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		basecampCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		basecampFootprintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		fieldMarker = new Paint(Paint.ANTI_ALIAS_FLAG);

		shieldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		bombPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		shotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

		enemyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		bombBar = new Paint();
		shieldBar = new Paint();
		scoreText = new Paint(Paint.ANTI_ALIAS_FLAG);


	}
	

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.e(this.toString(), "called onSizeChanged()");
		// set up dimensions and stuff
		screenWidth = w;
		screenHeight = h;
		centerX = w / 2;
		centerY = h / 2;
		basecampRadius = screenWidth / 6;
		basecampFootprintRadius = screenWidth/6;
		shieldRadius = (int) (basecampFootprintRadius * 1.2);
		shotRadius = screenWidth / 20;
		enemyRadius = screenWidth / 14;
		bombRadius = screenWidth/6;
		gestureFieldRadius = screenWidth / 4;

		
		// paint attributes
		backgroundPaint.setColor(backgroundColor);
		symbolPaint.setColor(backgroundColor);
		symbolPaint.setStrokeWidth(4);
		symbolPaint.setStyle(Paint.Style.STROKE);
		
		basecampPaint.setStrokeWidth(8);
		basecampPaint.setStyle(Paint.Style.STROKE);
		basecampPaint.setColor(basecampColor);
		
		basecampCenterPaint.setColor(backgroundColor);
		basecampCenterPaint.setStyle(Paint.Style.FILL);
		
		basecampFootprintPaint.setStyle(Paint.Style.FILL);
		basecampFootprintPaint.setColor(basecampFootprintColor);
		
		shieldPaint.setColor(shieldColor);
		shieldPaint.setStyle(Paint.Style.STROKE);
		shieldPaint.setStrokeWidth(10);
		
		bombPaint.setColor(bombColor);
		bombPaint.setStyle(Paint.Style.STROKE);
		bombPaint.setStrokeWidth(15);
		
		enemyPaint.setColor(enemyColor);
		enemyPaint.setStyle(Paint.Style.FILL);
		
		fieldMarker.setColor(Color.LTGRAY);
		fieldMarker.setStrokeWidth(3);
		fieldMarker.setStyle(Paint.Style.STROKE);
		
		bombBar.setColor(bombColor);
		bombBar.setStyle(Paint.Style.STROKE);
		bombBar.setStrokeWidth(20);
		
		shieldBar.setColor(shieldColor);
		shieldBar.setStyle(Paint.Style.STROKE);
		shieldBar.setStrokeWidth(20);
		
		scoreText.setColor(textColor);
		scoreText.setTextSize(72);
		scoreText.setTypeface(Circular.nexa_light);
		scoreText.setTextAlign(Paint.Align.CENTER);
		
		shotPaint.setColor(shotColor);
		shotPaint.setStyle(Paint.Style.FILL);	
		

		oval = new RectF(centerX - shieldRadius, centerY - shieldRadius,
				centerX + shieldRadius, centerY + shieldRadius);
	}
	
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.e(this.toString(), "called surfaceChanged()");
	}

	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(this.toString(), "called surfaceCreated()");
		setWillNotDraw(true);
		if(!hasBeenPutAside){
			thread = new GameThread(holder, theActivity, null);
			startGame();
		}else{
			
		}
		
		thread.setGameRunning(true);
		thread.start();
		thread.setStarted(true);
	}

	
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(this.toString(), "called surfaceDestroyed()");
		boolean retry = true;
		thread.setGameRunning(false);
		thread.setGameActive(false);
		while(retry){
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
		
	}

	

	
	
	/* OTHER METHODS
	 * ===============================================================
	 */
	
	public void startGame() {
		Log.e(this.toString(), "called startGame()");
		/*
		 * set up environment variables... may want to do some sort of
		 * synchronized() stuff. lock elements of the game that you want to
		 * wait on until environment is established
		 */

		// set up game variables
		shotVelocity = 10;
		enemyVelocity = 1;
		shieldVelocity = 1;
		bombVelocity = 5;
		enemyFrequency = 1.5;
		
		lifeAmt = 100;
		bombAmt = 100;
		shieldAmt = 100;
		
		initialTime = System.nanoTime();
		timeElapsed = System.nanoTime();
		shieldTimeElapsed = System.nanoTime();
		shieldInitialTime = System.nanoTime();
		score = 0;
		scoreThreshold = 0;
		
		// initialize object managers
		activeShots = Collections.synchronizedList( new ArrayList<Shot>());
		activeEnemies = new ArrayList<Enemy>();
		inactivePairs = new HashMap<Shot, Enemy>();
		removalList = new ArrayList<GameElement>();

		
		// initialize objects that only have one instance
		basecamp = new BaseCamp(centerX, centerY, true, basecampRadius, basecampPaint, basecampFootprintPaint, basecampCenterPaint,255);
		shield = new Shield(centerX, centerY, false, shieldRadius, shieldPaint, oval, 0,255);
		bomb = new Bomb(centerX, centerY, false, bombRadius, bombPaint,255);

		GAME_OVER = false;
		GAME_RUNNING = true;
		GAME_ACTIVE = true;
	}
	
	public int getScore(){
		return score;
	}
	
	
	public int distFromCenter(float cx, float cy, float px, float py) {
		return (int) Math.sqrt(Math.pow(Math.abs(cx - px), 2)
				+ Math.pow(Math.abs(cy - py), 2));
	}
	
	
	public void endGame(){
		//Only called when game is finished by a depletion of 'life'
		//NOT called when game is canceled
		Log.e(this.toString(), "called endGame");
		SHOW_ENDGAME_DIALOG = true;
		
		thread.setGameRunning(false);
		thread.setGameActive(false);
		GAME_OVER = true;
		
		//activeShots = null;
		//activeEnemies = null;
		//inactivePairs = null;
		
		//bring up endGame fragment/activity
		
		Log.e(this.toString(), "about to bring up end game dialog");
		((Circular)gameContext).bringUpEndGameDialog();
		
	}
	
	public void stopGame(){
		//called when quit to start screen
		Log.e(this.toString(), "called stopGame");
		SHOW_ENDGAME_DIALOG = false;
		
		thread.setGameRunning(false);
		thread.setGameActive(false);
		
		try {
			thread.join();
			Log.e(this.toString(), "join thread");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.e(this.toString(),"couldnt join");
			e.printStackTrace();
		}
		
		//activeShots = null;
		//activeEnemies = null;
		//inactivePairs = null;
		
	}
	
	
	public void restartGame(){
		thread = new GameThread(getHolder(), theActivity, null);
		startGame();
		thread.setStarted(true);
		thread.start();
		thread.setGameRunning(true);
	}
	
	
	
	
	
	
	
	/*
	 * =====================================================================
	 * GESTURE RECOGNITION
	 * =====================================================================
	 */

	private boolean FIRE_STATE = false;
	private boolean SHIELD_STATE = false;
	private boolean BOMB_STATE = false;

	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getActionMasked();
		int actionIndex = event.getActionIndex();
		
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:

			touchStarted(event, event.getX(actionIndex),
					event.getY(actionIndex));

			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
			multiTouchStarted(event, event.getX(newPointerIndex),
					event.getY(newPointerIndex));

			break;

		case MotionEvent.ACTION_UP:
			touchEnded(event, event.getX(actionIndex), event.getY(actionIndex));
			break;

		case MotionEvent.ACTION_POINTER_UP:
			final int upPointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			multiTouchEnded(event, event.getX(upPointerIndex),
					event.getY(upPointerIndex));
			break;

		default:
			break;
		}
		return true;
	}

	
	
	public void touchStarted(MotionEvent event, float x, float y) {
		touchStartX = x;
		touchStartY = y;
		if (distFromCenter(centerX, centerY, touchStartX, touchStartY) < gestureFieldRadius) {
			FIRE_STATE = true;
		}
	}

	
	
	public void touchEnded(MotionEvent event, float x, float y) {
		touchEndX = x;
		touchEndY = y;
		if (distFromCenter(centerX, centerY, touchEndX, touchEndY) > gestureFieldRadius
				&& event.getPointerCount() == 1 && FIRE_STATE) {
			gestureActionHandler(1);
			Log.e("gesture recognized", "shot fired");
		}
		gestureActionHandler(0);

	}

	
	
	public void multiTouchStarted(MotionEvent event, float x, float y) {
		// once there are two fingers down, no longer considering a fire event
		FIRE_STATE = false;
		multiTouchStartX = x;
		multiTouchStartY = y;
		if (distFromCenter(centerX, centerY, multiTouchStartX, multiTouchStartY) > gestureFieldRadius
				&& distFromCenter(centerX, centerY, touchStartX, touchStartY) > gestureFieldRadius) {
			// both fingers touching outside gesture field
			SHIELD_STATE = true;
			BOMB_STATE = false;
		} else {
			if (distFromCenter(centerX, centerY, multiTouchStartX,
					multiTouchStartY) < gestureFieldRadius
					&& distFromCenter(centerX, centerY, touchStartX,
							touchStartY) < gestureFieldRadius) {
				// both fingers touching inside gesture field
				BOMB_STATE = true;
				SHIELD_STATE = false;
			}
		}
	}

	
	
	public void multiTouchEnded(MotionEvent event, float x, float y) {
		multiTouchEndX = x;
		multiTouchEndY = y;
		if (distFromCenter(centerX, centerY, multiTouchEndX, multiTouchEndY) > gestureFieldRadius
				&& distFromCenter(centerX, centerY, touchEndX, touchEndY) > gestureFieldRadius
				&& BOMB_STATE) {
			// both fingers started inside gesture field, and ended outside
			// gestureField
			BOMB_DROPPED = true;
			gestureActionHandler(3);
			Log.e("gesture recognized", "Bomb Dropped");
		}
		if (distFromCenter(centerX, centerY, multiTouchEndX, multiTouchEndY) < gestureFieldRadius
				&& distFromCenter(centerX, centerY, touchEndX, touchEndY) < gestureFieldRadius
				&& SHIELD_STATE) {
			// both fingers started outside gesture field, and ended inside
			// gestureField
			SHIELD_UP = true;
			gestureActionHandler(2);
			Log.e("gesture recognized", "Shield Up");
		}
		gestureActionHandler(0);

	}

	
	
	public void gestureActionHandler(int action) {
		/*
		 * do whatever based on the gesture... 1 is shoot, 2 is shield, 3 is
		 * bomb...
		 */
		switch (action) {
		case 1:

			Shot shot = new Shot(centerX, centerY, touchStartX, touchStartY,
					touchEndX, touchEndY, true, shotRadius, shotPaint, 255);
			synchronized(activeShots){
				activeShots.add(shot);
			}
			
			Log.e("OBJECT CREATION", "new shot object, added in activeShots");
			break;
		case 2:
			// set visibility of shield object (should have already been created
			// set shield timer
			SHIELD_UP = true;
			shield.visible = true;
			shieldInitialTime = System.nanoTime();
			break;
		case 3:
			// can reuse the same bomb object and just show and hide it
			// reset the radius to the same as the current basecamp radius
			// animate?
			BOMB_DROPPED = true;
			bomb.visible = true;
			break;
		default:
			break;
		}

		// reset for next gesture
		FIRE_STATE = false;
		BOMB_STATE = false;
		SHIELD_STATE = false;

	}

	

	
	/* =====================================================================
	 *  THREAD STUFF
	 * =====================================================================
	 */
	public void pauseGame() {
		 Log.e(this.toString(), "called pauseGame");
		// pauses animations and timers and stuff.
		synchronized (thread) {
			thread.setGameRunning(false);
			Log.e("GAME STATE", "paused");
		}
	}

	
	public void unpauseGame() {
		 Log.e(this.toString(), "called unpauseGame");
		synchronized (thread) {
			//notify();
			thread.setGameRunning(true);
			
			//thread.run();
		
		}
	}

	
	public boolean threadStarted() {
		return threadStarted;
	}

	
	public boolean threadActive() {
		return GAME_RUNNING;
	}

	
	public void setThreadStarted(boolean b) {
		Log.e(this.toString(), "setThreadStarted");
		this.threadStarted = b;
	}
	
	
	public void setThreadActive(boolean b) {
		Log.e(this.toString(), "setThreadActive");
		this.GAME_RUNNING = b;
	}

	
	
	
	
	
	/*
	 * =====================================================================
	 * game thread 
	 * =====================================================================
	 */
	private class GameThread extends Thread {
		private SurfaceHolder surfaceHolder;

		private Handler theHandler;
		
		private float randX, randY;
		
		private int randReward,randomNumber;
		
		public GameThread(SurfaceHolder holder, Context context, Handler handler) {
			Log.e(this.toString(), "gameThread constructor");
			surfaceHolder = holder;
			theHandler = handler;
			setName("DrawingThread");

			/*
			 * Want to initialize all the paints and stuff here This will only
			 * be run when the thread is created
			 */

		}

		
		
		public void setGameRunning(boolean running) {
			Log.e(this.toString(), "thread setActive "+ running);
			GAME_RUNNING = running;
		}

		public void setStarted(boolean started) {
			Log.e(this.toString(), "thread setStarted");
			threadStarted = started;
		}

		public void setGameActive(boolean active) {
			Log.e(this.toString(), "thread setActive "+ active);
			GAME_ACTIVE= active;
		}
		
		@Override
		public void run() {
			//Log.e("thread", "running");

			
			while(GAME_ACTIVE){
				while (GAME_RUNNING) {
					Canvas c = null;
					try {
						c = surfaceHolder.lockCanvas(null);
						synchronized (surfaceHolder) {
							
							generateEnemies(DIFFICULTY_LEVEL);
							updateMotion();	
							drawAllTheThings(c);
							cleanUpGameElements();

						}
					} finally {
						if (c != null) {
							surfaceHolder.unlockCanvasAndPost(c);
						}
					}// end finally
					
					if(GAME_OVER){
						break;
					}
				}// end while GAME_RUNNING
				
				//Log.e(this.toString(), "Game no longer running");
				if(GAME_OVER){
					endGame();
				}	
			}
			
			
		}// end run
		
		
		public Bundle saveState(Bundle map) {
			/*
			 * save all of the variables that we care about... level of
			 * difficulty, positions of all present game elements, etc.
			 */
			return map;
		}

		
		public void generateEnemies(int DIFFICULTY_LEVEL){
			//generate random enemy elements based upon difficulty
			
			
			
			
				timeElapsed = ((System.nanoTime() - initialTime)/1000000000);
				randX = (float)(Math.random()*screenWidth); //between 0 and screen width
				randY = Math.random() < 0.5 ? 0:screenHeight; //either 0 or screen length
				
				switch(DIFFICULTY_LEVEL){
				case(1):
					if(timeElapsed > enemyFrequency){
						
						//5% +life enemy, %5 +shield, 5% +bomb
						randomNumber = (int)(Math.random()*20);
						if(randomNumber <= 1){
							randReward = 1;
						}else if(randomNumber <= 2){
							randReward = 2;
						}else if(randomNumber <= 3){
							randReward = 3;
						}else{
							randReward = 0;
						}
						//randReward = (int)(Math.random()*4);	//CHANGE, pick a weighted random reward...
						int rewardValue = 5; //assign appropriate value
						
						Enemy enemy = new Enemy(randX, randY, enemyColor, enemyVelocity, true, enemyRadius, randX, randY, (int)centerX, centerY, randReward, rewardValue, enemyPaint,255);
						activeEnemies.add(enemy);
						
						//30% of the time add a second, 10% of the time add 3
						if(randomNumber < 1){
							
							Log.e("mulitple enemies", "make 3 enemies");
							randX = (float)(Math.random()*screenWidth); 
							randY = Math.random() < 0.5 ? 0:screenHeight; 
							
							randomNumber = (int)(Math.random()*20);
							if(randomNumber <= 1){
								randReward = 1;
							}else if(randomNumber <= 2){
								randReward = 2;
							}else if(randomNumber <= 3){
								randReward = 3;
							}else{
								randReward = 0;
							}
							
							Enemy enemy1 = new Enemy(randX, randY, enemyColor, enemyVelocity, true, enemyRadius, randX, randY, (int)centerX, centerY, randReward, rewardValue, enemyPaint,255);
							activeEnemies.add(enemy1);
							
							
							
							randX = (float)(Math.random()*screenWidth); 
							randY = Math.random() < 0.5 ? 0:screenHeight; 
							
							randomNumber = (int)(Math.random()*20);
							if(randomNumber <= 1){
								randReward = 1;
							}else if(randomNumber <= 2){
								randReward = 2;
							}else if(randomNumber <= 3){
								randReward = 3;
							}else{
								randReward = 0;
							}
							
							Enemy enemy2 = new Enemy(randX, randY, enemyColor, enemyVelocity, true, enemyRadius, randX, randY, (int)centerX, centerY, randReward, rewardValue, enemyPaint,255);
							activeEnemies.add(enemy2);
							
						}else if (randomNumber < 3){
							
							Log.e("multiple enemies", "make 2 enemies");
							
							randX = (float)(Math.random()*screenWidth); 
							randY = Math.random() < 0.5 ? 0:screenHeight; 
							
							randomNumber = (int)(Math.random()*10);
							if(randomNumber <= 1){
								randReward = 1;
							}else if(randomNumber <= 2){
								randReward = 2;
							}else if(randomNumber <= 3){
								randReward = 3;
							}else{
								randReward = 0;
							}
							
							Enemy enemy1 = new Enemy(randX, randY, enemyColor, enemyVelocity, true, enemyRadius, randX, randY, (int)centerX, centerY, randReward, rewardValue, enemyPaint,255);
							activeEnemies.add(enemy1);
						}
						
						initialTime = System.nanoTime();
					}
				
				
				
				//this does nothing now. use the threshold to increase the frequency and/or velocity of enemies every x points
				//every 20 enemies at 5pts each, increase velocity a bit
				if(score > scoreThreshold){
					Log.e("BEFORE SCORE THRESHOLD", "vel: "+enemyVelocity+" freq: "+enemyFrequency);
					enemyVelocity += Math.log10(enemyVelocity);
					enemyFrequency -= Math.log10(enemyFrequency);
					scoreThreshold += 100;
					Log.e("AFTER SCORE THRESHOLD", "vel: "+enemyVelocity+" freq: "+enemyFrequency);
				}
				
					break;
					
					
					
					
				case(2):
					if(timeElapsed > enemyFrequency){
						
						randReward = 1;	//CHANGE, pick a weighted random reward...
						int rewardValue = 5; //assign appropriate value
						Enemy enemy = new Enemy(randX, randY, enemyColor, enemyVelocity, true, enemyRadius, randX, randY, (int)centerX, centerY, randReward, rewardValue, enemyPaint,255);
						activeEnemies.add(enemy);
						initialTime = System.nanoTime();
					}
					break;
				case(3):
					if(timeElapsed > enemyFrequency){
					
						randReward = 1;	//CHANGE, pick a weighted random reward...
						int rewardValue = 2; //assign appropriate value
						Enemy enemy = new Enemy(randX, randY, enemyColor, enemyVelocity, true, enemyRadius, randX, randY, (int)centerX, centerY, randReward, rewardValue, enemyPaint,255);
						activeEnemies.add(enemy);
						initialTime = System.nanoTime();
					}
					break;
				
				
			}//end switch
			
			
			
			
		
		}//end enemy generation
		
		
		
		public void updateMotion() {

			// called each time draw is, updates all game elements based on
			// their velocity

			synchronized(activeShots){
				for (Shot shot : activeShots) {
					
					double angle = shot.calculateAngle(shot.startX, shot.startY, shot.endX, shot.endY);
					
					double xVel =  ((shotVelocity) * Math.sin(angle));
					double yVel = ((-shotVelocity) * Math.cos(angle));
					
					// update position
					shot.centerX += xVel; //shotVelocity * xChange;
					shot.centerY += yVel; //shotVelocity * yChange;

					if (shot.centerX + shotRadius > screenWidth
							|| shot.centerX + shotRadius < 0
							|| shot.centerY + shotRadius > screenHeight
							|| shot.centerY + shotRadius < 0) {
						
						//add to removal list
						removalList.add(shot);
						
						Log.e("OBJECT MGMT", "removed shot from activeShots");
					}
				}//END shot logic

			}
			
			
			
			
			
			synchronized(activeEnemies){
				for(Enemy enemy : activeEnemies){
					double angle = enemy.calculateAngle(enemy.startX, enemy.startY, centerX, centerY);
					
					double xVel =  ((enemyVelocity) * Math.sin(angle));
					double yVel = ((-enemyVelocity) * Math.cos(angle));
					
					// update position
					enemy.centerX += xVel; //shotVelocity * xChange;
					enemy.centerY += yVel; //shotVelocity * yChange;
					
					
					if(enemy.intersectsWithBaseCamp(basecamp)){
						Log.e("INTERSECTION", "Intersected enemy with basecamp");
						removalList.add(enemy);
						lifeAmt -= 10;
						Log.e("LIEF AMT", ""+lifeAmt);
						if(lifeAmt <= 0){
							GAME_OVER = true;
							Log.e("GAME OVER", "THE GAME IS OVER, ALL IS LOST!");
						}
						basecamp.setRadius(basecampRadius*(lifeAmt/100));
						
					}
					
					if(SHIELD_UP && enemy.intersectsWithShield(shield)){
						Log.e("INTERSECTION", "Intersected enemy with shield");
						//animate to bounce off later? for now, just disappear
						removalList.add(enemy);
						continue;
					}
					
					if(BOMB_DROPPED && enemy.intersectsWithBomb(bomb)){
						//maybe animate this later too?
						score += 5;
						removalList.add(enemy);
						continue;
					}
					
					
					for(Shot shot : activeShots){
						
						if(enemy.intersectsWithShot(shot)){
							Log.e("INTERSECTION", "shot an enemy");
							//removal list for now, use hashmap once things work
							removalList.add(enemy);
							removalList.add(shot);
							int reward = enemy.rewardType;
							switch(reward){
							case 0:
								score+=5;
								break;
							case 1:
								if(lifeAmt<100){
									lifeAmt += enemy.rewardValue;
									if(lifeAmt>100) lifeAmt = 100;
									basecamp.setRadius(basecampRadius*(lifeAmt/100));
								}
								score+=10;
								break;
							case 2:
								if(shieldAmt<100) shieldAmt += enemy.rewardValue;
								score+=10;
								break;
							case 3:
								if(bombAmt<100) bombAmt += enemy.rewardValue;
								score+=10;
								break;
							default:
								score+=5;
								break;
							}
							
						}
						
						
						
					}//end foreach shot
				}//end foreach enemy
			}
			
			
			
			if(SHIELD_UP){
				shield.rotate(shieldVelocity);
			}
			
			if(BOMB_DROPPED){
				bomb.incrementRadius(bombVelocity);
				
				if(bomb.radius > Math.sqrt(centerX*centerX + centerY*centerY)){
					bomb.visible = false;
					bomb.setRadius(bombRadius);
					
					BOMB_DROPPED = false;
				}
				
			}

		}
		
		
		public void drawAllTheThings(Canvas canvas) {
			// background
			canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

			
			
			basecamp.draw(canvas);
			
			//canvas.drawCircle(centerX, centerY, gestureFieldRadius, fieldMarker);

			// set score text
			canvas.drawText(score + "", centerX, screenHeight / 8, scoreText);
			//Log.e("SCORE", ""+score);
			// draw stats
			canvas.drawLine(0, 0, screenWidth * (shieldAmt/100) , 0, shieldBar);
			canvas.drawLine(0, screenHeight , screenWidth * (bombAmt/100), screenHeight, bombBar);
			
			//Log.e("on draw", "called onDraw()");
			
			if(SHIELD_UP){
				if(shieldAmt <= 0){
					SHIELD_UP = false;
				}else{
					shieldTimeElapsed = (int)((System.nanoTime() - shieldInitialTime)/1000000000);
					
					if(shieldTimeElapsed < 10){
						shield.draw(canvas);
						shieldAmt -= .1;
						
					}else{
						SHIELD_UP = false;
					}
				}
				
				
				
				//keep track of time shield bar and time out.
				//maybe double tap to turn off shield?
			}
			
			
			synchronized(activeShots){
				for (Shot shot : activeShots) {
					shot.draw(canvas);
				}
			}
			
			synchronized(activeEnemies){
				for(Enemy enemy: activeEnemies){
					enemy.draw(canvas);
				}
			}
		
			
			
			if(BOMB_DROPPED && bomb.visible){
				if(bombAmt<=0){
					BOMB_DROPPED = false;
					bomb.setRadius(bombRadius);
				}else{
					bomb.draw(canvas);
					bombAmt -= .5;
				}
					
			}
			

		}
		
		
		public void drawEndGame(Canvas c){
			//display end game text
		}
		
		
		public void cleanUpGameElements(){
			for(GameElement item : removalList){
				
				if(item instanceof Shot)
					activeShots.remove(item);
				if(item instanceof Enemy)
					activeEnemies.remove(item);
			}
			
		}

	}// end game thread

}
