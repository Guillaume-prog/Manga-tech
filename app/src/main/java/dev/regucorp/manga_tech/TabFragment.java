package dev.regucorp.manga_tech;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.regucorp.manga_tech.data.MangaEntry;
import dev.regucorp.manga_tech.data.MangaModel;

public class TabFragment extends Fragment {

    private String TAG = "TabFragment";
    private int numEntries;
    private MangaEntry[] entries;

    private LinearLayout ll;

    public TabFragment(MangaEntry[] entries) {
        numEntries = 0;
        this.entries = entries;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment, container, false);
        ll = v.findViewById(R.id.fragment_list);

        init();

        return v;
    }

    public void init() {
        if(entries != null) {
            for(MangaEntry e : entries) {
                addManga(e);
            }
        } else {
            showNoEntries();
        }
    }

    public void addManga(MangaEntry entry) {
        View v = getLayoutInflater().inflate(R.layout.manga_component, null);

        setText(v, R.id.manga_name, entry.getName());
        setText(v, R.id.manga_person, entry.getPerson());
        setText(v, R.id.manga_vols, "4/12");

        if(numEntries == 0) ll.removeAllViews();
        ll.addView(v);
        numEntries++;
    }

    private void setText(View v, int resid, String text) {
        TextView tv = v.findViewById(resid);
        tv.setText(text);
    }

    private void showNoEntries() {
        TextView tv = new TextView(getContext());
        tv.setText("No mangas found");
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0, 100, 0, 0);
        ll.addView(tv);
    }

    
}