package com.mekuate.kyala.model.entities.quiz;

import android.os.Parcel;

import java.util.Arrays;

/**
 * Created by Mekuate on 02/07/2017.
 */

public abstract class OptionsQuiz<T> extends Quiz<String[]> {

    private T[] mOptions;

    public OptionsQuiz(String question, String[] answer, T[] options, boolean solved) {
        super(question, answer, solved);
        mOptions = options;
    }


    public T[] getOptions() {
        return mOptions;
    }

    protected void setOptions(T[] options) {
        mOptions = options;
    }

    @Override
    public boolean isAnswerCorrect(String[] answer) {

        return Arrays.equals(getAnswer(), answer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @SuppressWarnings("RedundantIfStatement")

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionsQuiz)) {
            return false;
        }

        OptionsQuiz that = (OptionsQuiz) o;

        if (!Arrays.equals(getAnswer(), ((String[]) that.getAnswer()))) {
            return false;
        }
        if (!Arrays.equals(mOptions, that.mOptions)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(mOptions);
        return result;
    }
}
