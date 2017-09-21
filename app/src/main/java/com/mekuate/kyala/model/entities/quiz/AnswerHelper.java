package com.mekuate.kyala.model.entities.quiz;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Mekuate on 02/07/2017.
 */

public class AnswerHelper {

    static final String SEPARATOR = System.getProperty("line.separator");
    private static final String TAG = "AnswerHelper";

    private AnswerHelper() {
        //no instance
    }

    /**
     * Converts an array of answers to a readable answer.
     *
     * @param answers The answers to display.
     * @return The readable answer.
     */
    public static String getStringAnswer(String[] answers) {
        StringBuilder readableAnswer = new StringBuilder();
        //Iterate over all answers
        for (int i = 0; i < answers.length; i++) {
            String answer = answers[i];
            readableAnswer.append(answer);
            //Don't add a separator for the last answer
            if (i < answers.length - 1) {
                readableAnswer.append(SEPARATOR);
            }
        }
        return readableAnswer.toString();
    }

    /**
     * Converts an array of answers with options to a readable answer.
     *
     * @param answers The actual answers
     * @param options The options to display.
     * @return The readable answer.
     */
    public static String getAnswer(String[] answers, String[] options) {
        String[] readableAnswers = new String[answers.length];
        for (int i = 0; i < answers.length; i++) {
            for (int j = 0; j < options.length; j++) {
                if (options[j].equals(answers[i])){
                    final String humanReadableAnswer = answers[i];
                    readableAnswers[i] = humanReadableAnswer;
                }
            }
        }
        return getStringAnswer(readableAnswers);
    }



    /**
     * Checks whether a provided answer is correct.
     *

     * @return <code>true</code> if correct else <code>false</code>.
     */
    public static boolean isAnswerCorrect(String[] checkedItems, String[] answerIds) {
        if (null == checkedItems || null == answerIds) {
            Log.i(TAG, "isAnswerCorrect got a null parameter input.");
            return false;
        }

        int nombre =0;

        for(int i=0; i <answerIds.length;i++){
            for(int j=0; j <checkedItems.length; j++)
            if (answerIds[i].equals(checkedItems[j])) {
               nombre++;
            }
        }
       return nombre == answerIds.length;
    }


    public static String getAnswer(HashMap<String, Boolean> answer, HashMap<String, String> options) {
        String[] readableAnswers = new String[answer.size()];
        int i=0;
        for (String keyAnswer: answer.keySet()) {
            final String humanReadableAnswer = options.get(keyAnswer);
            readableAnswers[i] = humanReadableAnswer;
            i++;
        }
        return getStringAnswer(readableAnswers);
    }
}
