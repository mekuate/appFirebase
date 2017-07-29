package com.mekuate.kyala.model.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.quiz.AlphaPickerQuiz;
import com.mekuate.kyala.model.entities.quiz.FillBlankQuiz;
import com.mekuate.kyala.model.entities.quiz.FillTwoBlanksQuiz;
import com.mekuate.kyala.model.entities.quiz.FourQuarterQuiz;
import com.mekuate.kyala.model.entities.quiz.MultiSelectQuiz;
import com.mekuate.kyala.model.entities.quiz.PickerQuiz;
import com.mekuate.kyala.model.entities.quiz.Quiz;
import com.mekuate.kyala.model.entities.quiz.SelectItemQuiz;
import com.mekuate.kyala.model.entities.quiz.ToggleTranslateQuiz;
import com.mekuate.kyala.model.entities.quiz.TrueFalseQuiz;
import com.mekuate.kyala.ui.ui.widget.AbsQuizView;
import com.mekuate.kyala.ui.ui.widget.AlphaPickerQuizView;
import com.mekuate.kyala.ui.ui.widget.FillBlankQuizView;
import com.mekuate.kyala.ui.ui.widget.FillTwoBlanksQuizView;
import com.mekuate.kyala.ui.ui.widget.FourQuarterQuizView;
import com.mekuate.kyala.ui.ui.widget.MultiSelectQuizView;
import com.mekuate.kyala.ui.ui.widget.PickerQuizView;
import com.mekuate.kyala.ui.ui.widget.SelectItemQuizView;
import com.mekuate.kyala.ui.ui.widget.ToggleTranslateQuizView;
import com.mekuate.kyala.ui.ui.widget.TrueFalseQuizView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mekuate on 24/07/2017.
 */

public class QuizAdapter  extends BaseAdapter{
    private final Context mContext;
    private final List<Quiz> mQuizzes;
    private final Matiere mCategory;
    private final int mViewTypeCount;
    private List<String> mQuizTypes;
    private Epreuve epreuve;

    public QuizAdapter(Context context, Matiere category, Epreuve epreuve) {
        mContext = context;
        mCategory = category;
        mQuizzes = epreuve.getQuiz();
        mViewTypeCount = calculateViewTypeCount();

    }

    private int calculateViewTypeCount() {
        Set<String> tmpTypes = new HashSet<>();
        for (int i = 0; i < mQuizzes.size(); i++) {
            tmpTypes.add(mQuizzes.get(i).getType().getJsonName());
        }
        mQuizTypes = new ArrayList<>(tmpTypes);
        return mQuizTypes.size();
    }

    @Override
    public int getCount() {
        return mQuizzes.size();
    }

    @Override
    public Quiz getItem(int position) {
        return mQuizzes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mQuizzes.get(position).getId();
    }

    @Override
    public int getViewTypeCount() {
        return mViewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
        return mQuizTypes.indexOf(getItem(position).getType().getJsonName());
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Quiz quiz = getItem(position);
        if (convertView instanceof AbsQuizView) {
            if (((AbsQuizView) convertView).getQuiz().equals(quiz)) {
                return convertView;
            }
        }
        convertView = getViewInternal(quiz);
        return convertView;
    }

    private AbsQuizView getViewInternal(Quiz quiz) {
        if (null == quiz) {
            throw new IllegalArgumentException("Quiz must not be null");
        }
        return createViewFor(quiz);
    }

    private AbsQuizView createViewFor(Quiz quiz) {
        switch (quiz.getType()) {
            case ALPHA_PICKER:
                return new AlphaPickerQuizView(mContext, mCategory, (AlphaPickerQuiz) quiz);
            case FILL_BLANK:
                return new FillBlankQuizView(mContext, mCategory, (FillBlankQuiz) quiz);
            case FILL_TWO_BLANKS:
                return new FillTwoBlanksQuizView(mContext, mCategory, (FillTwoBlanksQuiz) quiz);
            case FOUR_QUARTER:
                return new FourQuarterQuizView(mContext, mCategory, (FourQuarterQuiz) quiz);
            case MULTI_SELECT:
                return new MultiSelectQuizView(mContext, mCategory, (MultiSelectQuiz) quiz);
            case PICKER:
                return new PickerQuizView(mContext, mCategory, (PickerQuiz) quiz);
            case SINGLE_SELECT:
            case SINGLE_SELECT_ITEM:
                return new SelectItemQuizView(mContext,mCategory,(SelectItemQuiz) quiz);
            case TOGGLE_TRANSLATE:
                return new ToggleTranslateQuizView(mContext, mCategory,
                        (ToggleTranslateQuiz) quiz);
            case TRUE_FALSE:
                return new TrueFalseQuizView(mContext, null, (TrueFalseQuiz) quiz);
        }
        throw new UnsupportedOperationException(
                "Quiz of type " + quiz.getType() + " can not be displayed.");
    }
}
