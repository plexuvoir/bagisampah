package com.example.bagisampah;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    PageAdapter(FragmentManager fm, int numOfTabs){
        super(fm);
        this.numOfTabs=numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SemuaFragment();
            case 1:
                return new PlastikFragment();
            case 2:
                return new KertasFragment();
            case 3:
                return new TekstilFragment();
            case 4:
                return  new KalengFragment();
            case 5:
                return  new KacaFragment();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
