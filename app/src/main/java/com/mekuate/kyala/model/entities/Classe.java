package com.mekuate.kyala.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by Mekuate on 20/05/2017.
 */

public class Classe  implements Parcelable {
    private String id;
    private String nom;
   private HashMap<String,Boolean> niveau;
    private HashMap <String,HashMap<String, String>> matiere;

    public Classe() {
    }

    public Classe( String id,String nom) {
        this.nom = nom;
        this.id = id;
    }

    protected Classe(Parcel in) {
        id = in.readString();
        nom = in.readString();
    }

    public static final Creator<Classe> CREATOR = new Creator<Classe>() {
        @Override
        public Classe createFromParcel(Parcel in) {
            return new Classe(in);
        }

        @Override
        public Classe[] newArray(int size) {
            return new Classe[size];
        }
    };

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
        dest.writeString(id);
        dest.writeString(nom);
    }
}