package com.mekuate.kyala.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mekuate on 13/07/2017.
 */

public class Niveau implements Parcelable {
    private String nom;
    private String id;

    protected Niveau(Parcel in) {
        nom = in.readString();
        id  = in.readString();
    }

    public static final Creator<Niveau> CREATOR = new Creator<Niveau>() {
        @Override
        public Niveau createFromParcel(Parcel in) {
            return new Niveau(in);
        }

        @Override
        public Niveau[] newArray(int size) {
            return new Niveau[size];
        }
    };


    public Niveau(String nom) {
        this.nom = nom;
    }

    public Niveau() {
    }

    public Niveau(String nom,  String id) {
        this.nom = nom;

        this.id = id;
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

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(id);
    }
}
