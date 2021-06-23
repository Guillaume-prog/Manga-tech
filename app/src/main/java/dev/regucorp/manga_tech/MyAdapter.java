package dev.regucorp.manga_tech;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;

    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm, totalTabs);

        this.context = c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BlankFragment b = new BlankFragment("Borrowed Manga");
                return b;
            case 1:
                BlankFragment l = new BlankFragment("Lent Manga");
                return l;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

}
