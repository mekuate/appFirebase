package com.mekuate.kyala.ui.ui.widget.Fab;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.mekuate.kyala.R;

/**
 * Created by Mekuate on 10/08/2017.
 */

public class CheckableFab extends FloatingActionButton implements Checkable {

    private static final int[] CHECKED = {android.R.attr.state_checked};

    private boolean mIsChecked = true;


    public CheckableFab(Context context) {
        this(context, null);
    }

    public CheckableFab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableFab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setImageResource(R.drawable.ic_tick);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(++extraSpace);
        if (mIsChecked) {
            mergeDrawableStates(drawableState, CHECKED);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mIsChecked == checked) {
            return;
        }
        mIsChecked = checked;
        refreshDrawableState();
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }
}
