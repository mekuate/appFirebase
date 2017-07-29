package com.mekuate.kyala.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.mekuate.kyala.model.entities.quiz.Quiz;

import java.util.List;

/**
 * Created by Mekuate on 02/07/2017.
 */

public class Epreuve implements Parcelable {
    private String id;
    private String nom;
    private int duree;
    private String niveau;
    private String matiere;
    private String classe;
    private List <Quiz> quiz;

    private boolean solved;
    private  int[] score;
    public Epreuve(){};

    public Epreuve(String id, String nom, int duree, String niveau, String matiere, String classe, List<Quiz> quiz) {
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.niveau = niveau;
        this.matiere = matiere;
        this.classe = classe;
        this.quiz = quiz;
        this.score = new int[this.quiz.size()];
    }

    public Epreuve(String niveau, String id, String nom, int duree, String matiere, int[] score, String classe,List<Quiz> quiz) {
        this.niveau = niveau;
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.matiere = matiere;
        this.score = score;
        this.classe = classe;
        if (quiz.size() == score.length) {
            this.quiz = quiz;
            this.score = score;
        } else {
            throw new IllegalArgumentException("Quizzes and scores must have the same length");
        }
    }


    protected Epreuve(Parcel in) {
        id = in.readString();
        nom = in.readString();
        duree = in.readInt();
        niveau = in.readString();
        matiere = in.readString();
        classe = in.readString();
        quiz = in.createTypedArrayList(Quiz.CREATOR);
        solved = in.readByte() != 0;
        score = in.createIntArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nom);
        dest.writeInt(duree);
        dest.writeString(niveau);
        dest.writeString(matiere);
        dest.writeString(classe);
        dest.writeTypedList(quiz);
        dest.writeByte((byte) (solved ? 1 : 0));
        dest.writeIntArray(score);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public List<Quiz> getQuiz() {
        return quiz;
    }

    public void setQuiz(List<Quiz> quiz) {
        this.quiz = quiz;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
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
    public boolean isSolved(){
        return solved;
    }
    public void setSolved(boolean solved){ this.solved =solved;}



    public int getFirstUnsolvedQuizPosition() {
        if (quiz == null) {
            return -1;
        }
        for (int i = 0; i < quiz.size(); i++) {
            if (!quiz.get(i).isSolved()) {
                return i;
            }
        }
        return quiz.size();
    }
}
