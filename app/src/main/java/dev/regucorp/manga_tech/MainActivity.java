package dev.regucorp.manga_tech;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
        button.setOnClickListener(showDialogBtn -> {
            AlertDialog dialog = createDialog();
            dialog.show();

            Button submit = dialog.findViewById(R.id.dialog_add_manga);
            submit.setOnClickListener(addMangaBtn -> addEntry(dialog));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void addEntry(AlertDialog d) {
        String mangaName = getValue(d, R.id.manga_name);
        String mangaPerson = getValue(d, R.id.manga_person);
        int mangaVols = Integer.parseInt(getValue(d, R.id.manga_vols));
        int type = (tabLayout.getSelectedTabPosition() == 0) ? MangaEntry.BORROW : MangaEntry.LEND;

        MangaEntry entry = new MangaEntry(type, mangaPerson, mangaName, mangaVols);
        MangaModel.getInstance().addEntry(db, entry);

        TabFragment fr = (TabFragment) adapter.getItem(tabLayout.getSelectedTabPosition());
        fr.addManga(entry);

        d.dismiss();
        toast("Added manga");
    }

    private void setupTabs() {
        adapter = new MyAdapter(getSupportFragmentManager());

        MangaEntry[] borrowed = MangaModel.getInstance().get(db, MangaEntry.BORROW);
        MangaEntry[] lent = MangaModel.getInstance().get(db, MangaEntry.LEND);

        adapter.add("Borrowed", new TabFragment(db, borrowed));
        adapter.add("Lent", new TabFragment(db, lent));

        adapter.setUpPager(tabLayout, viewPager);
    }

    private AlertDialog createDialog() {
        View dialogView = load(R.layout.add_entry_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(dialogView);
        return builder.create();
    }

    private String getValue(AlertDialog d, int resid) {
        EditText e = d.findViewById(resid);
        return e.getText().toString();
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