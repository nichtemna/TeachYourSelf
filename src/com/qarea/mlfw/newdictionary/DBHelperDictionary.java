package com.qarea.mlfw.newdictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.qarea.mlfw.newdictionary.NewDictionary.DBTranslate;
import com.qarea.mlfw.newdictionary.NewDictionary.DBType;
import com.qarea.mlfw.newdictionary.NewDictionary.DBWords;

public class DBHelperDictionary extends SQLiteOpenHelper {

	private static final int version = 110;

	public DBHelperDictionary(Context context, String name) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE " + DBWords.TABLE_NAME + "(" + DBWords._ID
				+ " INTEGER PRIMARY KEY ," + DBWords.WORD_NAME
				+ " TEXT NOT NULL" +
				/* DBWords.ADDICTIONAL+" TEXT */");");

		db.execSQL("CREATE TABLE " + DBType.TABLE_NAME + "(" + DBType._ID
				+ " INTEGER PRIMARY KEY," + DBType.TYPE_NAME
				+ " TEXT NOT NULL);");

		db.execSQL("CREATE TABLE " + DBTranslate.TABLE_NAME + "("
				+ DBTranslate.ID_WORD + " INTEGER," + DBTranslate.ID_TYPE
				+ " INTEGER," + DBTranslate.TRANSLATES + " TEXT,"
				+ "PRIMARY KEY(" + DBTranslate.ID_WORD + ","
				+ DBTranslate.ID_TYPE + ")," + "FOREIGN KEY("
				+ DBTranslate.ID_WORD + ") REFERENCES " + DBWords.TABLE_NAME
				+ "(" + DBWords._ID + ")," + "FOREIGN KEY("
				+ DBTranslate.ID_TYPE + ") REFERENCES " + DBType.TABLE_NAME
				+ "(" + DBType._ID + "));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + DBWords.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBType.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + DBTranslate.TABLE_NAME);
		onCreate(db);
	}

}