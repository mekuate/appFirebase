package com.mekuate.kyala.model.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mekuate on 02/07/2017.
 */

public class Epreuve implements Parcelable {
    private String id;
    private String nom;
    private Long duree;
    private String niveau;
    private String matiere;
    private String classe;
    private HashMap <String, Boolean> quiz;


    private boolean solved;
    @Exclude
    private  int[] score;
    @Exclude
    protected List<Quize> quizzes;
    @Exclude
    private static final int NO_SCORE = 0;

    public Epreuve(){};


    public Epreuve(String id, String nom, Long duree, String niveau, String matiere, String classe, HashMap <String, Boolean> quiz, boolean solved) {
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.niveau = niveau;
        this.matiere = matiere;
        this.classe = classe;
        this.quiz = quiz;
        this.solved = solved;

    }



    protected Epreuve(Parcel in) {
        id = in.readString();
        nom = in.readString();
        duree = in.readLong();
        niveau = in.readString();
        matiere = in.readString();
        classe = in.readString();
        solved = in.readByte() != 0;
        score = in.createIntArray();
    }

    public static final Creator<Epreuve> CREATOR = new Creator<Epreuve>() {
        @Override
        public Epreuve createFromParcel(Parcel in) {
            return new Epreuve(in);
        }

        @Override
        public Epreuve[] newArray(int size) {
            return new Epreuve[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(nom);
        dest.writeLong(duree);
        dest.writeString(niveau);
        dest.writeString(matiere);
        dest.writeString(classe);
        dest.writeByte((byte) (solved ? 1 : 0));
        dest.writeIntArray(score);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    @Exclude
    public List<Quize> getQuizzes() {
        return quizzes;
    }
    @Exclude
    public void setQuizzes(List<Quize> quizes) {
        this.quizzes = quizes;
        if(this.quizzes != null){
            this.score = new int[this.quizzes.size()];
        }else
            this.score = null;

    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Long getDuree() {
        return duree;
    }

    public void setDuree(Long duree) {
        this.duree = duree;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }


    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }



    public void setScore(int[] score) {
        this.score = score;
    }

    /**
     * Updates a score for a provided quiz within this category.
     *
     * @param quize The quiz to rate.
     * @param correctlySolved <code>true</code> if the quiz was solved else <code>false</code>.
     */
    public void setScore(Quize quize, boolean correctlySolved) {
        int index = quizzes.indexOf(quize);
        Log.d("Score Quiz", "Setting score for " + quize + " with index " + index);
        if (-1 == index) {
            return;
        }
        score[index] = correctlySolved ? quize.getNote() : NO_SCORE;
    }

    public boolean isSolvedCorrectly(Quize quize) {
        return getScore(quize) == quize.getNote();
    }

    /**
     * Gets the score for a single quiz.
     *
     * @param quize The quiz to look for
     * @return The score if found, else 0.
     */
    public int getScore(Quize quize) {
        try {
            return score[quizzes.indexOf(quize)];
        } catch (IndexOutOfBoundsException ioobe) {
            return 0;
        }
    }

    /**
     * @return The sum of all quiz scores within this epreuve.
     */
    public int getScore() {
        int epreuveScore = 0;
        for (int quizScore : score) {
            epreuveScore += quizScore;
        }
        return epreuveScore;
    }

    public int[] getScores() {
        return score;
    }



}
