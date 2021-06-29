package dev.regucorp.manga_tech;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.regucorp.manga_tech.data.MangaEntry;
import dev.regucorp.manga_tech.data.MangaModel;

public class MangaViewActivity extends BaseActivity {

    private MangaEntry manga;
    private char[] ownedVols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_view);

        this.manga = getIntent().getExtras().getParcelable("manga");
        renderUI();
    }

    private void renderUI() {
        TextView title = findViewById(R.id.manga_name);
        title.setText(manga.getName());

        LinearLayout ll = findViewById(R.id.volume_list);

        ownedVols = manga.getOwned().toCharArray();
        for(int i = 0; i < manga.getNumVolumes(); i++) {
            View v = load(R.layout.volume_component);

            TextView volName = v.findViewById(R.id.volume_name);
            CheckBox volIsOwned = v.findViewById(R.id.volume_is_owned);

            int finalI = i;
            volIsOwned.setOnClickListener(checkbox -> {
                ownedVols[finalI] = (((CheckBox) checkbox).isChecked()) ? '1' : '0';
            });

            volName.setText("Volume " + (i + 1));
            volIsOwned.setChecked(ownedVols[i] == '1');

            ll.addView(v);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String newOwned = new String(ownedVols);

        if(!manga.getOwned().equals(newOwned)) {
            manga.setOwned(newOwned);
            MangaModel.getInstance().updateEntry(db, manga);
        }

        Log.d("TAG", "onBackPressed: pingy ping, " + newOwned);
        toast("Going backwards");
    }
}