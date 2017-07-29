package com.mekuate.kyala.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.transition.Slide;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.Persistence.PlainData;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.ui.ui.Jouer.JeuDemarrer;
import com.mekuate.kyala.ui.ui.login.CLogin;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class InterfaceAccueil extends Activity {
    private View mContentView;
    public User mUser;
    public FirebaseAuth mAuth;
public PlainData data ;
    FirebaseUser firebaseUser ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set exit transition
        setupWindowAnimations();
        setContentView(R.layout.activity_interface_accueil);
        // Initialize Firebase Auth
        data = new PlainData();
       // data.initializeNiveau();

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mContentView = findViewById(R.id.fullscreen_content);
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mContentView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // close after 2s and fade out transition
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startComponent();
                finish();
            }
        }, 5000);

    }

    public boolean checkLoggedIn(){
        Intent i;
       if((FirebaseAuth.getInstance().getCurrentUser())!=null){
           return true;
       }else
        return false;
    }
    private void setupWindowAnimations() {
        Slide fade = new Slide();
        fade.setDuration(2000);
        getWindow().setExitTransition(fade);

    }
    //TODO - VERIFIER SI USER EST CONNECTER ET OUVIR LE FORM CORRESPOND
    private void  startComponent(){

        Intent mainIntent;

        if(checkLoggedIn()) {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
            ref.keepSynced(true);
            ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        //gerer le cas ou l'utilisateur est enregistre dans firebase avec sa classe
                        mUser = dataSnapshot.getValue(User.class);
                        Intent intent = new Intent(InterfaceAccueil.this, JeuDemarrer.class);
                        intent.putExtra("User", (Parcelable) mUser);
                        startActivity(intent);



                    }else{
                        startActivity(new Intent(InterfaceAccueil.this, CLogin.class));
                        // do nothing
                        //Toast.makeText(InterfaceAccueil.this,"Pas cette utilisateur dans BD Firebase", Toast.LENGTH_LONG).show();
                }


            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            mainIntent = new Intent(InterfaceAccueil.this, CLogin.class);
            startActivity(mainIntent);
        }
    }



}
