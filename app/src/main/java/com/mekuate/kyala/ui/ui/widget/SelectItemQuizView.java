package com.mekuate.kyala.ui.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.adapter.OptionsQuizAdapter;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.model.entities.quiz.SelectItemQuiz;

import java.util.HashMap;

/**
 * Created by Mekuate on 11/07/2017.
 */

@SuppressLint("ViewConstructor")
public class SelectItemQuizView extends AbsQuizView<SelectItemQuiz> {

    private static final String KEY_ANSWERS = "ANSWERS";

    private String mAnswers;
    private ListView mListView;
    private Quize quize;

    public SelectItemQuizView(Context context, Epreuve epreuve, SelectItemQuiz quiz, Quize quize) {
        super(context, epreuve, quiz,quize);
        mAnswers = null;
        this.quize = quize;
    }

    @Override
    protected View createQuizContentView() {
        Context context = getContext();
        mListView = new ListView(context);
        mListView.setDivider(null);
        mListView.setSelector(R.drawable.selector_button);
        mListView.setAdapter(
                new OptionsQuizAdapter(getQuiz().getOptions(), R.layout.item_answer_start,
                        context, true));
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allowAnswer();
                mAnswers = parent.getItemAtPosition(position).toString();
            }
        });
        return mListView;
    }

    @Override
    protected boolean isAnswerCorrect() {
        final String[] answer = getQuiz().getAnswer();
        HashMap<String,String> options = quize.getOptions();
        int i=0;
        String bestAnswer = options.get(answer[0]).toString();

        return mAnswers.equals(bestAnswer);
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ANSWERS, mAnswers);
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        mAnswers = savedInput.getString(KEY_ANSWERS);
        if (mAnswers == null) {
            return;
        }
        final ListAdapter adapter = mListView.getAdapter();
        for (int i = 0; i < getQuiz().getAnswer().length; i++) {
            mListView.performItemClick(mListView.getChildAt(i), i, adapter.getItemId(i));
        }
    }


}
