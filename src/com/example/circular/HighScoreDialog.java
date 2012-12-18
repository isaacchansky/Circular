package com.example.circular;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HighScoreDialog extends DialogFragment{
	
	
	public static HighScoreDialog newInstance(String rank, String names,String scores){
		HighScoreDialog h = new HighScoreDialog();
		Bundle args = new Bundle();
		args.putString("ranks", rank);
		args.putString("names", names);
		args.putString("scores", scores);
		h.setArguments(args);
		return h;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.highscores, container,false);
		
		//Typeface nexa_bold = ((StartScreen)getActivity()).nexa_bold;
		//Typeface nexa_light = ((StartScreen)getActivity()).nexa_light;
		
		TextView highscoreText = (TextView) v.findViewById(R.id.highscoresTextView);
		highscoreText.setTypeface(((StartScreen)getActivity()).code);
		
		//populate three textviews with rank, name, score
		
		TextView ranks = (TextView) v.findViewById(R.id.rankTextView);
		ranks.setTypeface(Circular.nexa_bold);
		ranks.setText(getArguments().getString("ranks"));
		TextView names = (TextView) v.findViewById(R.id.nameTextView);
		names.setText(getArguments().getString("names"));
		names.setTypeface(Circular.nexa_bold);
		TextView scores = (TextView) v.findViewById(R.id.scoreTextView);
		scores.setText(getArguments().getString("scores"));
		scores.setTypeface(Circular.nexa_bold);
		
		Button clearDB = (Button) v.findViewById(R.id.clearDBbutton);
		clearDB.setText("clear Database");
		clearDB.setTypeface(Circular.nexa_light);
		clearDB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((StartScreen)getActivity()).database.open();
				((StartScreen)getActivity()).database.clearDB();
				((StartScreen)getActivity()).database.close();
				
			}
			
		});
		
		
		
		return v;
	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		
		if (getDialog() == null) return;
		
		//set animations
		getDialog().getWindow().setWindowAnimations(R.style.dialog_slide_animation_from_left);
	}
	
	
}
