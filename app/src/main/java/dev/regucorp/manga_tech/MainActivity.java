package dev.regucorp.manga_tech;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
        checkUpdate();

        ImageButton button = findViewById(R.id.add_button);
        button.setOnClickListener(showDialogBtn -> {
            AlertDialog dialog = createDialog(R.layout.add_entry_dialog);
            dialog.show();

            Button submit = dialog.findViewById(R.id.dialog_add_manga);
            submit.setOnClickListener(addMangaBtn -> addEntry(dialog));
        });
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

    private String getValue(AlertDialog d, int resid) {
        EditText e = d.findViewById(resid);
        return e.getText().toString();
    }

    private void checkUpdate() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String TAG = "Volley stuff";
        String updateUrl = getString(R.string.update_url), versionUrl = getString(R.string.version_url);
        String curVersion = getString(R.string.app_version);

        StringRequest sr = new StringRequest(Request.Method.GET, versionUrl, version -> {
            if(!version.trim().equals(curVersion)) {
                AlertDialog dialog = createDialog(R.layout.new_version_dialog);
                dialog.show();

                dialog.findViewById(R.id.launch_website).setOnClickListener(v -> {
                    // Send dude to browser
                    Uri uri = Uri.parse(updateUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                });
            }
        }, error -> {
            Log.e(TAG, "checkUpdate: ", error);
        });

        queue.add(sr);
    }


}