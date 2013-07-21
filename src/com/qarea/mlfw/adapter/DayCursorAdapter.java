package com.qarea.mlfw.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.provider.UserDictionary.Words;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qarea.mlfw.Extras;
import com.qarea.mlfw.R;
import com.qarea.mlfw.util.LocalDataProvider;

public class DayCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private LocalDataProvider dataProvider;
    private Context context;
    private ImageButton voice_btn;
    private TextToSpeech myTts;

    public DayCursorAdapter(Context context, Cursor c, int idDictionary, long currentDate,
                    TextToSpeech myTts) {
        super(context, c, true);
        inflater = LayoutInflater.from(context);
        dataProvider = LocalDataProvider.getInstance(context);
        this.context = context;
        this.myTts = myTts;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String word = cursor.getString(cursor.getColumnIndex(Words.WORD));
        TextView wordTV = (TextView) view.findViewById(R.id.raw_day_word);
        wordTV.setTypeface(null, Typeface.BOLD);
        wordTV.setTag("newWord");
        wordTV.setText(word);
        TextView trandslationTV = (TextView) view.findViewById(R.id.raw_day_translation);
        ArrayList<String> translations = new ArrayList<String>();
        translations.add(dataProvider.getDictionary(context).findTranslate(word));
        StringBuilder allTranslations = new StringBuilder();
        Iterator<String> translationIter = translations.iterator();
        while (translationIter.hasNext()) {
            allTranslations.append(translationIter.next());
            if (translationIter.hasNext())
                allTranslations.append(", ");
        }
        trandslationTV.setText(allTranslations.toString());

        voice_btn = (ImageButton) view.findViewById(R.id.voice_btn);
        voice_btn.setVisibility(showSound() ? View.VISIBLE : View.INVISIBLE);
        voice_btn.setFocusable(false);
        voice_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               myTts.speak(word, TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.raw_day_view, null);
    }

    private boolean showSound() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(Extras.VOICE_ENABLED, false);
    }
}
