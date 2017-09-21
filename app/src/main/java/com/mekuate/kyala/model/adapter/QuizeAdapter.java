package com.mekuate.kyala.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Quize;
import com.mekuate.kyala.view.QuizViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mekuate on 03/08/2017.
 */

public class QuizeAdapter extends BaseAdapter {
        private Epreuve epreuve;
    private List <Quize> quizes;
    private Context context;
    private LayoutInflater inflater;
    public QuizeAdapter(Context c, Epreuve epreuve, List<Quize> quizes) {
        this.epreuve=epreuve;
        this.quizes = quizes;
        this.context =c;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return quizes.size();
    }

    @Override
    public Object getItem(int position) {
        return quizes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(quizes.get(position).getId());
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuizViewHolder holder = null;
        if(convertView == null){
           convertView = inflater.inflate(R.layout.quiz_item, parent, false);
             holder = new QuizViewHolder(convertView);

        }else{
            holder = (QuizViewHolder) convertView.getTag();
        }
        //get a question
        String question = quizes.get(position).getQuestion();
        holder.questionTextview.setText(question);
       // set options

        //get options
       ListView mListView = new ListView(this.context);
        HashMap <String,String > options= quizes.get(position).getOptions();
        mListView.setDivider(null);
        mListView.setSelector(R.drawable.selector_button);
        mListView.setAdapter(new OptionsQuizAdapter(options.keySet().toArray(new String[0]), R.layout.item_answer_start,
                        context, true));
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        return convertView;
    }
}
