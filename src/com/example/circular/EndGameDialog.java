package com.example.circular;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EndGameDialog extends DialogFragment{

	
	
	public interface gameOverDialog{
		public void restartGame();
	}
	
	private String NAME;
	private int SCORE;
	
	public static EndGameDialog newInstance(int score, String name){
		Log.e("endgamedialog", "new endGameDialog instance");
		EndGameDialog e = new EndGameDialog();
		Bundle args = new Bundle();
		args.putInt("score", score);
		args.putString("name", name);
		e.setArguments(args);
		return e;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setStyle(EndGameDialog.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light);
		
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.gameover, container, false);
		
		TextView gameover = (TextView) v.findViewById(R.id.endGameText);
		gameover.setTypeface(((Circular)getActivity()).code);
		
		Button restart = (Button) v.findViewById(R.id.restartButton);
		restart.setTypeface(((Circular)getActivity()).nexa_light);
		restart.setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View v) {
				((Circular)getActivity()).restartGame();	
			}	
		});
		
		SCORE = getArguments().getInt("score");
		NAME = getArguments().getString("name");
		
		Button toStart = (Button) v.findViewById(R.id.backToHomeButton);
		toStart.setTypeface(((Circular)getActivity()).nexa_light);
		toStart.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View v) {
				((Circular)getActivity()).stopGame();
			}
			
		});
		
		TextView youscored = (TextView) v.findViewById(R.id.youScoredText);
		youscored.setText(NAME+", you scored");
		youscored.setTypeface(((Circular)getActivity()).nexa_light);
		
		TextView score = (TextView) v.findViewById(R.id.scoreText);
		score.setText(SCORE+"");
		score.setTypeface(((Circular)getActivity()).nexa_bold);
		
		return v;
		
	}

	
	
	
	public void dismissFragment(){
		this.dismiss();
	}
	
	
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);
		
	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		
		if (getDialog() == null) return;
		
		//set animations
		getDialog().getWindow().setWindowAnimations(R.style.dialog_slide_animation_from_top);
	}
	
	
}
