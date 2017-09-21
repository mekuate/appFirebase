package com.mekuate.kyala.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.mekuate.kyala.model.entities.quiz.AnswerHelper;

import java.util.HashMap;

/**
 * Created by Mekuate on 02/07/2017.
 * cette classe abstraite cree une structure generale des classes quiz
 */

public  class Quize implements Parcelable {
    private String id;
    private String reponse;
    private String question;
    private String enonce;
    private String image;
    private String quizType;
    private HashMap<String, String> options;
    private HashMap<String, Boolean> answer;
    private String end;
    private  String start;
    private String min;
    private  String max;
    private String step;
    private int note;
    private  boolean solved;


    protected Quize(Parcel in) {
        id = in.readString();
        reponse = in.readString();
        question = in.readString();
        quizType = in.readString();
        note= in.readInt();
        end = in.readString();
        start = in.readString();
        min = in.readString();
        max = in.readString();
        step = in.readString();
        solved = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(reponse);
        dest.writeString(question);
        dest.writeString(quizType);
        dest.writeString(end);
        dest.writeString(start);
        dest.writeString(min);
        dest.writeString(max);
        dest.writeInt(note);
        dest.writeString(step);
        dest.writeByte((byte) (solved ? 1 : 0));
    }

    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Quize> CREATOR = new Creator<Quize>() {
        @Override
        public Quize createFromParcel(Parcel in) {
            return new Quize(in);
        }

        @Override
        public Quize[] newArray(int size) {
            return new Quize[size];
        }
    };

    public HashMap<String, Boolean> getAnswer() {
        return answer;
    }

    public void setAnswer(HashMap<String, Boolean> answer) {
        this.answer = answer;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String answer) {
        this.reponse = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String questions) {
        this.question = questions;
    }

    public String getQuizType() {
        return quizType;
    }

    public void setQuizType(String quizType) {
        this.quizType = quizType;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public Quize() {
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public Quize(String id, String answer, String questions, String quizType, HashMap<String, String> options, boolean solved) {
        this.id = id;
        this.reponse = answer;
        this.question = questions;
        this.quizType = quizType;
        this.options = options;
        this.solved = solved;
    }

    public String getStringAnswer(){

        if (getReponse()!=null){
            return getReponse();
        }
        if(getAnswer()!=null){
             return AnswerHelper.getAnswer(getAnswer(), getOptions());
        }
        return null;
    }

    public  boolean isAnswerCorrect(){
        return false;
    }



}




