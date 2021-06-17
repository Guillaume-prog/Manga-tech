package dev.regucorp.manga_tech;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.regucorp.manga_tech.data.models.MangaModel;

public class MainActivity extends BaseActivity {

    private LinearLayout mangaList;
    private Button addMangaBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mangaList = findViewById(R.id.book_list);
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
        String name = ((EditText) findViewById(R.id.manga_name)).getText().toString();
        int volumes = Integer.parseInt(((EditText) findViewById(R.id.manga_volumes)).getText().toString());

        String ownedVols = new String(new char[volumes]).replace('\0', '0');
        MangaModel.getInstance().addEntry(db, name, volumes, ownedVols);

        toast("Manga added to collection");
        showManga(mangaList, name, ownedVols, volumes);
    }

    private void showEntries(LinearLayout mangaList) {
        Cursor mangas = MangaModel.getInstance().getEntries(db);

        if(mangas.moveToFirst()) {
            do {
                showManga(mangaList, mangas.getString(MangaModel.MANGA_NAME), mangas.getString(MangaModel.MANGA_OWNED), mangas.getInt(MangaModel.MANGA_VOLUMES));
            } while(mangas.moveToNext());
        } else {
            showNoManga();
        }
    }

    private void showManga(LinearLayout mangaList, String name, String owned, int volumes) {

        View manga = load(R.layout.manga_component);

        ((TextView) manga.findViewById(R.id.manga_entry_name)).setText(name);
        ((TextView) manga.findViewById(R.id.manga_entry_num_vols_owned)).setText(String.valueOf(getNumManga(owned)));
        ((TextView) manga.findViewById(R.id.manga_entry_num_vols)).setText(String.valueOf(volumes));

        manga.setOnClickListener(v -> {
            // Create intent for other window
            Intent i = new Intent(getApplicationContext(), MangaViewActivity.class);
            i.putExtra("manga_title", name);
            i.putExtra("manga_num_vols", volumes);
            i.putExtra("manga_vols_owned", owned);

            startActivity(i);
        });

        manga.setOnLongClickListener(v -> {
            MangaModel.getInstance().deleteManga(db, name);
            mangaList.removeView(manga);
            toast("Removed '"+ name +"' from list");
            if(mangaList.getChildCount() == 0) showNoManga();
            return true;
        });

        mangaList.addView(manga);
    }

    private void showNoManga() {
        TextView tv = new TextView(this);
        tv.setText("No mangas in  collection");
        mangaList.addView(tv);
    }

    private int getNumManga(String owned) {
        int num = 0;
        for(char c : owned.toCharArray()) {
            if(c == '1') num++;
        }
        return num;
    }
}