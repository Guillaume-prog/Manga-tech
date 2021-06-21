package dev.regucorp.manga_tech;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.regucorp.manga_tech.data.MangaEntry;
import dev.regucorp.manga_tech.data.MangaModel;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private LinearLayout mangaList;
    private Button addMangaBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mangaList = findViewById(R.id.borrowed_list);
        addMangaBtn = findViewById(R.id.add_manga);

        addMangaBtn.setOnClickListener(v -> {
            addEntry();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mangaList.removeAllViews();
        showEntries(mangaList);
    }

    private void addEntry() {
        String name = getInputValue(R.id.manga_name);
        String person = getInputValue(R.id.manga_person);
        int start = Integer.parseInt(getInputValue(R.id.manga_start));
        int end = Integer.parseInt(getInputValue(R.id.manga_end));

        MangaEntry entry = new MangaEntry(MangaEntry.BORROW, person, name, start, end);
        MangaModel.getInstance().addEntry(db, entry);

        toast("Manga added to collection");
        showManga(mangaList, entry);
    }

    private void showEntries(LinearLayout mangaList) {
        MangaEntry mangas[] = MangaModel.getInstance().get(db, MangaEntry.BORROW);
        Log.d(TAG, "showEntries: "+mangas.length);
        if(mangas != null) {
            for (MangaEntry m : mangas) {
                Log.d(TAG, "showEntries: "+m);
                showManga(mangaList, m);
            }
        } else {
            showNoManga();
        }
    }

    private void showManga(LinearLayout mangaList, MangaEntry m) {
        View manga = load(R.layout.manga_component);

        setTextValue(manga, R.id.manga_name, m.getName());
        setTextValue(manga, R.id.manga_person, m.getPerson());
        setTextValue(manga, R.id.manga_vols, (m.getEndVolume() - m.getStartVolume() + 1) + " Volumes");

        mangaList.addView(manga);
    }

    private void showNoManga() {
        TextView tv = new TextView(this);
        tv.setText("No mangas in  collection");
        mangaList.addView(tv);
    }

    // Lazy functions

    private String getInputValue(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }

    private void setTextValue(View parent, int id, String value) {
        ((TextView) parent.findViewById(id)).setText(value);
    }
}