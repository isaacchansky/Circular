package com.example.circular;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HighScoreData {

	private static final String DATABASE_NAME = "HighScores";
	private static final String PLAYER_NAME = "name";
	private static final String PLAYER_SCORE = "score";
	private static final String NAME_FIELD = "name TEXT";
	private static final String SCORE_FIELD = "score REAL";
	
	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase database;
	
	public HighScoreData(Context context){
		this.dbOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
	}
	
	public void open(){
		this.database = this.dbOpenHelper.getWritableDatabase();
	}
	
	public void close(){
		if(this.database!=null){
			this.database.close();
		}
	}
	
	public void insertScore(String name, int score){
		ContentValues newScore = new ContentValues();
		newScore.put(PLAYER_NAME, name);
		newScore.put(PLAYER_SCORE, score);
		
		this.open();
		this.database.insert(DATABASE_NAME, null, newScore);
		this.close();
	}
	
	public Cursor getTopScores(){
		Cursor result = database.query(DATABASE_NAME, null, null, null, null, null, "score DESC");
		return result;
	}
	
	public void clearDB(){
		database.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
		database.execSQL("CREATE TABLE "+DATABASE_NAME
				+"( _id integer primary key autoincrement, "
				+NAME_FIELD+", "+SCORE_FIELD+");");
	}
	
	/*
	 * db helper 
	 * =======================================================
	 */
	
	
	private class DatabaseOpenHelper extends SQLiteOpenHelper{
		
		private static final String DB_CREATE_QUERY = "CREATE TABLE "+DATABASE_NAME
				+"( _id integer primary key autoincrement, "
				+NAME_FIELD+", "+SCORE_FIELD+");";
		
		public DatabaseOpenHelper(Context context, String name, CursorFactory factory, int version){
			super(context, name, factory, version);
			
		}
		
		@Override
		public void onCreate(SQLiteDatabase db){
			db.execSQL(DB_CREATE_QUERY);
		}
		
		//onUpgrade used to update database to newer version... creates new db
		@Override 
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			Log.w(DatabaseOpenHelper.class.getName(), "Upgrading db from version "+oldVersion+" to "+newVersion+", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
			onCreate(db);
		}
	}
}
