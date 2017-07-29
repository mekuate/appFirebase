package com.mekuate.kyala.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mekuate.kyala.R;

/**
 * Created by Mekuate on 12/07/2017.
 */

public class ClasseViewHolder extends RecyclerView.ViewHolder  {
    public TextView nomClassefield;
    public CardView cardViewClasse;
    public  View mView;

    public ClasseViewHolder(View itemView) {
        super(itemView);
        this.nomClassefield= (TextView) itemView.findViewById(R.id.nom_classe);
        this.cardViewClasse = (CardView) itemView.findViewById(R.id.card_classe);
        this.mView =itemView;
    }


}
