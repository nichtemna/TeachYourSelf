package com.qarea.mlfw.newdictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

import com.qarea.mlfw.DBHelper.DICTIONARY_TYPE;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.util.SelectedDictionary;

public class NewDictionary {

	private Context context;
	private DBHelperDictionary sqlHelper;
	private DBDictionaryTable sqlDictionaryTableHelper;
	private SQLiteDatabase sqlDatabase;
	private String databaseName;
	private static NewDictionary instance;
	private int dictionaryId;

	public static interface DBWords extends BaseColumns {
		String TABLE_NAME = "words";
		String WORD_NAME = "word";
		String ADDICTIONAL = "addictional";
	}

	public static interface DBType extends BaseColumns {
		String TABLE_NAME = "type";
		String TYPE_NAME = "type_name";
	}

	public static interface DBTranslate extends BaseColumns {
		String TABLE_NAME = "translate";
		String ID_WORD = "id_word";
		String ID_TYPE = "id_type";
		String TRANSLATES = "translate";
	}

	public static interface DBDictionary extends BaseColumns {
		String TABLE_NAME = "dictionary";
		String FILE = "file";
		String NAME = "name";
	}

	private class DBDictionaryTable extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "DatabaseDictionarys";
		private static final int VERSION = 8;

		public DBDictionaryTable(Context context) {
			super(context, DATABASE_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DBDictionary.TABLE_NAME + " ("
					+ DBDictionary._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ DBDictionary.FILE + " TEXT NOT NULL," + DBDictionary.NAME
					+ " TEXT NOT NULL );");

			// db.execSQL("CREATE_TABLE" + DBType.TABLE_NAME + " (" + DBType._ID
			// + " INTEGER PRIMARY KEY," + DBType.DICTIONARY_NAME
			// + " INTEGER ," + DBType.TYPE_NAME + " TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DBDictionary.TABLE_NAME);
			// db.execSQL("DROP TABLE IF EXISTS " + DBType.TABLE_NAME);
			onCreate(db);
		}
	}

	public NewDictionary(Context context) {
		this.context = context;
		this.dictionaryId = SelectedDictionary.getDictionaryID();
		if (dictionaryId != -1) {
			databaseName = LocalDataProvider.getInstance(context)
					.getFileNameDictionary(dictionaryId);
		}
	}

	public static NewDictionary getInstance(Context context) {

		if (instance == null)
			instance = new NewDictionary(context);

		return instance;
	}

	public void fillBase(Context context) {
		this.context = context;
		this.dictionaryId = SelectedDictionary.getDictionaryID();
		if (dictionaryId != -1) {
			databaseName = LocalDataProvider.getInstance(context)
					.getFileNameDictionary(dictionaryId);
			testToFilling();
		}
	}

	public void createNewDict(Context context) {
		this.context = context;
		this.dictionaryId = SelectedDictionary.getDictionaryID();
		if (dictionaryId != -1) {
			databaseName = LocalDataProvider.getInstance(context)
					.getFileNameDictionary(dictionaryId);
			LocalDataProvider.getInstance(context);
		}
	}

	// if true DBDictionaryTable if DBHelper
	private NewDictionary openDictionarys(boolean switcher) throws SQLException {
		if (switcher) {
			sqlDictionaryTableHelper = new DBDictionaryTable(context);
			sqlDatabase = sqlDictionaryTableHelper.getWritableDatabase();
		} else {
			this.dictionaryId = SelectedDictionary.getDictionaryID();
			databaseName = LocalDataProvider.getInstance(context)
					.getFileNameDictionary(dictionaryId);
			if (databaseName.contains("/")) {
				databaseName = databaseName.substring(databaseName
						.lastIndexOf("/") + 1);
				if (databaseName.contains(".")) {
					databaseName = databaseName.substring(0,
							databaseName.indexOf("."));
				}
			}
			sqlHelper = new DBHelperDictionary(context, databaseName);
			sqlDatabase = sqlHelper.getWritableDatabase();
		}
		return this;
	}

	private void close() throws SQLException {
		sqlDatabase.close();
	}

	// ---------------------------------------------------------------------
	public int getDictionaryId() {
		// this.dictionaryId = SelectedDictionary.getDictionaryID();
		return this.dictionaryId;
	}

	public ArrayList<String> getAllWords() {
		openDictionarys(false);
		ArrayList<String> rez = new ArrayList<String>();
		String[] columns = { DBWords.WORD_NAME };
		Cursor cursor = null;
		try {
			cursor = sqlDatabase.query(DBWords.TABLE_NAME, columns, null, null,
					null, null, null);
			if (cursor.moveToFirst()) {
				do {
					rez.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			String error = e.toString();
			error = error + "";
		} finally {
			close();
		}
		return rez;
	}

	public ArrayList<String> getAllDictionaryInDatabase() {
		openDictionarys(true);
		String[] columns = new String[] { DBDictionary.NAME };
		Cursor cursor = sqlDatabase.query(DBDictionary.TABLE_NAME, columns,
				null, null, null, null, null);
		ArrayList<String> dictionarys = new ArrayList<String>();
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); cursor.isAfterLast(); cursor
					.moveToNext()) {
				dictionarys.add(cursor.getString(0));
			}
		}
		cursor.close();
		close();
		return dictionarys;
	}

	public HashSet<String> getAllWords(String checkStr) {
		openDictionarys(false);
		String[] columns = { DBWords.WORD_NAME };
		String where = DBWords.WORD_NAME + " like '" + checkStr + "%'";
		Cursor cursor = null;
		try {
			cursor = sqlDatabase.query(DBWords.TABLE_NAME, columns, where,
					null, null, null, null);
		} catch (Exception e) {
			String error = e.toString();
			error = error + "";
		}
		HashSet<String> returnWords = new HashSet<String>();
		// cursor.moveToFirst();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			returnWords.add(cursor.getString(0));
		}
		cursor.close();
		close();
		return returnWords;
	}

	public ArrayList<String> getWordsTypes() {
		openDictionarys(false);
		ArrayList<String> types = new ArrayList<String>();
		Cursor cursor = null;
		cursor = sqlDatabase.rawQuery("SELECT " + DBType.TYPE_NAME + " FROM "
				+ DBType.TABLE_NAME + ";", null);
		cursor.close();
		close();
		return types;
	}

	public boolean containWord(String word) {
		openDictionarys(false);
		String[] columns = { DBWords.WORD_NAME };
		String where = DBWords.WORD_NAME + "='" + word + "'";
		Cursor cursor = sqlDatabase.query(DBWords.TABLE_NAME, columns, where,
				null, null, null, null);

		if (cursor.getCount() != 0) {
			cursor.close();
			close();
			return true;
		}
		cursor.close();
		close();
		return false;
	}

	public void setDictionaryName() {
		this.databaseName = LocalDataProvider
				.getDictionaryNameById(dictionaryId);
		testToFilling();

	}

	// if no word return null, return type and translate in one ArrayList
	public String findTranslate(String word) {
		openDictionarys(false);
		String[] columns = { DBWords._ID };
		String where = DBWords.WORD_NAME + "='" + word + "'";

		Cursor cursor = sqlDatabase.query(DBWords.TABLE_NAME, columns, where,
				null, null, null, null);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			int wordId = cursor.getInt(0);
			cursor.close();

			where = DBTranslate.ID_WORD + "=" + wordId + "";
			String[] columnsTranslate = { DBTranslate.ID_TYPE,
					DBTranslate.TRANSLATES };
			cursor = sqlDatabase.query(DBTranslate.TABLE_NAME,
					columnsTranslate, where, null, null, null, null);
			ArrayList<Integer> types = new ArrayList<Integer>();
			ArrayList<String> transalteList = new ArrayList<String>();
			StringBuilder translate = new StringBuilder();
			while (cursor.moveToNext()) {
				types.add(cursor.getInt(0));
				transalteList.add(cursor.getString(1));

			}
			cursor.close();
			for (int i = 0; i < types.size(); i++) {

				String[] columnsType = { DBType.TYPE_NAME };
				where = DBType._ID + "=" + types.get(i) + "";
				cursor = sqlDatabase.query(DBType.TABLE_NAME, columnsType,
						where, null, null, null, null);
				cursor.moveToFirst();
				translate.append(cursor.getString(0) + ": \r\n");
				translate.append(starToComma(transalteList.get(i)) + "; \r\n");
			}
			cursor.close();
			close();
			String translateInString = translate.toString();
			return translateInString;
		}
		cursor.close();
		close();

		return "";
	}

	// if no word return null, return type and translate in one ArrayList
	public String getRandomTranslation() {
		openDictionarys(false);
		Random randomGenerator = new Random();
		int wordId = randomGenerator.nextInt(getTranslateCount());
		if (wordId == 0) {
			wordId = 1;
		}
		String where = DBTranslate.ID_WORD + "=" + wordId + "";
		String[] columnsTranslate = { DBTranslate.ID_TYPE,
				DBTranslate.TRANSLATES };
		Cursor cursor = sqlDatabase.query(DBTranslate.TABLE_NAME,
				columnsTranslate, where, null, null, null, null);
		ArrayList<Integer> types = new ArrayList<Integer>();
		ArrayList<String> transalteList = new ArrayList<String>();
		StringBuilder translate = new StringBuilder();
		while (cursor.moveToNext()) {
			types.add(cursor.getInt(0));
			transalteList.add(cursor.getString(1));

		}
		cursor.close();
		for (int i = 0; i < types.size(); i++) {

			String[] columnsType = { DBType.TYPE_NAME };
			where = DBType._ID + "=" + types.get(i) + "";
			cursor = sqlDatabase.query(DBType.TABLE_NAME, columnsType, where,
					null, null, null, null);
			cursor.moveToFirst();
			translate.append(cursor.getString(0) + ": \r\n");
			translate.append(starToComma(transalteList.get(i)) + "; \r\n");
		}
		cursor.close();
		close();
		String translateInString = translate.toString();
		return translateInString;
	}

	public int getTranslateCount() {
		Cursor cursor = sqlDatabase.rawQuery(
				"SELECT COUNT(translate) FROM translate ", null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return 1;
	}

	@SuppressLint("NewApi")
	public HashMap<String, String> findAllTranslate() {
		openDictionarys(false);
		ArrayList<String> words = new ArrayList<String>();
		HashMap<String, String> map = new HashMap<String, String>();
		String[] columns = { DBWords.WORD_NAME };
		Cursor cursor = null;
		try {
			cursor = sqlDatabase.query(DBWords.TABLE_NAME, columns, null, null,
					null, null, null);
		} catch (Exception e) {
			String error = e.toString();
			error = error + "";
		}
		if (cursor.moveToFirst()) {
			do {
				words.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		for (int j = 0; j < words.size(); j++) {
			String query = "SELECT translate FROM words AS w, translate AS tr WHERE w.word = '"
					+ words.get(j).toString() + "' AND tr.id_word = w._id";
			Cursor c = sqlDatabase.rawQuery(query, null);

			if (c.moveToFirst()) {
				map.put(words.get(j), c.getString(0));
			}

		}

		close();
		return map;
	}

	public String[] findTranslateArray(String word) {
		openDictionarys(false);
		String[] result;
		String[] columns = { DBWords._ID };
		String where = DBWords.WORD_NAME + "='" + word + "'";

		Cursor cursor = sqlDatabase.query(DBWords.TABLE_NAME, columns, where,
				null, null, null, null);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			int wordId = cursor.getInt(0);
			cursor.close();

			where = DBTranslate.ID_WORD + "=" + wordId + "";
			String[] columnsTranslate = { DBTranslate.ID_TYPE,
					DBTranslate.TRANSLATES };
			cursor = sqlDatabase.query(DBTranslate.TABLE_NAME,
					columnsTranslate, where, null, null, null, null);
			ArrayList<Integer> types = new ArrayList<Integer>();
			ArrayList<String> transalteList = new ArrayList<String>();

			while (cursor.moveToNext()) {
				types.add(cursor.getInt(0));
				transalteList.add(cursor.getString(1));

			}
			cursor.close();
			result = new String[types.size()];
			for (int i = 0; i < types.size(); i++) {

				String[] columnsType = { DBType.TYPE_NAME };
				where = DBType._ID + "=" + types.get(i) + "";
				cursor = sqlDatabase.query(DBType.TABLE_NAME, columnsType,
						where, null, null, null, null);
				cursor.moveToFirst();
				result[i] = cursor.getString(0) + ":"
						+ starToComma(transalteList.get(i));
			}
			cursor.close();
			close();
			return result;
		} else {
			cursor.close();
			close();

			return null;
		}
	}

	// change * to ,
	private String starToComma(String string) {
		// TODO Auto-generated method stub
		int star = string.indexOf("*");
		while (star != -1) {
			string = string.subSequence(0, star) + ","
					+ string.subSequence(star + 1, string.length());
			star = string.indexOf("*", star + 1);
		}
		string = string.substring(0, string.length() - 1);
		return string;
	}

	public ArrayList<String> findAllOnlyTranslate(String word) {
		openDictionarys(false);
		Cursor cursor = sqlDatabase
				.rawQuery(
						"SELECT translate.translate FROM words INNER JOIN translate ON words._id = translate.id_word WHERE word LIKE '"
								+ word + "'", null);
		ArrayList<String> transalteList = new ArrayList<String>();

		String[] list;
		if (cursor.moveToFirst()) {
			do {
				list = cursor.getString(0).split("\\*");
				Log.d("tag", "cursor.getString(0) " + cursor.getString(0) + " "
						+ list.length);
				for (String string : list) {
					transalteList.add(string.trim());
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		close();
		return transalteList;
	}

	public int checkOfTheCorrectness(String word, String wordTranslate) {
		word = word.trim();
		ArrayList<String> translate = findAllOnlyTranslate(word);
		if (translate.size() > 0) {
			for (String str : translate) {
				if (str.equals(wordTranslate)) {
					return 1;
				}
			}
			return 0; // not correct
		} else {
			return 3; // no words in dictionary
		}
	}

	// if true dictionary was insert
	public void addDictionary(String fileName) {
		openDictionarys(true);
		String[] columns = { DBDictionary._ID };
		String where = DBDictionary.NAME + "='" + fileName + "'";
		Cursor cursor = sqlDatabase.query(DBDictionary.TABLE_NAME, columns,
				where, null, null, null, null);
		String file = LocalDataProvider.getDictionaryNameById(dictionaryId);
		if (cursor.getCount() != 0) {
			cursor.close();
			close();
			return;
		} else {
			cursor.close();
			ContentValues values = new ContentValues();
			values.put(DBDictionary.FILE, file);
			values.put(DBDictionary.NAME, fileName);
			sqlDatabase.insert(DBDictionary.TABLE_NAME, null, values);
			close();
		}
	}

	// if true dictionary was insert
	public void addDictionaryNew(String fileName) {
		openDictionarys(true);
		String[] columns = { DBDictionary._ID };
		String where = DBDictionary.NAME + "='" + fileName + "'";
		Cursor cursor = sqlDatabase.query(DBDictionary.TABLE_NAME, columns,
				where, null, null, null, null);

		if (cursor.getCount() != 0) {
			cursor.close();
			close();
			return;
		} else {
			cursor.close();
			ContentValues values = new ContentValues();
			values.put(DBDictionary.FILE, "file");
			values.put(DBDictionary.NAME, fileName);
			sqlDatabase.insert(DBDictionary.TABLE_NAME, null, values);
			close();
		}
	}

	public void addWordInDictionary(String word, String type, String translate) {
		if (translate == "") {
			return;
		}

		openDictionarys(false);

		String[] columnsWords = { DBWords._ID };
		String where = DBWords.WORD_NAME + "='" + word + "'";
		sqlDatabase.execSQL("INSERT INTO " + DBWords.TABLE_NAME + "("
				+ DBWords.WORD_NAME + ")" + " Select '" + word + "' "
				+ "WHERE NOT EXISTS (SELECT 1 FROM " + DBWords.TABLE_NAME + " "
				+ "WHERE " + DBWords.WORD_NAME + "='" + word + "' )");
		Cursor cursor = sqlDatabase.query(DBWords.TABLE_NAME, columnsWords,
				where, null, null, null, null);
		cursor.moveToFirst();
		int idWordSave = cursor.getInt(0);
		cursor.close();
		// wordSave = word;

		String[] columnsType = { DBType._ID };
		where = DBType.TYPE_NAME + "='" + type + "'";
		sqlDatabase.execSQL("INSERT INTO " + DBType.TABLE_NAME + "("
				+ DBType.TYPE_NAME + ")" + " Select '" + type + "' "
				+ "WHERE NOT EXISTS (SELECT 1 FROM " + DBType.TABLE_NAME + " "
				+ "WHERE " + DBType.TYPE_NAME + "='" + type + "' )");

		cursor = sqlDatabase.query(DBType.TABLE_NAME, columnsType, where, null,
				null, null, null);
		cursor.moveToFirst();
		int idTypeSave = cursor.getInt(0);
		cursor.close();

		ContentValues values = new ContentValues();
		values.put(DBTranslate.ID_WORD, idWordSave);
		values.put(DBTranslate.ID_TYPE, idTypeSave);
		values.put(DBTranslate.TRANSLATES, translate);
		sqlDatabase.insert(DBTranslate.TABLE_NAME, null, values);

		close();
	}

	/*
	 * Update words translation in dictionary
	 */
	public void updateWordInDictionary(String word, String type,
			String translate) {
		if (translate == "") {
			return;
		}

		openDictionarys(false);
		// get word id
		String[] columnsWords = { DBWords._ID };
		String where = DBWords.WORD_NAME + "='" + word + "'";
		Cursor cursor = sqlDatabase.query(DBWords.TABLE_NAME, columnsWords,
				where, null, null, null, null);
		cursor.moveToFirst();
		int idWordSave = cursor.getInt(0);
		cursor.close();
		// get type id
		String[] columnsType = { DBType._ID };
		where = DBType.TYPE_NAME + "='" + type + "'";
		cursor = sqlDatabase.query(DBType.TABLE_NAME, columnsType, where, null,
				null, null, null);
		cursor.moveToFirst();
		int idTypeSave = cursor.getInt(0);
		cursor.close();
		// delete old words translation
		where = DBTranslate.ID_TYPE + "=" + idTypeSave + " AND "
				+ DBTranslate.ID_WORD + "=" + idWordSave;
		// insert words translation in db
		where = DBTranslate.ID_TYPE + "=" + idTypeSave + "";
		ContentValues values = new ContentValues();
		values.put(DBTranslate.ID_WORD, idWordSave);
		values.put(DBTranslate.ID_TYPE, idTypeSave);
		values.put(DBTranslate.TRANSLATES, translate.replaceAll(",", "*") + "*");
		sqlDatabase.replace(DBTranslate.TABLE_NAME, null, values);
		close();
	}

	public String getTranslate(String word) {
		openDictionarys(false);
		String translation;
		Cursor cursor = sqlDatabase
				.rawQuery(
						"SELECT translate.translate FROM words INNER JOIN translate ON words._id = translate.id_word WHERE words.word  LIKE '"
								+ word + "'", null);

		if (cursor.moveToFirst()) {
			translation = cursor.getString(0);
		} else {
			translation = null;
		}
		close();
		return translation;
	}

	public String getType(String word) {
		openDictionarys(false);
		int id = 0;
		String type = "";
		String[] columns = { DBTranslate.ID_TYPE };
		String where = DBTranslate.TRANSLATES + "=?";
		Cursor cursor = sqlDatabase.query(DBTranslate.TABLE_NAME, columns,
				where, new String[] { word }, null, null, null, null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			cursor.moveToLast();
			id = cursor.getInt(0);
		}
		cursor.close();
		String[] column = { DBType.TYPE_NAME };
		where = DBType._ID + "=?";
		Cursor cursor2 = sqlDatabase.query(DBType.TABLE_NAME, column, where,
				new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor2.getCount() != 0) {
			cursor2.moveToFirst();
			cursor2.moveToLast();
			type = cursor2.getString(0);
		}
		close();
		return type;
	}

	public void testToFilling() {
		try {
			load();
			addDictionary(databaseName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/*
	 * Add dictionary to DB
	 */
	public void load() throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, Integer> type = new HashMap<String, Integer>();
		ArrayList<String> wordsTransalateInsert = new ArrayList<String>();
		String dictionaryName = LocalDataProvider
				.getDictionaryNameById(dictionaryId);
		String dictionaryType = LocalDataProvider
				.getDictionaryTypeById(dictionaryId);

		InputStream in;
		if (dictionaryType.equals(DICTIONARY_TYPE.ASSET.toString())) {
			AssetManager assetManager = context.getAssets();
			in = assetManager.open(dictionaryName);
		} else if (dictionaryType.equals(DICTIONARY_TYPE.INSERTED.toString())) {
			in = new FileInputStream(dictionaryName);
		} else {
			in = new FileInputStream(dictionaryName);
		}
		InputStreamReader fileStream = new InputStreamReader(in);
		BufferedReader readerStream = new BufferedReader(fileStream);

		String tmpString = "";
		int firstStar = 0;
		int wordIndex = 0;

		String sqlWord = "INSERT INTO " + DBWords.TABLE_NAME + " VALUES(?,?);";
		String sqlType = "INSERT INTO " + DBType.TABLE_NAME + " VALUES(?,?);";
		String sqlTranslate = "INSERT INTO " + DBTranslate.TABLE_NAME
				+ " VALUES(?,?,?);";
		openDictionarys(false);
		sqlDatabase.beginTransaction();
		SQLiteStatement stmtWord = sqlDatabase.compileStatement(sqlWord);
		SQLiteStatement stmtType = sqlDatabase.compileStatement(sqlType);
		SQLiteStatement stmtTranslate = sqlDatabase
				.compileStatement(sqlTranslate);
		while ((tmpString = readerStream.readLine()) != null) {
			try {
				if (tmpString != null && tmpString.length() != 0) {
					firstStar = tmpString.indexOf('*', 0);

					String words = tmpString.substring(0, firstStar);
					if (words != null) {

						fillTranslate(wordIndex, words, tmpString, type,
								wordsTransalateInsert, stmtType, stmtTranslate,
								stmtWord);
						wordIndex++;

					}
				}
			} catch (Exception e) {
				String error = e.toString();
				error = error + "";
			}
		}

		sqlDatabase.setTransactionSuccessful();
		sqlDatabase.endTransaction();
		in.close();
		close();
	}

	public void sqlQuery(String sql) {
		sqlDatabase.execSQL(sql);

	}

	private void fillTranslate(int wordIndex, String word,
			String stringFromFile, HashMap<String, Integer> typeOfWords,
			ArrayList<String> wordsTransalateInsert,/*
													 * StringBuilder
													 * strInsertTranslate ,
													 */
			SQLiteStatement stmtType, SQLiteStatement stmtTranslate,
			SQLiteStatement stmtWord) {
		// TODO Auto-generated method stub
		String type;
		int start = 0;
		int frstStar = stringFromFile.indexOf('*', start);
		boolean glaz = true;
		while (frstStar != stringFromFile.length()) {
			type = "";

			int scndStar = stringFromFile.indexOf('*', frstStar + 1);
			type = stringFromFile.substring(frstStar + 1, scndStar);

			frstStar = stringFromFile.indexOf('*', scndStar + 1);
			if (frstStar == -1) {
				frstStar = stringFromFile.length();
			}

			String translate = addTranslate(stringFromFile.substring(scndStar,
					frstStar));
			if (translate.length() > 0) {
				if (!typeOfWords.containsKey(type)) {
					// -----------------------------------------
					stmtType.bindLong(1, typeOfWords.size());
					stmtType.bindString(2, type);
					stmtType.execute();
					stmtType.clearBindings();
					// ----------------------------------------
					typeOfWords.put(type, typeOfWords.size());
				}

				if (glaz) {
					stmtWord.bindLong(1, wordIndex);
					stmtWord.bindString(2, word);
					stmtWord.execute();
					stmtWord.clearBindings();
					glaz = false;
				}
				try {
					int typeId = typeOfWords.get(type);
					stmtTranslate.bindLong(1, wordIndex);
					stmtTranslate.bindLong(2, typeId);
					stmtTranslate.bindString(3, translate);
					stmtTranslate.execute();
					stmtTranslate.clearBindings();
				} catch (Exception e) {
					String error = e.toString();
					error = error + "";
				}
			}

		}
	}

	private String addTranslate(String stringFromFile) {
		// TODO Auto-generated method stub
		int end = 0;
		int start = 0;
		StringBuffer translate = new StringBuffer();
		while (end != stringFromFile.length()) {
			start = stringFromFile.indexOf(')', start);
			end = stringFromFile.indexOf(')', start + 1);
			if (end == -1) {
				end = stringFromFile.length();
			}
			if (start != -1 && start + 1 != end)
				/* ERROR */
				try {
					if (end != stringFromFile.length()) {
						translate.append(stringFromFile.substring(start + 1,
								end - 1) + "*");
					} else {
						translate.append(stringFromFile.substring(start + 1,
								end) + "*");
					}
				} catch (Exception e) {
					String error = e.toString();
					error = error + "";
				}
			start = end + 1;
		}
		for (int i = 0; i < translate.length(); i++) {
			if (translate.charAt(i) == ',' || translate.charAt(i) == ';') {
				translate.setCharAt(i, '*');
			}
		}
		String result = translate.toString();
		return result;

	}

	public void exportDictionary(String folder, String name) {
		openDictionarys(false);
		String query = "SELECT words.word, type.type_name, translate.translate FROM words INNER JOIN translate  ON words._id = translate.id_word INNER JOIN type ON type._id = translate.id_type";
		Cursor cursor = null;
		try {
			cursor = sqlDatabase.rawQuery(query, null);
		} catch (Exception e) {
			String error = e.toString();
			error = error + "";
		}
		File file = new File(folder + File.separator + name + ".txt");
		if (cursor.moveToFirst()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				do {
					String translation = cursor.getString(2);
					translation = translation.replace("*", ",");
					translation = translation.substring(0,
							translation.length() - 1);
					String data = cursor.getString(0) + "*"
							+ cursor.getString(1) + "*1)" + translation + "\n";
					writer.write(data);
				} while (cursor.moveToNext());
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cursor.close();
		close();
		cursor.close();
		close();
	}
}
