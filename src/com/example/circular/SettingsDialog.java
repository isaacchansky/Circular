package com.example.circular;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingsDialog extends DialogFragment{

	public interface notifyTheThread{
		public void setGamePaused(boolean t);
	}
	
	public static SettingsDialog newInstance(){
		SettingsDialog s = new SettingsDialog();
		Bundle args = new Bundle();
		s.setArguments(args);
		
		return s;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light);
		((Circular) getActivity()).setGamePaused(true);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.settings, container, false);
		
		TextView settingsText = (TextView) v.findViewById(R.id.settingsTextView);
		settingsText.setTypeface(Circular.code);
		return v;
	}

	
	@Override
	public void onPause(){
		super.onPause();
		
	}
	
	
	@Override
	public void onDismiss(DialogInterface dialog){
		super.onDismiss(dialog);
		((Circular) getActivity()).setGamePaused(false);
	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		
		if (getDialog() == null) return;
		
		//set animations
		getDialog().getWindow().setWindowAnimations(R.style.dialog_slide_animation_from_right);
	}
	
	
	
}


