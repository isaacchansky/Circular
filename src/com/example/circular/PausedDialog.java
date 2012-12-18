package com.example.circular;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PausedDialog extends DialogFragment {

	public interface notifyTheThread{
		public void setGamePaused(boolean t);
		public void stopGame();
	}
	
	
	
	public static PausedDialog newInstance(){
		PausedDialog p = new PausedDialog();
		Bundle args = new Bundle();
		p.setArguments(args);
		
		return p;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		((Circular) getActivity()).setGamePaused(true);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.paused, container, false);
		
		TextView pausedText = (TextView)v.findViewById(R.id.pausedText);
		pausedText.setTypeface(Circular.code);
		
		Button cancel = (Button) v.findViewById(R.id.backToGameButton);
		cancel.setTypeface(Circular.nexa_light);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismissFragment();
			}
			
		});
		
		
		Button exitToStart = (Button) v.findViewById(R.id.exitButton);
		exitToStart.setTypeface(Circular.nexa_light);
		exitToStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Circular)getActivity()).stopGame();
				
			    
			}
			
		});
		return v;
	}
	
	
	
	
	public void dismissFragment(){
		this.dismiss();
	}
	
	
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);
		
		try {
			((Circular) getActivity()).setGamePaused(false);
		} catch (Exception e) {
			//do nothing
		}
		

	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		
		if (getDialog() == null) return;
		
		//set animations
		getDialog().getWindow().setWindowAnimations(R.style.dialog_slide_animation_from_right);
	}
	
	
}
