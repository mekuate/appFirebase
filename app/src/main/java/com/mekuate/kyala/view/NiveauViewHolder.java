package com.mekuate.kyala.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mekuate.kyala.R;

/**
 * Created by Mekuate on 13/07/2017.
 */

public class NiveauViewHolder extends RecyclerView.ViewHolder {
    public TextView nomNiveauTextView;
    public CardView cardViewNiveau;
    public  View mView;

    public NiveauViewHolder(View itemView) {
        super(itemView);
        this.mView =itemView;
        this.cardViewNiveau = (CardView) itemView.findViewById(R.id.card_niveau);
        this.nomNiveauTextView= (TextView) itemView.findViewById(R.id.nom_niveau);

    }
}
