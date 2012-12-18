package com.example.circular;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartScreen extends FragmentActivity {

	public Typeface code, nexa_light, nexa_bold;
	public HighScoreData database;
	DialogFragment highScoresDialogFragment;
	public String highscoreNames;
	public String highscoreValues;
	public String rank;
	public String PLAYER_NAME;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        
        //set up database
        database = new HighScoreData(this);
        
        
        //set up fonts
        code = Typeface.createFromAsset(getAssets(), "fonts/CODE_bold.otf");
        nexa_light = Typeface.createFromAsset(getAssets(), "fonts/NEXA_light.otf");
        nexa_bold = Typeface.createFromAsset(getAssets(), "fonts/NEXA_bold.otf");
        
        TextView title = (TextView)findViewById(R.id.title);
        title.setTypeface(code);
        
        final SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        
        final EditText name = (EditText) findViewById(R.id.nameEditText);
        name.setTypeface(nexa_light);
        String playername = prefs.getString("name", "");
        if(playername.length()!=0){
        	PLAYER_NAME = playername;
        	name.setText(PLAYER_NAME);
        }
        
        final TextView warning = (TextView)findViewById(R.id.warningText);
        warning.setTypeface(nexa_bold);
        
        
        Button startGame = (Button) findViewById(R.id.playButton);
        startGame.setTypeface(nexa_light);
        startGame.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				PLAYER_NAME = name.getText().toString().toUpperCase();
				if(PLAYER_NAME.length()==0){
					warning.setText("enter a name, please!");
				}else{
					warning.setText("");
					Log.e(this.toString(), "player name is "+PLAYER_NAME);
					
					
					SharedPreferences.Editor e = prefs.edit();
					e.putString("name", PLAYER_NAME);
					e.commit();
					
					Intent intent = new Intent(StartScreen.this, Circular.class);
					intent.putExtra("name", PLAYER_NAME);
			        startActivity(intent);
				}
				
				
			}
        	
        });   
        
        
        
        Button viewHighScore = (Button) findViewById(R.id.highScoreButton);
        viewHighScore.setTypeface(nexa_light);
        viewHighScore.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				rank = "";
				highscoreNames="";
				highscoreValues="";
				database.open();
				Cursor cursor = database.getTopScores();
				int rankcounter = 1;
				while(cursor.moveToNext()){
					rank += rankcounter+"\n";
					highscoreNames +=  cursor.getString(1)+"\n";
					highscoreValues += cursor.getString(2)+"\n";
					rankcounter ++;
				}
				
				database.close();
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				highScoresDialogFragment = HighScoreDialog.newInstance(rank, highscoreNames, highscoreValues);
				highScoresDialogFragment.show(ft, "highScores");
				
			}
        	
        });   
    }


    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.start_screen, menu);
        
        return true;
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // your stuff or nothing
        
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // your stuff or nothing
    }

    
    @Override
    public void onPause(){
    	super.onPause();
    	
    }
    
    @Override
	public void onStart(){
		super.onStart();
		
		if (getWindow() == null) return;
		
		//set animations
		getWindow().setWindowAnimations(R.style.dialog_slide_animation_from_left);
	}
    
    
    public HighScoreData getDatabase(){
    	return database;
    }

}
