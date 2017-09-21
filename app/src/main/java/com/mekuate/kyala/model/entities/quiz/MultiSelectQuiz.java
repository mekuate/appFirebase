package com.mekuate.kyala.model.entities.quiz;

import android.annotation.SuppressLint;

/**
 * Created by Mekuate on 02/07/2017.
 */

@SuppressLint("ParcelCreator")
public final class MultiSelectQuiz extends OptionsQuiz<String> {

    public MultiSelectQuiz(String question, String[] answer, String[] options, boolean solved) {
        super(question, answer, options, solved);
    }



    @Override
    public QuizType getType() {
        return QuizType.MULTI_SELECT;
    }

    @Override
    public String getStringAnswer() {
        return AnswerHelper.getAnswer(getAnswer(), getOptions());
    }


}
