package com.qarea.mlfw.util;

import java.util.ArrayList;
import java.util.Collections;

public class SelectedDictionary {

	private static int dictionaryId = -1;
	private static SelectedDictionary cureentDictionary;
	private static ArrayList<Integer> dictionaryList;
	private static int repeatCount = -1;

	private SelectedDictionary() {
	}

	public static SelectedDictionary getIntance() {
		if (cureentDictionary == null) {
			cureentDictionary = new SelectedDictionary();
		}

		return cureentDictionary;
	}

	public static int getDictionaryID() {
		return dictionaryId;
	}

	public static void setDictionaryID(int newId) {
		dictionaryId = newId;
	}

	public static void setDictionaryList(ArrayList<Integer> newDictionaryList) {
		dictionaryList = newDictionaryList;
		Collections.sort(dictionaryList);
	}

	public static ArrayList<Integer> getDictionaryList() {
		if (dictionaryList == null)
			dictionaryList = new ArrayList<Integer>();
		return dictionaryList;
	}

	public static void addDictionary(Integer newDictionary) {
		if (dictionaryList == null)
			dictionaryList = new ArrayList<Integer>();

		if (!dictionaryList.contains(newDictionary)) {
			dictionaryList.add(newDictionary);
			Collections.sort(dictionaryList);
		}
	}

	public static void removeDictionary(Integer removeDictionary) {
		if (dictionaryList != null)
			dictionaryList.remove(removeDictionary);
	}

	public static int getRepeatCount() {
		return repeatCount;
	}

	public static void setRepeatCount(int repeatCount) {
		SelectedDictionary.repeatCount = repeatCount;
	}

}