package com.example.bagisampah.View.MainMenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.bagisampah.View.Search.KacaFragment;
import com.example.bagisampah.View.Search.KalengFragment;
import com.example.bagisampah.View.Search.KertasFragment;
import com.example.bagisampah.View.Search.PlastikFragment;
import com.example.bagisampah.View.Search.SemuaFragment;
import com.example.bagisampah.View.Search.TekstilFragment;

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
