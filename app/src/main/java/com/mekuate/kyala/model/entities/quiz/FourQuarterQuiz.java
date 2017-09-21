package com.mekuate.kyala.model.entities.quiz;

import java.util.Arrays;

/**
 * Created by Mekuate on 02/07/2017.
 */

public final class FourQuarterQuiz extends OptionsQuiz<String>  {



    public FourQuarterQuiz(String question, String [] answer, String[] options, boolean solved) {
        super(question, answer, options, solved);
    }



    @Override
    public QuizType getType() {
        return QuizType.FOUR_QUARTER;
    }

    @Override
    public String getStringAnswer() {
        return AnswerHelper.getAnswer(getAnswer(), getOptions());
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FourQuarterQuiz)) {
            return false;
        }

        FourQuarterQuiz quiz = (FourQuarterQuiz) o;
        final String[] answer = getAnswer();
        final String question = getQuestion();
        if (answer != null ? !Arrays.equals(answer, quiz.getAnswer()) : quiz.getAnswer() != null) {
            return false;
        }
        if (!question.equals(quiz.getQuestion())) {
            return false;
        }

        //noinspection RedundantIfStatement
        if (!Arrays.equals(getOptions(), quiz.getOptions())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(getOptions());
        result = 31 * result + Arrays.hashCode(getAnswer());
        return result;
    }

}
