package com.mekuate.kyala.ui.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.mekuate.kyala.model.entities.Classe;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.ui.ui.Jouer.JeuDemarrer;
import com.mekuate.kyala.utils.ActivityHelper;
import com.mekuate.kyala.view.ClasseViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {

    private CircleImageView profilePicture;
    private TextView nomEditText;
    ActivityHelper activityHelper = new ActivityHelper();
    private  GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
    RecyclerView recyclerView;
    private ViewFlipper viewFlipper;
    private Button suivantButton;
    float initialX;
    Classe classe;
    private FirebaseAuth mAuth;
    CallbackManager callbackManager = CallbackManager.Factory.create();
    User modelUser = null;
    FirebaseUser firebaseUser;
    LoginButton loginButton;



    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged:signed_in:" + firebaseUser.getUid());
                //startPlay();


            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
    //end of listener for events
    //----------------

    // here I just setup UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_user_select_classe);
        activityHelper.hideStatusBar(this.getWindow());
        // JUST SETUP UI
           nomEditText = (TextView) findViewById(R.id.nom);
        profilePicture = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile);
         recyclerView = (RecyclerView) findViewById(R.id.classe_content_view);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        if(mAuth.getCurrentUser() != null){
            //startPlay();

        }

        CallbackRegistration();

    }

    // Load list of classe
    public void LoadDataClasse(){
        // Liste des classes et des inscrits de chaque classe
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classe");
        ref.keepSynced(true);
        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<Classe,ClasseViewHolder> adapter = new FirebaseRecyclerAdapter<Classe, ClasseViewHolder>(
                Classe.class,
                R.layout.classe_list,
                ClasseViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(final ClasseViewHolder viewHolder, Classe model, final int position) {
                viewHolder.nomClassefield.setText(model.getNom());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         classe= getItem(position);
                       v.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                        //update Firebase user or insert firebaseuser dans basededonnees
                        modelUser.setClasse(classe.getId());
                        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");
                        refUser.child(modelUser.getId()).setValue(modelUser);
                        refUser.keepSynced(true);
                        // open main activity classe with user information
                        Intent intent = new Intent(Login.this, JeuDemarrer.class);
                        intent.putExtra("User", (Parcelable) modelUser);
                        //intent.putExtra("Classe", (Parcelable)classe);
                        startActivity(intent);
                        finish();




                    }
                });


            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // register for events
        mAuth.addAuthStateListener(mAuthListener);
        // and load data
        LoadDataClasse();


    }

    private void CallbackRegistration(){
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
    protected void onStop() {
        super.onStop();
        //unregister from events
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                                        modelUser = dataSnapshot.getValue(User.class);
                                        Intent intent = new Intent(Login.this, JeuDemarrer.class);
                                        intent.putExtra("User", (Parcelable) modelUser);
                                        startActivity(intent);

                                    } else {
                                        String photoUrl = "https://graph.facebook.com/" + AccessToken.getCurrentAccessToken().getUserId() + "/picture?type=large";
                                        modelUser = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), photoUrl);
                                        SetUserProfile();
                                       viewFlipper.showNext();


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Echec de connexion, Verifie ta connexion Internet.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    private void SetUserProfile(){
        //image play with offline capability
        Picasso.with(this).load(modelUser.getPhoto()).networkPolicy(NetworkPolicy.OFFLINE).into(profilePicture, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(Login.this).load(modelUser.getPhoto()).into(profilePicture);
            }
        });

        nomEditText.setText(modelUser.getNom());
    }
}
