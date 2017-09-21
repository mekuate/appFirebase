package com.mekuate.kyala.model.Persistence;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.model.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Mekuate on 11/07/2017.
 */


public final class FirebaseHelper {

    List <Epreuve> epreuveList = new ArrayList<Epreuve>();
    List <String> epreuveResolu = new ArrayList<String>();
    List<Quize> quizs = new ArrayList<Quize>();
    Epreuve epreuve;
    Matiere matiere;
    Classe classe;
    Niveau niveau;
    User user;

    public FirebaseHelper(){

    }

   public interface CallbackFunction {
        void doWithEpreuve(Epreuve epreuve);
        void doWithQuiz( Epreuve epreuve, List <Quize> quizs);

    }
// set quiz score an
public void setEpreuveScore(Epreuve epreuve){
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("epreuve");
    HashMap<String, Object> Epr = new HashMap<String, Object>();
    Epr.put(epreuve.getId(), epreuve.getScore());
    ref.setValue(Epr);

}


    public void getEpreuve(final String classeId, final String matiereId, final String niveauId, final CallbackFunction callbackFunction){
                // get liste des epreuves
                DatabaseReference refEpreuve = FirebaseDatabase.getInstance().getReference("epreuve");
                Query refEpreuve_classe =  refEpreuve.orderByChild("classe").equalTo(classeId);
                refEpreuve_classe.keepSynced(true);
                refEpreuve_classe.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        epreuveList.clear();
                        //GenericTypeIndicator <Epreuve> genericTypeIndicator= new GenericTypeIndicator<Epreuve>(){};
                        for(DataSnapshot ds:dataSnapshot.getChildren()){

                            epreuve= ds.getValue(Epreuve.class);
                            if(epreuve.getMatiere().equals(matiereId)  && epreuve.getNiveau().equals(niveauId)){
                                // 1- get liste des epreuves de la classe, matiere, niveau selectionne
                                // 2- verifie si epreuve n'es pas encore resolu
                                    epreuveList.add(epreuve);




                            }

                            // 2 - randomly get an epreuve
                            // Get some random valid index from the list.

                                int index = new Random().nextInt(epreuveList.size());
                                // Remove the question from the list and store it into a variable.
                                epreuve = epreuveList.get(index);


                            callbackFunction.doWithEpreuve(epreuve);

                        }

                        if(epreuve!=null){


                        // Get all the quiz of an epreuve
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("epreuve").child(epreuve.getId()).child("quiz");

                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("quiz");
                        //keep quiz locally synced
                        rootRef.keepSynced(true);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                quizs.clear();
                                epreuve.setQuizzes(quizs);
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    String quizKey =  ds.getKey();
                                    final DatabaseReference refQuiz = rootRef.child(quizKey);
                                    refQuiz.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Quize quiz = dataSnapshot.getValue(Quize.class);
                                            quizs.add(quiz);
                                            epreuve.setQuizzes(quizs);
                                            callbackFunction.doWithQuiz(epreuve,quizs);




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

                        //finsi de epreuve different de null
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






    }

}




