package com.qarea.mlfw.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.util.Log;

public class Dictionary {

	final static String pathToTextFile = "sdcard/translate/newdictionary/";

	HashMap<String, WordValue> dictionary;
	int eye; // watch what find first synonyms (1) or last synonym (2) or
				// translate (3) or last translate (4) or type (5)
	String dictionaryName;
	private int dictionaryId;

	public Dictionary() {
		dictionary = new HashMap<String, WordValue>();
		this.dictionaryName = "";
		this.dictionaryId = SelectedDictionary.getDictionaryID();
		setDictionaryName();
	}

	public Dictionary(int dictionaryID) {
		dictionary = new HashMap<String, WordValue>();
		this.dictionaryName = "";
		this.dictionaryId = dictionaryID;
		setDictionaryName();
	}

	public void addNewWord(WordValue newWord) {
		dictionary.put(newWord.getWord(), newWord);
	}

	public int getDictionaryId() {
		return dictionaryId;
	}

	public String getPath() {
		return pathToTextFile + dictionaryName;
	}

	public HashSet<String> getAllWords(String checkStr) {
		ArrayList<String> allWords = new ArrayList<String>(dictionary.keySet());
		HashSet<String> returnWords = new HashSet<String>();
		for (String word : allWords) {
			if (word.length() >= checkStr.length()) {
				if (word.substring(0, checkStr.length()).equals(
						checkStr.toLowerCase()))
					returnWords.add(word);
			}
		}
		return returnWords;
	}

	public boolean containWord(String word) {
		if (dictionary.get(word) == null)
			return false;
		return true;
	}

	public void setDictionaryName() {
		this.dictionaryName = LocalDataProvider
				.getDictionaryNameById(dictionaryId);
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() throws IOException {
		if (dictionaryName == "")
			return;
		dictionary = new HashMap<String, WordValue>();
		InputStream in = new FileInputStream(pathToTextFile + dictionaryName);
		InputStreamReader fileStream = new InputStreamReader(in);
		BufferedReader readerStream = new BufferedReader(fileStream);
		String tmpString = "";
		ArrayList<String> arrWords; // all words
		ArrayList<String> arrTranslate;// translate
		String wordType = "";// type of word
		while ((tmpString = readerStream.readLine()) != null) {
			arrWords = new ArrayList<String>();
			arrTranslate = new ArrayList<String>();
			eye = 1;

			if (tmpString.length() > 0 && tmpString.charAt(0) == ' '
					&& tmpString.charAt(1) != ' ') {
				// string parsing
				int tmpIndexStart = 1;// because the word must be at least one
										// letter, we start with the second
										// element
				while (tmpIndexStart < tmpString.length()) {
					int tmpIndexEndSubString = findIndexOfSeparator(tmpString,
							tmpIndexStart); // write
											// in
											// what
											// index
											// char
											// separator
					if (eye == 1 || eye == 2) {
						arrWords.add(tmpString.substring(tmpIndexStart,
								tmpIndexEndSubString - 1));
						// -1 because in position tmpIndexEndSubString-1 - _
					} else if (eye == 3 || eye == 4) {
						arrTranslate.add(tmpString.substring(tmpIndexStart,
								tmpIndexEndSubString - 1));
					} else {
						wordType = tmpString.substring(tmpIndexStart,
								tmpIndexEndSubString);
					}
					tmpIndexStart = tmpIndexEndSubString + 2;
					// +2 because tmpIndexEndSubString that are separating
					// element, and after separating we have _
				}
				Log.e("1", "1");
				// load to HashMap dictionary
				loadToHashMap(arrWords, arrTranslate, wordType);
				Log.e("2", "2");
			}
		}
		in.close();
	}

	private void loadToHashMap(ArrayList<String> arrWords,
			ArrayList<String> arrTranslate, String wordType) {
		String key;
		WordValue value;
		for (String word : arrWords) {
			key = word;
			value = dictionary.get(key);
			if (value == null) {
				value = new WordValue();
				value.setType(wordType);
			}
			value.setWord(word);
			value.setTranslation(arrTranslate);
			dictionary.put(key, value);
		}
	}

	private int findIndexOfSeparator(String strFromStream, int frstChar) {

		// if ; - synonym is \t - translate
		while (strFromStream.length() > frstChar) {
			// synonym
			if (eye == 1 && strFromStream.charAt(frstChar) == ';') {
				eye = 1;
				break;
			}
			// last synonym
			else if (eye == 1 && strFromStream.charAt(frstChar) == '\t') {
				eye = 2;
				break;
			}
			// translate
			else if (eye == 2 && strFromStream.charAt(frstChar) == ';') {
				eye = 3;
				break;
			} else if (eye == 3 && strFromStream.charAt(frstChar) == ';') {
				eye = 3;
				break;
			}
			// last translate
			else if ((eye == 3 || eye == 2)
					&& strFromStream.charAt(frstChar) == '\t') {
				eye = 4;
				break;
			}
			// type
			else if (eye == 4 && frstChar == strFromStream.length() - 1) {
				eye = 5;
				break;
			}
			frstChar++;
		}
		return frstChar;
	}

	// if no word return null
	public WordValue findTranslate(String word) {
		WordValue findWordTranslate = dictionary.get(word);

		return findWordTranslate;
	}

	// true if translate correctness else false. If are no word return false
	public int checkOfTheCorrectness(String word,
			ArrayList<String> parsedWordTranslate) {
		word = word.trim();
		boolean rightWord = false;
		WordValue wordToCheckOfTheCorrectness = findTranslate(word);
		if (wordToCheckOfTheCorrectness != null) {
			ArrayList<String> translates = new ArrayList<String>();
			wordToCheckOfTheCorrectness.getTranslation(translates);
			for (String wordTranslate : parsedWordTranslate) {
				for (String str : translates) {
					if (str.equals(wordTranslate)/* == wordTranslate */) {
						rightWord = true;
						break;
					}

				}
				if (rightWord == false) {
					return 0; // not correct
				} else
					rightWord = false;
			}
			return 1; // correct
		}
		return 3; // no words in dictionary
	}
}
