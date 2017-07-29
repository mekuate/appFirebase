package com.mekuate.kyala.model.Persistence;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mekuate.kyala.model.entities.Niveau;

/**
 * Created by Mekuate on 26/07/2017.
 */

public  class PlainData {
   public PlainData(){}

    public void initializeNiveau(){
        //niveau 1
        Niveau n1 = new Niveau("Trimestre 1");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String id = ref.child("niveau").push().getKey();
        n1.setId(id);
        ref.child("niveau").child(id).setValue(n1);

        //niveau 2
        Niveau n2 = new Niveau("Trimestre 2");
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        String id1 = ref2.child("niveau").push().getKey();
        n2.setId(id1);
        ref2.child("niveau").child(id1).setValue(n2);

        //niveau 3
        Niveau n3 = new Niveau("Trimestre 3");
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference();
        String id3 = ref3.child("niveau").push().getKey();
        n3.setId(id3);
        ref3.child("niveau").child(id3).setValue(n3);

        //niveau 4
        Niveau n4 = new Niveau("Baccalauréat");
        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference();
        String id4 = ref4.child("niveau").push().getKey();
        n4.setId(id4);
        ref4.child("niveau").child(id4).setValue(n4);

    }
}
