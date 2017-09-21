package com.mekuate.kyala.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mekuate.kyala.R;

/**
 * Created by Mekuate on 03/08/2017.
 */

public class QuizViewHolder extends RecyclerView.ViewHolder {
        public TextView questionTextview;
        public View mView;



    public QuizViewHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
        questionTextview = (TextView)itemView.findViewById(R.id.question_view);
    }
}
