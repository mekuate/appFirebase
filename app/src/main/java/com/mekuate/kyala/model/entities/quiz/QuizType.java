package com.mekuate.kyala.model.entities.quiz;

import com.mekuate.kyala.model.entities.JsonAttributes;

/**
 * Created by Mekuate on 02/07/2017.
 */

public enum QuizType {
    ALPHA_PICKER(JsonAttributes.QuizType.ALPHA_PICKER, AlphaPickerQuiz.class),
    FILL_BLANK(JsonAttributes.QuizType.FILL_BLANK, FillBlankQuiz.class),
    FOUR_QUARTER(JsonAttributes.QuizType.FOUR_QUARTER, FourQuarterQuiz.class),
    MULTI_SELECT(JsonAttributes.QuizType.MULTI_SELECT, MultiSelectQuiz.class),
    PICKER(JsonAttributes.QuizType.PICKER, PickerQuiz.class),
    SINGLE_SELECT(JsonAttributes.QuizType.SINGLE_SELECT, SelectItemQuiz.class),
    SINGLE_SELECT_ITEM(JsonAttributes.QuizType.SINGLE_SELECT_ITEM, SelectItemQuiz.class),
    TRUE_FALSE(JsonAttributes.QuizType.TRUE_FALSE, TrueFalseQuiz.class);

    private final String mJsonName;
    private final Class<? extends Quiz> mType;

    QuizType(final String jsonName, final Class<? extends Quiz> type) {
        mJsonName = jsonName;
        mType = type;
    }

    public String getJsonName() {
        return mJsonName;
    }

    public Class<? extends Quiz> getType() {
        return mType;
    }
}
