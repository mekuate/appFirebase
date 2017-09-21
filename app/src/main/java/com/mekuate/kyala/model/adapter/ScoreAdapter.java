package com.mekuate.kyala.model.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mekuate.kyala.R;
import com.mekuate.kyala.model.entities.Epreuve;
import com.mekuate.kyala.model.entities.Quize;

import java.util.List;

/**
 * Created by Mekuate on 14/08/2017.
 */

public class ScoreAdapter extends BaseAdapter {

    private final Epreuve epreuve;
    private final int count;
    private final List<Quize> mQuizList;

    private Drawable mSuccessIcon;
    private Drawable mFailedIcon;

    public ScoreAdapter(Epreuve epreuve) {
        this.epreuve = epreuve;
        mQuizList = epreuve.getQuizzes();
        count = mQuizList.size();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Quize getItem(int position) {
        return mQuizList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position > count || position < 0) {
            return AbsListView.INVALID_POSITION;
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = createView(parent);
        }

        final Quize quize = getItem(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.mQuizView.setText(quize.getQuestion());

        viewHolder.mAnswerView.setText(quize.getStringAnswer());
        setSolvedStateForQuiz(viewHolder.mSolvedState, position);
        return convertView;
    }

    private void setSolvedStateForQuiz(ImageView solvedState, int position) {
        final Context context = solvedState.getContext();
        final Drawable tintedImage;
        if (this.epreuve.isSolvedCorrectly(getItem(position))) {
            tintedImage = getSuccessIcon(context);
        } else {
            tintedImage = getFailedIcon(context);
        }
        solvedState.setImageDrawable(tintedImage);
    }

    private Drawable getSuccessIcon(Context context) {
        if (null == mSuccessIcon) {
           // mSuccessIcon = loadAndTint(context, R.drawable.ic_tick, R.color.colorVert_light);
            mSuccessIcon = ContextCompat.getDrawable(context, R.drawable.ic_check);
            if (mSuccessIcon == null) {
                throw new IllegalArgumentException("The drawable with id " + R.drawable.ic_check
                        + " does not exist");
            }

           // DrawableCompat.setTint(DrawableCompat.wrap(mSuccessIcon),  R.color.colorVert_light);
        }
        return mSuccessIcon;
    }

    private Drawable getFailedIcon(Context context) {
        if (null == mFailedIcon) {
            //mFailedIcon = loadAndTint(context, R.drawable.ic_cross, R.color.colorRed);
            mFailedIcon = ContextCompat.getDrawable(context, R.drawable.ic_clear);
            if (mFailedIcon == null) {
                throw new IllegalArgumentException("The drawable with id " + R.drawable.ic_cross
                        + " does not exist");
            }
            //DrawableCompat.setTint(DrawableCompat.wrap(mFailedIcon), R.color.colorRed);
        }
        return mFailedIcon;
    }



    private View createView(ViewGroup parent) {
        View convertView;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup scorecardItem = (ViewGroup) inflater.inflate(
                R.layout.item_scorecard, parent, false);
        convertView = scorecardItem;
        ViewHolder holder = new ViewHolder(scorecardItem);
        convertView.setTag(holder);
        return convertView;
    }

    private class ViewHolder {

        final TextView mAnswerView;
        final TextView mQuizView;
        final ImageView mSolvedState;

        public ViewHolder(ViewGroup scorecardItem) {
            mQuizView = (TextView) scorecardItem.findViewById(R.id.quiz);
            mAnswerView = (TextView) scorecardItem.findViewById(R.id.answer);
            mAnswerView.setTypeface(null, Typeface.BOLD);
            mSolvedState = (ImageView) scorecardItem.findViewById(R.id.solved_state);
        }

    }
}
