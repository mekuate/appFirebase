package com.mekuate.kyala.model.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mekuate.kyala.model.entities.Classe;
import com.mekuate.kyala.model.entities.User;

import layout.ChallengeFragment;
import layout.EpreuveFragment;

/**
 * Created by Mekuate on 27/06/2017.
 */

public class FragmentTabAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    private Classe classe;
    private User user;

    public FragmentTabAdapter(FragmentManager fm, int NumOfTabs, User user) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.classe =classe;
        this.user=user;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EpreuveFragment tab1 = new EpreuveFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("User", (Parcelable)user);
                tab1.setArguments(bundle);

                return tab1;

            case 1:
                ChallengeFragment tab2 = new ChallengeFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("User", (Parcelable)user);
                tab2.setArguments(bundle1);
                return tab2;
            case 2:
                ChallengeFragment tab3 = new ChallengeFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("User", (Parcelable)user);
                tab3.setArguments(bundle2);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
