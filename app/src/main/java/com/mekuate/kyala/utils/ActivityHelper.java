package com.mekuate.kyala.utils;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.mekuate.kyala.R;

/**
 * Created by Mekuate on 14/07/2017.
 */

public final class ActivityHelper extends AppCompatActivity {
    private Window window;
    public ActivityHelper() {
    }

    public void hideStatusBar(Window w){
        //hide status bar
        View decorView = w.getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    //set icon for niveau

    public Drawable setNiveauIcon(String name){
        android.content.res.Resources res;
        res= getResources();
        switch (name){
            case "level1":

                return res.getDrawable(R.drawable.levelone, null);
            case "level2":
                return res.getDrawable(R.drawable.leveltwo, null);
            case "level3":
                return res.getDrawable(R.drawable.levelthree, null);
            case "examen":
                return res.getDrawable(R.drawable.levelexamen, null);
        }
        return null;

    }
}
