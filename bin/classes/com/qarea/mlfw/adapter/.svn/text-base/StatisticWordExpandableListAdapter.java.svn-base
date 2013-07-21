package com.qarea.mlfw.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.qarea.mlfw.BaseActivity;
import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.activity.MoveWordCalendarActivity;
import com.qarea.mlfw.model.Word;
import com.qarea.mlfw.newdictionary.NewDictionary;
import com.qarea.mlfw.util.LocalDataProvider;
import com.qarea.mlfw.util.WordStatistic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StatisticWordExpandableListAdapter extends BaseExpandableListAdapter {

    Context context;
    ArrayList<WordStatistic> learned;// alhmAllForList;
    ArrayList<WordStatistic> notLearned;
    ArrayList<WordStatistic> almostLearned;
    private LayoutInflater mChildInflater;
    private LayoutInflater mGroupInflater;
    private String group[];
    private long dateFrom;
    private TextToSpeech myTts;

    public StatisticWordExpandableListAdapter(Context context,
                                              ArrayList<WordStatistic> alhmAllForList, LocalDataProvider dataProvider,
                                              TextToSpeech myTts) {
        this.myTts = myTts;
        this.context = context;
        group = new String[]{"Learned", "Almost Learned", "Not Learned"};
        // this.alhmAllForList = alhmAllForList;
        learned = new ArrayList<WordStatistic>();
        notLearned = new ArrayList<WordStatistic>();
        almostLearned = new ArrayList<WordStatistic>();
        for (WordStatistic ws : alhmAllForList) {
            if (ws.getPercent() >= 80) {
                learned.add(ws);
            } else if (ws.getPercent() >= 50) {
                almostLearned.add(ws);
            } else {
                notLearned.add(ws);
            }
        }
        mChildInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.AM_PM, 0);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        dateFrom = cal.getTimeInMillis();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return learned.indexOf(childPosition);
        } else if (groupPosition == 1) {
            return almostLearned.indexOf(childPosition);
        } else {
            return notLearned.indexOf(childPosition);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        ExpandableList holder = null;
        if (convertView == null) {
            convertView = mChildInflater.inflate(R.layout.raw_statistics_word, null);
            holder = new ExpandableList();
            holder.word = (TextView) convertView.findViewById(R.id.statistic_word_text_view);
            holder.status = (TextView) convertView.findViewById(R.id.statistic_msg_text_view);
            holder.percent = (TextView) convertView.findViewById(R.id.statistic_word_percent);
            holder.countAttempt = (TextView) convertView.findViewById(R.id.statistic_msg_count);
            holder.voiceButton = (ImageButton) convertView.findViewById(R.id.voice_btn);
            holder.voiceButton.setVisibility(showSound() ? View.VISIBLE : View.INVISIBLE);
            holder.voiceButton.setFocusable(false);
            convertView.setTag(holder);
        } else {
            holder = (ExpandableList) convertView.getTag();
        }
        WordStatistic info = null;
        if (groupPosition == 0) {
            if (learned.size() != 0) {
                info = learned.get(childPosition);
            }
        } else if (groupPosition == 1) {
            if (almostLearned.size() != 0) {
                info = almostLearned.get(childPosition);
            }
        } else {
            if (notLearned.size() != 0) {
                info = notLearned.get(childPosition);
            }
        }
        if (info != null) {
            holder.word.setText(info.getWordName());
            if (info.getPercent() >= 80) {
                holder.status.setText("Learned");
                convertView.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
            } else if (info.getPercent() >= 50) {
                holder.status.setText("Almost Learned");
                convertView.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
            } else {
                holder.status.setText("Not Learned");
                convertView.setBackgroundColor(context.getResources().getColor(R.color.light_blue));
            }
            holder.percent.setText(new BigDecimal(info.getPercent()).setScale(0, RoundingMode.UP)
                    .intValue() + "%");
            holder.countAttempt.setText(info.getCorrectAnswerCount() + "/" + info.getAnswerCount());
            holder.voiceButton.setOnClickListener(new rowClickListener(info, holder.voiceButton));
            convertView.setOnClickListener(new rowClickListener(info, convertView));
        }
        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            return learned.size();
        } else if (groupPosition == 1) {
            return almostLearned.size();
        } else {
            return notLearned.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        ExpandableListHeader holder = null;
        if (convertView == null) {
            convertView = mGroupInflater.inflate(R.layout.row_head_statstic_list_adapter, null);
            holder = new ExpandableListHeader();
            holder.name = (TextView) convertView.findViewById(R.id.tvNameHeadStatistic);
            holder.status = (TextView) convertView.findViewById(R.id.tvCountWordHeadStatistic);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);

        } else {
            holder = (ExpandableListHeader) convertView.getTag();
        }
        String status = group[groupPosition];
        if (status != null) {
            holder.name.setText(status);
            int wordCount = 0;
            if (groupPosition == 0) {
                wordCount = learned.size();
                convertView.setBackgroundColor(context.getResources().getColor(R.color.blue));
                holder.image.setBackgroundDrawable(context.getResources().getDrawable(
                        R.drawable.sign_green));
            } else if (groupPosition == 1) {
                wordCount = almostLearned.size();
                convertView.setBackgroundColor(context.getResources().getColor(R.color.blue_blue));
                holder.image.setBackgroundDrawable(context.getResources().getDrawable(
                        R.drawable.sign_yellow));
            } else {
                wordCount = notLearned.size();
                convertView.setBackgroundColor(context.getResources().getColor(R.color.dark_blue));
                holder.image.setBackgroundDrawable(context.getResources().getDrawable(
                        R.drawable.sign_red));
            }
            String strWordsCount = wordCount + " words";
            holder.status.setText(strWordsCount);
        }
        return convertView;
        // return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    private boolean showSound() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(Extras.VOICE_ENABLED, false);
    }

    class ExpandableListHeader {
        TextView name;
        TextView status;
        ImageView image;
        LinearLayout headLinearLayout;
    }

    class ExpandableList {
        TextView word;
        TextView status;
        TextView percent;
        TextView countAttempt;
        ImageButton voiceButton;
    }

    class rowClickListener implements OnClickListener {
        WordStatistic word;
        View view;

        public rowClickListener(WordStatistic word, View view) {
            this.word = word;
        }

        @Override
        public void onClick(View v) {
        Log.d("tag", "onclick " + v.getId());
            switch (v.getId()) {
                case R.id.row_statistic_word:
                    AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(context);
                    deleteAlertDialog.setItems(new String[]{"Translate", "Repeate"},
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        NewDictionary dictionary = LocalDataProvider
                                                .getInstance(context)
                                                .getDictionary(context);
                                        String translate = dictionary.findTranslate(word
                                                .getWordName());

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                context);
                                        alertDialogBuilder.setTitle(word.getWordName());
                                        alertDialogBuilder
                                                .setMessage(translate)
                                                .setCancelable(false)
                                                .setNegativeButton(
                                                        "Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int id) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                        AlertDialog alertDialog = alertDialogBuilder
                                                .create();
                                        alertDialog.show();
                                    } else if (which == 1) {
                                        Word word_to_move = new Word(word.getWordId(), word
                                                .getDictonaryId(), word
                                                .getWordName());
                                        Intent translatorIntent = new Intent(context,
                                                MoveWordCalendarActivity.class);
                                        translatorIntent.putExtra(BaseActivity.MOVE_WORD,
                                                word_to_move);
                                        translatorIntent.putExtra(BaseActivity.MOVE_TIME,
                                                dateFrom);
                                        translatorIntent.putExtra(
                                                BaseActivity.REPEATE_WORD,
                                                BaseActivity.REPEATE_WORD_ADD);
                                        context.startActivity(translatorIntent);
                                    }
                                }
                            });
                    deleteAlertDialog.setCancelable(true);
                    deleteAlertDialog.show();
                    break;
                case R.id.voice_btn:
                    myTts.speak(word.getWordName(), TextToSpeech.QUEUE_ADD, null);
                    break;
            }
        }
    }

}
