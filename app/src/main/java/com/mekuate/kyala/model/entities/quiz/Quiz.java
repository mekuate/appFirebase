package com.mekuate.kyala.model.entities.quiz;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Mekuate on 02/07/2017.
 * cette classe abstraite cree une structure generale des classes quiz
 */

public abstract class Quiz<A>  implements Parcelable {
    private static final String TAG = "Quiz";

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @SuppressWarnings("TryWithIdenticalCatches")
        @Override
        public Quiz createFromParcel(Parcel in) {
            int ordinal = in.readInt();
            QuizType type = QuizType.values()[ordinal];
            try {
                Constructor<? extends Quiz> constructor = type.getType()
                        .getConstructor(Parcel.class);
                return constructor.newInstance(in);
            } catch (InstantiationException e) {
                performLegacyCatch(e);
            } catch (IllegalAccessException e) {
                performLegacyCatch(e);
            } catch (InvocationTargetException e) {
                performLegacyCatch(e);
            } catch (NoSuchMethodException e) {
                performLegacyCatch(e);
            }
            throw new UnsupportedOperationException("Could not create Quiz");
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    private static void performLegacyCatch(Exception e) {
        Log.e(TAG, "createFromParcel ", e);
    }

    private final String question;
    private final String quizType;
    private A answer;
    /**
     * Flag indicating whether this quiz has already been solved.
     * It does not give information whether the solution was correct or not.
     */
    private boolean mSolved;

    protected Quiz(String question, A answer, boolean solved) {
        this.question = question;
        this.answer = answer;
        quizType = getType().getJsonName();
        mSolved = solved;
    }

    protected Quiz(Parcel in) {
        question = in.readString();
        quizType = getType().getJsonName();
        mSolved = ParcelableHelper.readBoolean(in);
    }

    /**
     * @return The {@link QuizType} that represents this quiz.
     */
    public abstract QuizType getType();

    /**
     * Implementations need to return a human readable version of the given answer.
     */
    public abstract String getStringAnswer();

    public String getQuestion() {
        return question;
    }

    public A getAnswer() {
        return answer;
    }

    protected void setAnswer(A answer) {
        answer = answer;
    }

    public boolean isAnswerCorrect(A answer) {
        return this.answer.equals(answer);
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    /**
     * @return The id of this quiz.
     */
    public int getId() {
        return getQuestion().hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelableHelper.writeEnumValue(dest, getType());
        dest.writeString(question);
        ParcelableHelper.writeBoolean(dest, mSolved);
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }

        Quiz quiz = (Quiz) o;

        if (mSolved != quiz.mSolved) {
            return false;
        }
        if (!answer.equals(quiz.answer)) {
            return false;
        }
        if (!question.equals(quiz.question)) {
            return false;
        }
        if (!quizType.equals(quiz.quizType)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + answer.hashCode();
        result = 31 * result + quizType.hashCode();
        result = 31 * result + (mSolved ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return getType() + ": \"" + getQuestion() + "\"";
    }
}


