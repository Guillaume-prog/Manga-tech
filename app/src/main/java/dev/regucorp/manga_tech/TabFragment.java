package dev.regucorp.manga_tech;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.regucorp.manga_tech.data.DataHandler;
import dev.regucorp.manga_tech.data.MangaEntry;
import dev.regucorp.manga_tech.data.MangaModel;

public class TabFragment extends Fragment {

    private String TAG = "TabFragment";
    private List<MangaEntry> entries = new ArrayList<MangaEntry>();
    private MangaEntry[] startEntries;

    private DataHandler db;

    private LinearLayout ll;
    private TextView statsView;
    private View lastView;

    private int numSeries, totalVolumes, lastViewNumVols = 0;

    public TabFragment(DataHandler db, MangaEntry[] entryList) {
        this.db = db;
        this.startEntries = entryList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment, container, false);
        ll = v.findViewById(R.id.fragment_list);
        statsView = v.findViewById(R.id.manga_count);

        init();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(lastView != null) {
            TextView titleView = lastView.findViewById(R.id.manga_name);
            String title = titleView.getText().toString();

            MangaEntry updatedEntry = MangaModel.getInstance().getByName(db, title);

            totalVolumes += updatedEntry.getNumOwned() - lastViewNumVols;
            updateStats();

            int oldIndex = ll.indexOfChild(lastView);
            ll.removeView(lastView);

            ll.addView(genManga(updatedEntry), oldIndex);
            lastView = null;
        }
    }

    public void init() {
        numSeries = 0;
        totalVolumes = 0;

        if(startEntries != null) {
            for(MangaEntry e : startEntries) {
                addManga(e);
            }
        } else {
            showNoEntries();
        }
    }

    private void updateStats() {
        statsView.setText(numSeries + " series - " + totalVolumes + " volumes");
    }

    public void addManga(MangaEntry entry) {
        ll.addView(genManga(entry));
        numSeries++;
        totalVolumes += entry.getNumOwned();
        updateStats();
    }

    public View genManga(MangaEntry entry) {
        View v = getLayoutInflater().inflate(R.layout.manga_component, null);
        String percentage = entry.getNumOwned() + " Vols";

        setText(v, R.id.manga_name, entry.getName());
        setText(v, R.id.manga_person, entry.getPerson());
        setText(v, R.id.manga_vols, percentage);

        // Access manga vols
        v.setOnClickListener(view -> {
            lastView = v;
            lastViewNumVols = entry.getNumOwned();
            sendToActivity(entry);
        });

        // Delete
        v.setOnLongClickListener(view -> {
            Dialog d = createDialog(R.layout.confirm_delete_dialog);
            d.show();

            Button delBtn = d.findViewById(R.id.delete_entry);
            Button cancelBtn = d.findViewById(R.id.cancel_button);

            delBtn.setOnClickListener(button -> {
                ll.removeView(v);
                MangaModel.getInstance().deleteEntry(db, entry);

                numSeries--;
                totalVolumes -= entry.getNumOwned();
                updateStats();

                entries.remove(entry);
                if(entries.size() == 0) showNoEntries();
                d.dismiss();
            });

            cancelBtn.setOnClickListener(button -> d.dismiss());

            return true;
        });

        if(entries.size() == 0) ll.removeAllViews();
        entries.add(entry);

        return v;
    }

    protected AlertDialog createDialog(int layout_id) {
        View dialogView = getLayoutInflater().inflate(layout_id, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        return builder.create();
    }


    private void sendToActivity(MangaEntry e) {
        Intent intent = new Intent(getActivity(), MangaViewActivity.class);
        intent.putExtra("manga", e);

        startActivity(intent);
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