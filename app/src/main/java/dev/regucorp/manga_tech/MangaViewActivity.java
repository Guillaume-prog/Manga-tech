package dev.regucorp.manga_tech;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import dev.regucorp.manga_tech.data.models.MangaModel;

public class MangaViewActivity extends BaseActivity {

    private static final String TAG = "MangaViewActivity";

    private String title;
    private char[] owned;
    private int volumes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);

        getIntentData();

        renderUI();
    }

    private void renderUI() {
        TextView titleView = findViewById(R.id.manga_name);
        titleView.setText(title);

        LinearLayout volList = findViewById(R.id.volume_list);

        int i = 1;
        Log.d(TAG, "renderUI: " + owned);
        for(char c : owned) {
            View v = load(R.layout.volume_component);

            ((TextView) v.findViewById(R.id.volume_name)).setText("Volume #" + i++);

            CheckBox box = v.findViewById(R.id.volume_is_owned);
            box.setChecked(c == '1');
            int finalI = i;
            box.setOnClickListener(checkbox -> {
                owned[finalI -2] = (((CheckBox) checkbox).isChecked()) ? '1' : '0';
                MangaModel.getInstance().updateOwned(db, title, new String(owned));
            });

            volList.addView(v);
        }
    }

    private void getIntentData() {
        Bundle extras = getIntent().getExtras();
        title = extras.getString("manga_title");
        owned = extras.getString("manga_vols_owned").toCharArray();
        volumes = extras.getInt("manga_num_vols");
        Log.d(TAG, "getIntentData: "+ new String(owned));
    }

}
