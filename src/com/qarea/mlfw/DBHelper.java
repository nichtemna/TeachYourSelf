package com.qarea.mlfw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WordsTeacher";
    private static final int DATABASE_VERSION = 22;
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String UNIQUE = "UNIQUE ON CONFLICT ABORT";

    public enum DICTIONARY_TYPE {
        ASSET, CUSTOM, INSERTED
    }

    private static final String CREATE_TABLE_WORDS = CREATE_TABLE + DBWords.TABLE_NAME + " ("
                    + DBWords._ID + " INTEGER PRIMARY KEY," + DBWords.DICTIONARY_ID + " INTEGER ,"
                    + DBWords.WORD_NAME + " TEXT);";

    private static final String CREATE_TABLE_TYPES = CREATE_TABLE + DBTypes.TABLE_NAME + " ("
                    + DBTypes._ID + " INTEGER PRIMARY KEY," + DBTypes.DICTIONARY_ID + " INTEGER ,"
                    + DBTypes.TYPE_NAME + " TEXT);";

    private static final String CREATE_TABLE_DICTIONARY = CREATE_TABLE + DBDictionary.TABLE_NAME
                    + " (" + DBDictionary._ID + " INTEGER PRIMARY KEY," + DBDictionary.FILE
                    + " TEXT, " + DBDictionary.NAME + " TEXT " + UNIQUE + ","
                    + DBDictionary.ENABLED + " BOOLEAN, " + DBDictionary.DICT_TYPE + " TEXT, "
                    + DBDictionary.SELECTED + " BOOLEAN);";

    private static final String CREATE_TABLE_STATISTIC = CREATE_TABLE + DBStatistic.TABLE_NAME
                    + " (" + DBStatistic._ID + " INTEGER PRIMARY KEY," + DBStatistic.WORD
                    + " INTEGER , " + DBStatistic.DATE + " DATE," + DBStatistic.RESULT
                    + " BOOLEAN);";

    private static final String CREATE_TABLE_SCHEDULE = CREATE_TABLE + DBSchedule.TABLE_NAME + " ("
                    + DBSchedule._ID + " INTEGER PRIMARY KEY," + DBSchedule.WORD + " INTEGER , "
                    + DBSchedule.DATE + " DATE);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.onCreate(this.getReadableDatabase());
    }

    public static interface DBWords extends BaseColumns {
        String TABLE_NAME = "words";
        String DICTIONARY_ID = "dictionary_id";
        String WORD_NAME = "word";
    }

    public static interface DBTypes extends BaseColumns {
        String TABLE_NAME = "types";
        String DICTIONARY_ID = "dictionary_id";
        String TYPE_NAME = "type";
    }

    public static interface DBDictionary extends BaseColumns {
        String TABLE_NAME = "dictionary";
        String FILE = "file";
        String NAME = "name";
        String ENABLED = "enabled";
        String SELECTED = "selected";
        String DICT_TYPE = "dictionary_type";// from assets, created or inserted
    }

    public static interface DBStatistic extends BaseColumns {
        String TABLE_NAME = "statistic";
        String WORD = "word_id";
        String DATE = "date";
        String RESULT = "result";
    }

    public static interface DBSchedule extends BaseColumns {
        String TABLE_NAME = "schedule";
        String WORD = "word_id";
        String DATE = "date";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TYPES);
        db.execSQL(CREATE_TABLE_WORDS);
        db.execSQL(CREATE_TABLE_DICTIONARY);
        db.execSQL(CREATE_TABLE_STATISTIC);
        db.execSQL(CREATE_TABLE_SCHEDULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersoin, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBWords.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBTypes.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBDictionary.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBStatistic.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBSchedule.TABLE_NAME);
        onCreate(db);
    }

}
