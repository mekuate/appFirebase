package com.mekuate.kyala.model.Persistence;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mekuate.kyala.model.entities.Classe;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.Niveau;
import com.mekuate.kyala.model.entities.quiz.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mekuate on 11/07/2017.
 */

public final class FirebaseHelper {

     List <Epreuve> epreuves = new ArrayList<Epreuve>();
    Epreuve epreuve;
    Matiere matiere;
    Classe classe;
    Niveau niveau;
    public FirebaseHelper(){

    }

// get liste des quiz d'une classe, matiere,niveau - firebase deep

    //llste des épreuve non resolus de classeID, de matiereID et de niveauId


    public List<Epreuve> getEpreuves(String classeId, String matiereId, String niveauId){

        DatabaseReference refEpreuve = FirebaseDatabase.getInstance().getReference("epreuve");
        Query epreuve_clase = refEpreuve.orderByChild("classe").equalTo(classeId);
        Query epreuve_classe_matiere = epreuve_clase.orderByChild("matiere").equalTo(matiereId);
        Query epreuve_classe_matiere_niveau = epreuve_classe_matiere.orderByChild("niveau").equalTo(niveauId);
        epreuve_classe_matiere_niveau.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                epreuves.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        Epreuve epreuve= ds.getValue(Epreuve.class);
                        epreuves.add(epreuve);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return epreuves;
    }

    //randomly get une epreuve

    public Epreuve getRandomEpreuve(List <Epreuve> epreuveList){
        // Get some random valid index from the list.
        int index = new Random().nextInt(epreuveList.size());
// Remove the question from the list and store it into a variable.
        Epreuve epreuve = epreuveList.get(index);
        return  epreuve;
    }

    //les quiz d'une epreuve
    public List <Quiz> getQuizEpreuve(String epreuveID){
        final List <Quiz> quizs = new ArrayList<Quiz>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("epreuve").child(epreuveID).child("quiz");
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
       ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String quizKey =  ds.getKey();

                    rootRef.child("quiz").child(quizKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Quiz quiz = dataSnapshot.getValue(Quiz.class);
                            quizs.add(quiz);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return quizs;
    }

    // get liste of quiz to play according to classe,matiere,niveau

    public List <Quiz> getQuizToPlayEpreuve(String classeID, String matiereID, String niveauID){
        List <Quiz> quizs = new ArrayList<Quiz>();
        List<Epreuve> epreuveList = getEpreuves(classeID,matiereID,niveauID);
        Epreuve epreuve = getRandomEpreuve(epreuveList);
        quizs = getQuizEpreuve(epreuve.getId());
         return quizs;
    }

    // get classe, matiere and niveau knowing their ID

    public Classe getClasseWith(String ID){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classe");
        Query query=  ref.orderByKey().equalTo(ID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     classe = dataSnapshot.getValue(Classe.class);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return classe;
    }

    public Matiere getMatiere(String ID){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("matiere");
        Query query = ref.orderByKey().equalTo(ID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                      matiere = dataSnapshot.getValue(Matiere.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return matiere;
    }

    public Niveau getNiveauWith(String ID){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("niveau").child(ID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    niveau = dataSnapshot.getValue(Niveau.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return niveau;
    }



}




