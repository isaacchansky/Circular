package com.example.circular;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/*
 * Base activity class...
 */
public class Circular extends FragmentActivity implements  PausedDialog.notifyTheThread, SettingsDialog.notifyTheThread, GameView.gameOverDialog {
	private GameView gameView;
	
	public static Typeface code;

	public static Typeface nexa_light;

	public static Typeface nexa_bold;
	
	public String PLAYER_NAME;
	
	public HighScoreData database;
	
	DialogFragment settingsDialogFragment, pausedDialogFragment, gameOverDialogFragment;
	
	
	@Override
	public void onStart(){
		super.onStart();
		
		if (getWindow() == null) return;
		
		//set animations
		getWindow().setWindowAnimations(R.style.dialog_slide_animation_from_right);
	}
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        Log.e(this.toString(), "called onCreate()");
        gameView = (GameView) findViewById(R.id.gameview);
        
        settingsDialogFragment = SettingsDialog.newInstance();
        pausedDialogFragment = PausedDialog.newInstance();
        
        //set up fonts
        code = Typeface.createFromAsset(getAssets(), "fonts/CODE_bold.otf");
        nexa_light = Typeface.createFromAsset(getAssets(), "fonts/NEXA_light.otf");
        nexa_bold = Typeface.createFromAsset(getAssets(), "fonts/NEXA_bold.otf");
        
        database = new HighScoreData(this);
        
        Intent intent = getIntent();
        PLAYER_NAME = intent.getStringExtra("name");
        
    }//end onCreate

    
	@Override
	public void onPause(){
		super.onPause();
		Log.e(this.toString(), "called onPause()");	
		setGamePaused(true);
		
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		//need a way to check if this is our first time starting up, or we've been paused... savedInstanceState?
		Log.e(this.toString(), "called onResume()");
		
		/*if(gameView.threadStarted() && !gameView.threadActive())	setGamePaused(false);
		if(gameView.hasBeenPutAside){
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			pausedDialogFragment.show(ft, "paused");
		}*/
		
	}
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.e(this.toString(), "called onDestroy()");
		//release resources
		
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_MENU){
			
			 FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	         //ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	        // ft.add(R.id.settingsFragment, settingsFragment);
	         settingsDialogFragment.show(ft, "settings");
	         return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//exit dialog
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			pausedDialogFragment.show(ft, "paused");
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	public void setGamePaused(boolean t) {
		if(t){
			gameView.pauseGame();
		}else{
			gameView.unpauseGame();
		}
	}
	


	public void stopGame() {
		//called when 'quit to start screen' is pressed
		gameView.stopGame();
		Intent intent = new Intent(this, StartScreen.class);
		//next line is supposed to keep the 'back' button pointing to the home
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(intent);
	}
    
	
	public void restartGame(){
		Intent intent = new Intent(this, Circular.class);
		startActivity(intent);
	}
	
    
	 @Override
	 public void onSaveInstanceState(Bundle savedInstanceState) {
	     super.onSaveInstanceState(savedInstanceState);
	     Log.e(this.toString(), "called onSavedInstanceState");
	     // save stuff
	 }

	 @Override
	 public void onRestoreInstanceState(Bundle savedInstanceState) {
	     super.onRestoreInstanceState(savedInstanceState);
	     // restore stuff
	     Log.e(this.toString(), "called onRestoreInstanceState");
	 }

	 
	 public GameView getGameInstance(){
		 return this.gameView;
	 }


	@Override
	public void bringUpEndGameDialog() {
		
		writeScoreToDB(PLAYER_NAME, getGameInstance().getScore());
		
		Log.e(this.toString(), "called bringUpEndGameDialog() with score "+getGameInstance().getScore());
		//exit dialog
		gameOverDialogFragment = EndGameDialog.newInstance(getGameInstance().getScore(), PLAYER_NAME);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		gameOverDialogFragment.show(ft, "gameover");
		
	}

	public void writeScoreToDB(String name, int score){
		database.open();
		database.insertScore(name, score);
		database.close();
	}
    
	public Typeface nexa_light(){
		return nexa_light;
	}
	
	public Typeface nexa_bold(){
		return nexa_bold;
	}
	
	public Typeface code_typeface(){
		return code;
	}
}
