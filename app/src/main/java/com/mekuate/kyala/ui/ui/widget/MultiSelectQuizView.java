package com.mekuate.kyala.ui.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mekuate.kyala.model.adapter.OptionsQuizAdapter;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.model.entities.quiz.AnswerHelper;
import com.mekuate.kyala.model.entities.quiz.MultiSelectQuiz;

import java.util.HashMap;

/**
 * Created by Mekuate on 11/07/2017.
 */

@SuppressLint("ViewConstructor")
public class MultiSelectQuizView extends AbsQuizView<MultiSelectQuiz> {

    private static final String KEY_ANSWER = "ANSWER";

    private ListView mListView;
    private String[] mAnswer;
    private Quize quize;

    public MultiSelectQuizView(Context context, Epreuve epreuve, MultiSelectQuiz quiz, Quize quize) {
        super(context, epreuve, quiz, quize);
        this.quize=quize;
        mAnswer= new String[quize.getOptions().size()];
    }

    @Override
    protected View createQuizContentView() {
        mListView = new ListView(getContext());
        mListView.setAdapter(
                new OptionsQuizAdapter(getQuiz().getOptions(),
                        android.R.layout.simple_list_item_multiple_choice));
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setItemsCanFocus(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allowAnswer();
                mAnswer[position] = parent.getItemAtPosition(position).toString();

            }
        });
        return mListView;
    }

    @Override
    protected boolean isAnswerCorrect() {
        final String[] answer = getQuiz().getAnswer();
        HashMap<String,String> options = quize.getOptions();
        String[] bestAnswer = new String[answer.length];
        for(int i=0; i< answer.length; i++){
             bestAnswer[i] = options.get(answer[i]).toString();

        }

        return AnswerHelper.isAnswerCorrect(mAnswer,bestAnswer);
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_ANSWER, mAnswer);
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        final String[] answers = savedInput.getStringArray(KEY_ANSWER);
        if (null == answers) {
            return;
        }
        final ListAdapter adapter = mListView.getAdapter();
        for (int i = 0; i < getQuiz().getOptions().length; i++) {
            mListView.performItemClick(mListView.getChildAt(i), i, adapter.getItemId(i));
        }
    }

    private boolean[] getBundleableAnswer() {
        SparseBooleanArray checkedItemPositions = mListView.getCheckedItemPositions();
        final int answerSize = checkedItemPositions.size();
        if (0 == answerSize) {
            return null;
        }
        final int optionsSize = getQuiz().getOptions().length;
        boolean[] bundleableAnswer = new boolean[optionsSize];
        int key;
        for (int i = 0; i < answerSize; i++) {
            key = checkedItemPositions.keyAt(i);
            bundleableAnswer[key] = checkedItemPositions.valueAt(i);
        }
        return bundleableAnswer;
    }
}
