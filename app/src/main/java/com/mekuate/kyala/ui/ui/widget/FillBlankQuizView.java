package com.mekuate.kyala.ui.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.model.entities.quiz.FillBlankQuiz;

/**
 * Created by Mekuate on 11/07/2017.
 */


@SuppressLint("ViewConstructor")
public class FillBlankQuizView extends TextInputQuizView<FillBlankQuiz> {

    private static final String KEY_ANSWER = "ANSWER";

    private EditText mAnswerView;

    public FillBlankQuizView(Context context, Epreuve epreuve, FillBlankQuiz quiz, Quize quize) {
        super(context, epreuve, quiz, quize);
    }

    @Override
    protected View createQuizContentView() {

        if (null == mAnswerView) {
            mAnswerView = createEditText();
        }
        return mAnswerView;
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ANSWER, mAnswerView.getText().toString());
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        mAnswerView.setText(savedInput.getString(KEY_ANSWER));
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(mAnswerView.getText().toString());
    }
}
