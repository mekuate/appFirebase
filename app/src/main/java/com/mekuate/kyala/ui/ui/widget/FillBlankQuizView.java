package com.mekuate.kyala.ui.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.quiz.FillBlankQuiz;

/**
 * Created by Mekuate on 11/07/2017.
 */


@SuppressLint("ViewConstructor")
public class FillBlankQuizView extends TextInputQuizView<FillBlankQuiz> {

    private static final String KEY_ANSWER = "ANSWER";

    private EditText mAnswerView;

    public FillBlankQuizView(Context context, Matiere matiere, FillBlankQuiz quiz) {
        super(context, matiere, quiz);
    }

    @Override
    protected View createQuizContentView() {
        String start = getQuiz().getStart();
        String end = getQuiz().getEnd();
        if (null != start || null != end) {
            return getStartEndView(start, end);
        }
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

    /**
     * Creates and returns views that display the start and end of a question.
     *
     * @param start The content of the start view.
     * @param end The content of the end view.
     * @return The created views within an appropriate container.
     */
    private View getStartEndView(String start, String end) {
        LinearLayout container = (LinearLayout) getLayoutInflater().inflate(
                R.layout.quiz_fill_blank_with_surrondings, this, false);
        mAnswerView = (EditText) container.findViewById(R.id.quiz_edit_text);
        mAnswerView.addTextChangedListener(this);
        mAnswerView.setOnEditorActionListener(this);
        //noinspection PrivateResource
        TextView startView = (TextView) container.findViewById(R.id.start);
        setExistingContentOrHide(startView, start);

        //noinspection PrivateResource
        TextView endView = (TextView) container.findViewById(R.id.end);
        setExistingContentOrHide(endView, end);

        return container;
    }

    /**
     * Sets content to a {@link TextView}. If content is null, the view will not be displayed.
     *
     * @param view The view to hold the text.
     * @param content The text to display.
     */
    private void setExistingContentOrHide(TextView view, String content) {
        if (null == content) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(content);
        }
    }

    @Override
    protected boolean isAnswerCorrect() {
        return getQuiz().isAnswerCorrect(mAnswerView.getText().toString());
    }
}
