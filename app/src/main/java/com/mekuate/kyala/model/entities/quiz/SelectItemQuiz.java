package com.mekuate.kyala.model.entities.quiz;

import android.annotation.SuppressLint;
import android.os.Parcel;

/**
 * Created by Mekuate on 02/07/2017.
 */

@SuppressLint("ParcelCreator")
public final class SelectItemQuiz extends OptionsQuiz<String> {

    public SelectItemQuiz(String question, int[] answer, String[] options, boolean solved) {
        super(question, answer, options, solved);
    }

    @SuppressWarnings("unused")
    public SelectItemQuiz(Parcel in) {
        super(in);
        String[] options = in.createStringArray();
        setOptions(options);
    }

    @Override
    public QuizType getType() {
        return QuizType.SINGLE_SELECT;
    }

    @Override
    public String getStringAnswer() {
        return AnswerHelper.getAnswer(getAnswer(), getOptions());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeStringArray(getOptions());
    }
}
