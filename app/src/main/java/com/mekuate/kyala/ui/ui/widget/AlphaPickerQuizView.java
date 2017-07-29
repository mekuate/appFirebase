package com.mekuate.kyala.ui.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.quiz.AlphaPickerQuiz;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Mekuate on 11/07/2017.
 */

@SuppressLint("ViewConstructor")
public class AlphaPickerQuizView extends AbsQuizView<AlphaPickerQuiz> {

    private static final String KEY_SELECTION = "SELECTION";

    private TextView mCurrentSelection;
    private SeekBar mSeekBar;
    private List<String> mAlphabet;

    public AlphaPickerQuizView(Context context, Matiere matiere, AlphaPickerQuiz quiz) {
        super(context, matiere, quiz);
    }

    @Override
    protected View createQuizContentView() {
        ScrollView layout = (ScrollView) getLayoutInflater().inflate(
                R.layout.quiz_layout_picker, this, false);
        mCurrentSelection = (TextView) layout.findViewById(R.id.seekbar_progress);
        mCurrentSelection.setText(getAlphabet().get(0));
        mSeekBar = (SeekBar) layout.findViewById(R.id.seekbar);
        mSeekBar.setMax(getAlphabet().size() - 1);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentSelection.setText(getAlphabet().get(progress));
                allowAnswer();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /* no-op */
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /* no-op */
            }
        });
        return layout;
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(mCurrentSelection.getText().toString());
    }

    @Override
    public Bundle getUserInput() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SELECTION, mCurrentSelection.getText().toString());
        return bundle;
    }

    @Override
    public void setUserInput(Bundle savedInput) {
        if (savedInput == null) {
            return;
        }
        String userInput = savedInput.getString(KEY_SELECTION, getAlphabet().get(0));
        mSeekBar.setProgress(getAlphabet().indexOf(userInput));
    }


    private List<String> getAlphabet() {
        if (null == mAlphabet) {
            mAlphabet = Arrays.asList(getResources().getStringArray(R.array.alphabet));
        }
        return mAlphabet;
    }
}
