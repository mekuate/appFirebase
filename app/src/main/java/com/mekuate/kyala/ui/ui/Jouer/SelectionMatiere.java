package com.mekuate.kyala.ui.ui.Jouer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Matiere;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.view.MatiereViewHolder;

public class SelectionMatiere extends AppCompatActivity {
    private CardView cardviewmatiere;
    private String niveauID;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentArgument();
        setContentView(R.layout.activity_selection_matiere);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //hide status bar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        //build matiere
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMatiere);
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(glm);
        //les matieres de la classe de l'utilisateur
        final DatabaseReference roofref = FirebaseDatabase.getInstance().getReference("matiere");
        roofref.keepSynced(true);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classe").child(user.getClasse()).child("matiere");
        final FirebaseRecyclerAdapter<Boolean, MatiereViewHolder> adapter1 = new FirebaseRecyclerAdapter<Boolean, MatiereViewHolder>(
                Boolean.class,
                R.layout.matiere_list,
                MatiereViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(final MatiereViewHolder viewHolder, Boolean model, final int position) {
                String matiereKey = getRef(position).getKey();

                roofref.child(matiereKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Matiere matiere = dataSnapshot.getValue(Matiere.class);
                        viewHolder.nomMatiereTextView.setText(matiere.getNom());
                        viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //open quiz activity with all data: user, niveau, matiere
                                Intent intent= new Intent(SelectionMatiere.this, PartieExercice.class);
                                intent.putExtra("User", (Parcelable)user);
                                intent.putExtra("niveauKey",niveauID);
                                intent.putExtra("matiereKey", getRef(position).getKey());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });
            }



        };
        recyclerView.setAdapter(adapter1);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getFragmentArgument(){
        Intent intent = getIntent();
        intent.setExtrasClassLoader(User.class.getClassLoader());
        user = intent.getParcelableExtra("User");
       niveauID =  intent.getStringExtra("niveau");

    }
}
