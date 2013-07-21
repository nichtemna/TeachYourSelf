package com.qarea.mlfw.activity;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.qarea.mlfw.DBHelper;
import com.qarea.mlfw.DBHelper.DBDictionary;
import com.qarea.mlfw.R;
import com.qarea.mlfw.fragment.ShowCorrectnessDialogFragment;
import com.qarea.mlfw.model.Block;
import com.qarea.mlfw.model.ExpandableBlock;
import com.qarea.mlfw.model.WordWithTranslation;
import com.qarea.mlfw.newdictionary.NewDictionary;
import com.qarea.mlfw.util.SelectedDictionary;

public class CheckKnowledgeSwipeActivity extends
		AbstractCheckingKnowledgeActivity implements
		com.qarea.mlfw.fragment.ShowCorrectnessDialogFragment.OnDismissListener {
	private Block main_block;
	private ExpandableBlock tip_block;
	private ShowCorrectnessDialogFragment showDialog;

	private boolean touchMainBlock = false, tipViewVisible = false,
			setCoordiantes = false;
	private int wordIndex;// index of words for checking
	private ArrayList<WordWithTranslation> words;// words for checking
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private NewDictionary dictionary;
	private int right_position;// index of block with right answer
	private int blocks_id = 0;
	private boolean isRefreshing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_knowledg_swipe);

		initialize();

		setDictionarySpinner();
	}

	private void initialize() {
		wordIndex = 0;
		words = new ArrayList<WordWithTranslation>();
		dictionary = dataProvider.getDictionary(this);

		sDictionary = (Spinner) findViewById(R.id.sDictionary);

		main_block = new Block((TextView) findViewById(R.id.word_tv), blocks_id);

		tip_block = new ExpandableBlock((TextView) findViewById(R.id.tip_tv),
				++blocks_id);

		blocks.clear();

		blocks.add(new Block((TextView) findViewById(R.id.answer1_tv),
				++blocks_id));
		blocks.add(new Block((TextView) findViewById(R.id.answer2_tv),
				++blocks_id));
		blocks.add(new Block((TextView) findViewById(R.id.answer3_tv),
				++blocks_id));
	}

	/*
	 * Distance between blocks layout and top, helps to count touch position
	 */
	private int getTopDistance() {
		Spinner spinner = (Spinner) findViewById(R.id.sDictionary);
		TextView textView = (TextView) findViewById(R.id.textViewDict);
		return (int) (getResources().getDimensionPixelSize(
				R.dimen.abs__action_bar_default_height)
				+ spinner.getHeight() + textView.getHeight() + Block
				.getMargin() * 2);
	}

	/*
	 * View can be not drawn on create
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (!setCoordiantes) {
			setCoordiantes = true;
			getBlocksStartPositions();
		}
		super.onWindowFocusChanged(hasFocus);
	}

	private void getBlocksStartPositions() {
		int[] location = new int[2];

		// get blocks start position
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).getTextView().getLocationOnScreen(location);
			blocks.get(i).setLayoutParams(
					blocks.get(0).getTextView().getWidth() / 2,
					blocks.get(0).getTextView().getHeight());
			blocks.get(i).setWidthHeight(
					blocks.get(0).getTextView().getWidth() / 2,
					blocks.get(0).getTextView().getHeight());
			blocks.get(i).setX(location[0] + blocks.get(0).getWidth());
			blocks.get(i).setY(blocks.get(i).getTextView().getTop());
			blocks.get(i).move(blocks.get(i).getX(), blocks.get(i).getY());
		}

		// get tv_main start position
		main_block.getTextView().getLocationOnScreen(location);
		main_block.setX(main_block.getTextView().getLeft());
		main_block.setY(blocks.get(1).getY());
		main_block.setLayoutParams(blocks.get(1).getWidth(), blocks.get(1)
				.getHeight());
		main_block.setWidthHeight(blocks.get(1).getWidth(), blocks.get(1)
				.getHeight());

		// get tv_tip start position
		tip_block.setX(tip_block.getTextView().getLeft() - 20);
		tip_block.setY(tip_block.getTextView().getTop() - 20);
		tip_block.setHeightWidth();
	}

	private void showTipView(Block block) {
		tipViewVisible = true;
		tip_block.setBackgroundColor(getResources().getColor(
				R.color.check_knowledge_green));
		tip_block.expand(block);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				tipViewVisible = true;
			}
		}, 500);
	}

	private void hideTipView() {
		tip_block.hide();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				tip_block.getTextView().setVisibility(View.GONE);
				tipViewVisible = false;
			}
		}, 500);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (tipViewVisible) {
				hideTipView();
			} else {
				/*
				 * if move begun at the main_block - let it move
				 */
				if (inViewBounds(main_block, event)) {
					touchMainBlock = true;
					main_block.getStartTouchPos(event);
				} else {
					touchMainBlock = false;
				}
				for (int i = 0; i < blocks.size(); i++) {
					if (inViewBounds(blocks.get(i), event)) {
						showTipView(blocks.get(i));
					}
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (touchMainBlock) {
				float moveX = event.getRawX() - main_block.getWidth() / 2;
				float moveY = event.getRawY() - main_block.getHeight() / 2
						- getTopDistance();
				main_block.move(moveX, moveY);
				return true;
			}
		case MotionEvent.ACTION_UP:
			if (touchMainBlock) {
				boolean answered = false;
				for (int i = 0; i < blocks.size(); i++) {
					if (inViewBounds(blocks.get(i), event)) {
						setChosenAnswer(i);
						answered = true;
					}
				}
				if (!answered && main_block.sameEventCoordinates(event)) {
					showTipView(main_block);
					animate(main_block.getTextView()).setDuration(500)
							.x(main_block.getX()).y(main_block.getY());
					touchMainBlock = false;
				} else if (!answered) {
					animate(main_block.getTextView()).setDuration(500)
							.x(main_block.getX()).y(main_block.getY());
					touchMainBlock = false;
				}
			}
			break;
		default:
			break;
		}
		return false;
	}

	private void setChosenAnswer(int pos) {
		boolean correct = (pos == right_position);

		saveResults(correct);

		showCorrectnessDialog(correct, words.get(wordIndex));

		wordIndex++;

		// refresh words with delay, to show animation
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
					if (showDialog.isVisible()) {
						showDialog.dismiss();
					}
				//	refreshWord();
				}
		}, 3000);
	}

	protected void showCorrectnessDialog(boolean correct,
			WordWithTranslation word) {
		main_block.getTextView().setVisibility(View.GONE);
		FragmentManager fm = getSupportFragmentManager();
		showDialog = ShowCorrectnessDialogFragment.newInstance(correct, word);
		showDialog.show(fm, "fragment_show");
	}

	/*
	 * save results for statistic
	 */
	private void saveResults(boolean correct) {
		Date date = new Date();
		long today = date.getTime();
		dataProvider.insertStatus(words.get(wordIndex).getId(), today, correct);
	}

	protected void refreshWord() {
		isRefreshing = true;
		words = getFillHashMap();
		if (words.size() > 0) {
			if (words.size() > wordIndex) {
				if (tipViewVisible) {
					hideTipView();
				}
				returnBlocksToStart();
				populateBlocks(words.get(wordIndex));
				blocksSlideDown();
				isRefreshing = false;
			} else {
				finish();
			}
		} else {
			// if user choose empty dictionary - clear all text, disable next
			// button and show choose dictionary dialog
			main_block.getTextView().setText("");
			for (Block block : blocks) {
				block.setText("");
				block.getTextView().setVisibility(View.GONE);
			}
			main_block.getTextView().setVisibility(View.GONE);
			showChoseDialog();
		}
	}

	private void returnBlocksToStart() {
		main_block.move(0, -500);

		for (int i = 0; i < blocks.size(); i++) {
			ViewHelper.setY(blocks.get(i).getTextView(), -500);
		}
	}

	private void populateBlocks(WordWithTranslation word) {
		main_block.getTextView().setText(word.getWord());

		Random randomGenerator = new Random();
		right_position = randomGenerator.nextInt(2);
		for (int i = 0; i < blocks.size(); i++) {
			if (i == right_position) {
				blocks.get(i).setText(word.getTranslation());
			} else {
				blocks.get(i).setText(dictionary.getRandomTranslation());
			}
		}
	}

	private void blocksSlideDown() {
		for (Block block : blocks) {
			block.getTextView().setVisibility(View.VISIBLE);
			block.setBackgroundColor(getResources().getColor(
					R.color.check_knowledge_blue));
		}
		main_block.getTextView().setVisibility(View.VISIBLE);
		main_block.setBackgroundColor(getResources().getColor(
				R.color.check_knowledge_blue));

		for (int i = 0; i < blocks.size(); i++) {
			animate(blocks.get(i).getTextView()).setDuration(1500 - (i * 300))
					.y(blocks.get(i).getY());
		}

		animate(main_block.getTextView()).setDuration(1000)
				.x(main_block.getX()).y(main_block.getY());
	}

	private ArrayList<WordWithTranslation> getFillHashMap() {
		ArrayList<WordWithTranslation> array = new ArrayList<WordWithTranslation>();
		calendar.setTime(new Date());
		clearCalendar();
		Long fromDate = calendar.getTimeInMillis();
		calendar.add(Calendar.DAY_OF_WEEK, 1);
		Long toDate = calendar.getTimeInMillis();
		Cursor allIdAndWords = null;
		allIdAndWords = dataProvider.getAllWordsIdAndName(fromDate, toDate);
		if (allIdAndWords == null) {
			showChoseDialog();
		} else {
			if (allIdAndWords.moveToFirst()) {
				do {

					int id = allIdAndWords.getInt(0);

					String name = allIdAndWords.getString(allIdAndWords
							.getColumnIndex(DBHelper.DBWords.WORD_NAME));

					int dictionary_id = allIdAndWords.getInt(allIdAndWords
							.getColumnIndex(DBHelper.DBWords.DICTIONARY_ID));

					String translate = dictionary.findTranslate(name);

					WordWithTranslation word = new WordWithTranslation(id,
							dictionary_id, name, translate);
					array.add(word);
				} while (allIdAndWords.moveToNext());
			}
			allIdAndWords.close();
		}
		allIdAndWords.close();
		return array;
	}

	private void setDictionarySpinner() {
		ArrayList<String> dictionaryArray = new ArrayList<String>();

		Cursor allDictionary = dataProvider.getSelectedDictionary();
		while (allDictionary.moveToNext())
			dictionaryArray.add(allDictionary.getString(allDictionary
					.getColumnIndex(DBDictionary.NAME)));
		allDictionary.close();
		ArrayAdapter<String> sadapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dictionaryArray);
		sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sDictionary.setAdapter(sadapter);
		sDictionary.setSelection(SelectedDictionary.getDictionaryList()
				.indexOf(SelectedDictionary.getDictionaryID()));
		sDictionary.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				TextView raw = (TextView) arg1;
				int dictionaryID = dataProvider.getDictionaryIdByName(raw
						.getText().toString());
				SelectedDictionary.setDictionaryID(dictionaryID);
				wordIndex = 0;
				refreshWord();

				SharedPreferences settings = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(SAVE_WORD, dictionaryID);
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	/*
	 * check if touch event happened in the bounds of view
	 */
	private boolean inViewBounds(Block block, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		Rect outRect = new Rect();
		// int[] location = new int[2];
		block.getTextView().getDrawingRect(outRect);
		// .getLocationOnScreen(location);
		outRect.offset(block.getX(), block.getY());
		return outRect.contains(x, y - getTopDistance());
	}

	@Override
	public void onBackPressed() {
		if (tipViewVisible) {
			hideTipView();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onDismiss() {
		refreshWord();
	}

	@Override
	public boolean isRefreshing() {
		return isRefreshing;
	}
}
