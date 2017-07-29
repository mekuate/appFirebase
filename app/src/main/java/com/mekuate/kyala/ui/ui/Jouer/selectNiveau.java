package com.mekuate.kyala.ui.ui.Jouer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mekuate.kyala.R;

public class selectNiveau extends AppCompatActivity {
    private CardView niveau_trimestre1;
    private CardView niveau_trimestre2;
    private CardView niveau_trimestre3;
    private CardView niveau_examen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_niveau);
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

        //gestion des clics sur les images
        niveau_trimestre1 = (CardView) findViewById(R.id.card_premier_trimestre);
        niveau_trimestre2 = (CardView) findViewById(R.id.card_deuxieme_trimestre);
        niveau_trimestre3 = (CardView) findViewById(R.id.card_troisieme_trimestre);
        niveau_examen = (CardView) findViewById(R.id.card_examen);

        niveau_trimestre1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(selectNiveau.this, SelectionMatiere.class));
            }
        });
        niveau_trimestre2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(selectNiveau.this, SelectionMatiere.class));
            }
        });

        niveau_trimestre3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(selectNiveau.this, SelectionMatiere.class));
            }
        });
        niveau_examen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(selectNiveau.this, SelectionMatiere.class));
            }
        });
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
}
