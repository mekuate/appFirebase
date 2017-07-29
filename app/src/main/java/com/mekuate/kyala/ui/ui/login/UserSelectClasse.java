package com.mekuate.kyala.ui.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Classe;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.ui.ui.Jouer.JeuDemarrer;
import com.mekuate.kyala.view.ClasseViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSelectClasse extends AppCompatActivity {

    private CircleImageView profilePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select_classe);
        //hide status bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        // get users data
        Intent intent = getIntent();
        intent.setExtrasClassLoader(User.class.getClassLoader());
        final User mUser = intent.getParcelableExtra("User");
         profilePicture = (CircleImageView) findViewById(R.id.profile);
        // (optional) use Picasso to download and show to image
        //image play with offline capability
        Picasso.with(this).load(mUser.getPhoto()).networkPolicy(NetworkPolicy.OFFLINE).into(profilePicture, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(UserSelectClasse.this).load(mUser.getPhoto()).into(profilePicture);
            }
        });

        TextView nom = (TextView) findViewById(R.id.nom);
        nom.setText(mUser.getNom());

        // Liste des classes et des inscrits de chaque classe
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
         RecyclerView recyclerView = (RecyclerView) findViewById(R.id.classe_content_view);
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
            protected void populateViewHolder(ClasseViewHolder viewHolder, Classe model, final int position) {
                viewHolder.nomClassefield.setText(model.getNom());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Classe selectedClasse= getItem(position);
                        //update Firebase user or insert firebaseuser dans basededonnees
                        mUser.setClasse(selectedClasse.getId());
                        DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");
                        refUser.child(mUser.getId()).setValue(mUser);
                        refUser.keepSynced(true);
                        // open main activity classe with user information
                        Intent intent = new Intent(UserSelectClasse.this, JeuDemarrer.class);
                        intent.putExtra("User", (Parcelable) mUser);
                        intent.putExtra("Classe", (Parcelable)selectedClasse);
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



}
