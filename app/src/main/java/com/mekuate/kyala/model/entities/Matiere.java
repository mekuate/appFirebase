package com.mekuate.kyala.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mekuate on 20/05/2017.
 */

public class Matiere implements Parcelable {
    private String id;
    private String nom;


    protected Matiere(Parcel in) {
        id = in.readString();
        nom = in.readString();
    }

    public Matiere() {
    }

    public static final Creator<Matiere> CREATOR = new Creator<Matiere>() {
        @Override
        public Matiere createFromParcel(Parcel in) {
            return new Matiere(in);
        }

        @Override
        public Matiere[] newArray(int size) {
            return new Matiere[size];
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
}
