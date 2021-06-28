package dev.regucorp.manga_tech;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import dev.regucorp.manga_tech.data.MangaEntry;
import dev.regucorp.manga_tech.data.MangaModel;

public class MainActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        setupTabs();

        ImageButton button = findViewById(R.id.add_button);
        button.setOnClickListener(v -> {
            TabFragment fr = (TabFragment) adapter.getItem(tabLayout.getSelectedTabPosition());
            fr.addManga(new MangaEntry(MangaEntry.BORROW, "Johnny D.", "Deathnote", 4, 10));
        });
    }

    private void setupTabs() {
        adapter = new MyAdapter(getSupportFragmentManager());

        MangaEntry[] borrowed = MangaModel.getInstance().get(db, MangaEntry.BORROW);
        MangaEntry[] lent = MangaModel.getInstance().get(db, MangaEntry.LEND);

        adapter.add("Borrowed", new TabFragment(borrowed));
        adapter.add("Lent", new TabFragment(lent));

        adapter.setUpPager(tabLayout, viewPager);
    }



    /*private void addEntry() {
        String name = getInputValue(R.id.manga_name);
        String person = getInputValue(R.id.manga_person);
        int start = Integer.parseInt(getInputValue(R.id.manga_start));
        int end = Integer.parseInt(getInputValue(R.id.manga_end));

        MangaEntry entry = new MangaEntry(MangaEntry.BORROW, person, name, start, end);
        MangaModel.getInstance().addEntry(db, entry);

        toast("Manga added to collection");
        showManga(mangaList, entry);
    }*/


}