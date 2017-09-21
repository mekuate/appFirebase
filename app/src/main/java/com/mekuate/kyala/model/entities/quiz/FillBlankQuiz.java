package com.mekuate.kyala.model.entities.quiz;

import android.annotation.SuppressLint;
import android.os.Parcel;

/**
 * Created by Mekuate on 02/07/2017.
 */

@SuppressLint("ParcelCreator")
public final class FillBlankQuiz extends Quiz<String> {


    public FillBlankQuiz(String question, String answer, boolean solved) {
        super(question, answer, solved);
    }

    @SuppressWarnings("unused")
    public FillBlankQuiz(Parcel in) {
        super(in);
        setAnswer(in.readString());
    }

    @Override
    public String getStringAnswer() {
        return getAnswer();
    }


    @Override
    public QuizType getType() {
        return QuizType.FILL_BLANK;
    }

    @Override
    public boolean isAnswerCorrect(String answer) {
        return getAnswer().equalsIgnoreCase(answer);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(getAnswer());

    }
}
