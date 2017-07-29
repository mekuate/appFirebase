package com.mekuate.kyala.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mekuate on 20/05/2017.
 */

public class User implements Parcelable{
    private String nom;
    private String prenom;
    private String classe;
    private String etablissement;
    private String photo;
    private String email;
    private String telephone;
    private String id;

    public User(String nom, String prenom, String classe, String etablissement, String email, String  photo, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;
        this.etablissement = etablissement;
        this.email = email;
        this.photo = photo;
        this.telephone = telephone;
    }


    public User() {
    }

    protected User(Parcel in) {
        nom = in.readString();
        prenom = in.readString();
        classe = in.readString();
        etablissement = in.readString();
        photo = in.readString();
        email = in.readString();
        telephone = in.readString();
        id = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public User(String id,String nom, String email, String photo) {
        this.nom = nom;
        this.email = email;
        this.photo = photo;
        this.id =id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String nom, String id, String telephone, String email, String photo, String etablissement, String classe, String prenom) {
        this.nom = nom;
        this.id = id;
        this.telephone = telephone;
        this.email = email;
        this.photo = photo;
        this.etablissement = etablissement;
        this.classe = classe;
        this.prenom = prenom;
    }

    public User(String nom, String classe, String photo, String email, String id) {
        this.nom = nom;
        this.classe = classe;
        this.photo = photo;
        this.email = email;
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "User{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", classe='" + classe + '\'' +
                ", etablissement='" + etablissement + '\'' +
                ", photo=" + photo +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(classe);
        dest.writeString(etablissement);
        dest.writeString(photo);
        dest.writeString(email);
        dest.writeString(telephone);
        dest.writeString(id);
    }
}
