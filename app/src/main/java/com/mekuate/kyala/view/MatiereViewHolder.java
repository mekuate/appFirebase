package com.mekuate.kyala.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mekuate.kyala.R;

/**
 * Created by Mekuate on 21/07/2017.
 */

public class MatiereViewHolder extends RecyclerView.ViewHolder {
    public TextView nomMatiereTextView;
    //public ImageView imageMatiereView;
    public View mview;

    public MatiereViewHolder(View itemView) {
        super(itemView);
        this.nomMatiereTextView = (TextView) itemView.findViewById(R.id.nom_matiere);
        //this.imageMatiereView = (ImageView) itemView.findViewById(R.id.image_matiere);
        this.mview= itemView;
    }
}
