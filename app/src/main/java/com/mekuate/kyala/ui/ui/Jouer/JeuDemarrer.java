package com.mekuate.kyala.ui.ui.Jouer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mekuate.kyala.R;
import com.mekuate.kyala.model.adapter.FragmentTabAdapter;
import com.mekuate.kyala.model.entities.Classe;
import com.mekuate.kyala.model.entities.User;
import com.mekuate.kyala.utils.ActivityHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JeuDemarrer extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private CircleImageView profilePicture;
    private User mUser;
    private Classe classe;
    TextView classeUserTextView;
    private TextView nomUser;
    private Toolbar toolbar;
    private ActivityHelper activityHelper = new ActivityHelper();
    private TableLayout tableLayout;
    private FloatingActionButton btnEpreuve;
    FirebaseUser firebaseUser;
    private List <Classe> classeList = new ArrayList<Classe>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu_demarrer);
        activityHelper.hideStatusBar(this.getWindow());
        this.setToolbar();
        setUser();


        this.setTableLayout();
        // find btnstartepreuve from a fragment
        //hide status bar


    }


    public void setUser() {
        Intent intent = getIntent();
        intent.setExtrasClassLoader(User.class.getClassLoader());
        mUser = intent.getParcelableExtra("User");
        //classe= intent.getParcelableExtra("Classe");
        this.setProfilePicture();
        this.setClasseUser();
        this.setNomUser();

    }

    public void setProfilePicture() {
        profilePicture = (CircleImageView) findViewById(R.id.image_profile);
        Picasso.with(this).load(mUser.getPhoto()).networkPolicy(NetworkPolicy.OFFLINE).into(profilePicture, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(JeuDemarrer.this).load(mUser.getPhoto()).into(profilePicture);
            }
        });
    }

    public void setClasseUser() {
        classeUserTextView = (TextView) findViewById(R.id.classe);
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("classe");

     // Query query = ref1.orderByKey().equalTo(mUser.getClasse());
        ref1.child(mUser.getClasse()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.exists()){
                   classe = dataSnapshot.getValue(Classe.class);
                   classeUserTextView.setText(classe.getNom());
               }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void setNomUser() {
         nomUser = (TextView) findViewById(R.id.nom_eleve);
        nomUser.setText(mUser.getNom());
    }

    public void setToolbar() {
         toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
    }

    public void setTableLayout() {
        //add fragment to tablayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_en_classe));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_competition));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_concours));
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final FragmentTabAdapter adapter = new FragmentTabAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount(), mUser);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuactionbar, menu);
        return true;
    }


}
