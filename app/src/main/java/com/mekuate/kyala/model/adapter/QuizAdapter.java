package com.mekuate.kyala.model.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.model.entities.quiz.AlphaPickerQuiz;
import com.mekuate.kyala.model.entities.quiz.FillBlankQuiz;
import com.mekuate.kyala.model.entities.quiz.FourQuarterQuiz;
import com.mekuate.kyala.model.entities.quiz.MultiSelectQuiz;
import com.mekuate.kyala.model.entities.quiz.PickerQuiz;
import com.mekuate.kyala.model.entities.quiz.Quiz;
import com.mekuate.kyala.model.entities.quiz.SelectItemQuiz;
import com.mekuate.kyala.model.entities.quiz.TrueFalseQuiz;
import com.mekuate.kyala.ui.ui.widget.AbsQuizView;
import com.mekuate.kyala.ui.ui.widget.AlphaPickerQuizView;
import com.mekuate.kyala.ui.ui.widget.FillBlankQuizView;
import com.mekuate.kyala.ui.ui.widget.FourQuarterQuizView;
import com.mekuate.kyala.ui.ui.widget.MultiSelectQuizView;
import com.mekuate.kyala.ui.ui.widget.PickerQuizView;
import com.mekuate.kyala.ui.ui.widget.SelectItemQuizView;
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
    private final List<Quize> mQuizzes;
    private final int mViewTypeCount;
    private List<String> mQuizTypes;
    private Epreuve epreuve;

    public QuizAdapter(Context context, Epreuve epreuve, List<Quize> quizs) {
        mContext = context;
        this.epreuve = epreuve;
        mQuizzes = quizs;
        mViewTypeCount = calculateViewTypeCount();

    }

    private int calculateViewTypeCount() {
        Set<String> tmpTypes = new HashSet<>();
        for (int i = 0; i < mQuizzes.size(); i++) {
            tmpTypes.add(mQuizzes.get(i).getQuizType());
        }
        mQuizTypes = new ArrayList<>(tmpTypes);
        return mQuizTypes.size();
    }

    @Override
    public int getCount() {
        return mQuizzes.size();
    }

    @Override
    public Quize getItem(int position) {
        return mQuizzes.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Long.valueOf(mQuizzes.get(position).getId());
        return 0;
    }


    @Override
    public int getViewTypeCount() {
        return mViewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {
       // mQuizTypes.indexOf(getItem(position).getType().getJsonName())
        return 0 ;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Quize quize = getItem(position);
        if (convertView instanceof AbsQuizView) {
            if (((AbsQuizView) convertView).getQuiz().equals(quize)) {
                return convertView;
            }
        }
        convertView = getViewInternal(quize);
        return convertView;
    }

    private AbsQuizView getViewInternal(Quize quize) {
        if (null == quize) {
            throw new IllegalArgumentException("Quiz must not be null");
        }
        return createViewFor(quize);
    }

   private AbsQuizView createViewFor(final Quize quize) {

        switch (quize.getQuizType()) {
            case "ALPHA_PICKER":
                Quiz quiz = new AlphaPickerQuiz(quize.getQuestion(),quize.getReponse(), quize.isSolved());
                return new AlphaPickerQuizView(mContext, epreuve, (AlphaPickerQuiz) quiz, quize);
            case "FILL_BLANK":
                Quiz quiz1 = new FillBlankQuiz(quize.getQuestion(),quize.getReponse(), quize.isSolved());
                return new FillBlankQuizView(mContext, epreuve, (FillBlankQuiz) quiz1, quize);
            case "FOUR_QUARTER":
                Quiz quiz3 = new FourQuarterQuiz(quize.getQuestion(),quize.getAnswer().keySet().toArray(new String[0]), quize.getOptions().values().toArray(new String[0]), quize.isSolved());
                return new FourQuarterQuizView(mContext, epreuve, (FourQuarterQuiz) quiz3, quize);
            case "MULTI_SELECT":
                Quiz quiz4 = new MultiSelectQuiz(quize.getQuestion(),quize.getAnswer().keySet().toArray(new String[0]), quize.getOptions().values().toArray(new String[0]),quize.isSolved() );
                return new MultiSelectQuizView(mContext, epreuve, (MultiSelectQuiz) quiz4, quize);
            case "PICKER":
                Quiz quiz5 = new PickerQuiz(quize.getQuestion(), Integer.parseInt(quize.getReponse()), Integer.parseInt(quize.getMin()), Integer.parseInt(quize.getMax()), Integer.parseInt(quize.getStep()),quize.isSolved());
                return new PickerQuizView(mContext, epreuve, (PickerQuiz) quiz5, quize);
            case "SINGLE_SELECT":
            case "SINGLE_SELECT_ITEM":
                SelectItemQuiz quiz6 = new SelectItemQuiz(quize.getQuestion(),quize.getAnswer().keySet().toArray(new String[0]), quize.getOptions().values().toArray(new String[0]),quize.isSolved());
                return new SelectItemQuizView(mContext,epreuve,quiz6, quize);
            case "TRUE_FALSE":
                TrueFalseQuiz quiz8 = new TrueFalseQuiz(quize.getQuestion(), quize.getReponse(), quize.isSolved());
                return new TrueFalseQuizView(mContext, epreuve, quiz8, quize);
        }
        throw new UnsupportedOperationException(
                "Quiz of type " + quize.getQuizType() + " can not be displayed.");
    }
}
