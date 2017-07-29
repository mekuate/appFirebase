package com.mekuate.kyala.ui.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.ui.ui.Jouer.JeuDemarrer;

import static android.content.ContentValues.TAG;

public class CLogin extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    User modelUser = null;
    FirebaseDatabase ref;
    FirebaseUser firebaseUser;
    LoginButton loginButton;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        this.setTitle("Se connecter");
        setContentView(R.layout.activity_clogin);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        // If using in a fragment
        //loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                loginButton.setVisibility(View.INVISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {
                // TODO: 30/05/2017: annulation de l'enregistrement
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setupWindowAnimations() {


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = mAuth.getCurrentUser();
                            //verifier si l'utilisateur est eneregistre dans firebase avec sa classe
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                            //activate offline data user
                            ref.keepSynced(true);

                            ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        //gerer le cas ou l'utilisateur est enregistre dans firebase avec sa classe

                                        modelUser = dataSnapshot.getValue(User.class);
                                        Intent intent = new Intent(CLogin.this, JeuDemarrer.class);
                                        intent.putExtra("User", (Parcelable) modelUser);
                                        startActivity(intent);

                                    } else {
                                        String photoUrl = "https://graph.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture?type=large";
                                        modelUser = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), photoUrl);
                                        Intent intent = new Intent(CLogin.this, UserSelectClasse.class);
                                        intent.putExtra("User", (Parcelable) modelUser);
                                        startActivity(intent);


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(CLogin.this, "Echec de connexion, Verifie ta connexion Internet.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    /**public void updateUI(){
     //verifier si l'utilisateur est eneregistre dans firebase avec sa classe
     DatabaseReference ref= FirebaseDatabase.getInstance().getReference("users");
     ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override public void onDataChange(DataSnapshot dataSnapshot) {
    if(dataSnapshot.exists()){
    //gerer le cas ou l'utilisateur est enregistre dans firebase avec sa classe
    modelUser = dataSnapshot.getValue(User.class);
    Intent intent = new Intent(CLogin.this, JeuDemarrer.class);
    intent.putExtra("User", (Parcelable) modelUser);
    startActivity(intent);

    } else {
    String photoUrl = "https://graph.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture?type=large";
    modelUser = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(),photoUrl);
    Intent intent = new Intent(CLogin.this, UserSelectClasse.class);
    intent.putExtra("User", (Parcelable) modelUser);
    startActivity(intent);


    }
    }

    @Override public void onCancelled(DatabaseError databaseError) {

    }
    });





     }*/



}
