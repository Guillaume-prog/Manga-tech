package dev.regucorp.manga_tech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import dev.regucorp.manga_tech.data.DataHandler;
import dev.regucorp.manga_tech.data.MangaModel;

public class BaseActivity extends AppCompatActivity {

        protected DataHandler db;
        protected LayoutInflater inflater;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            MangaModel.createInstance(getResources());

            db = DataHandler.getInstance(this);
            inflater = getLayoutInflater();
        }

        protected void toast(String text) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

        protected View load(int resid) {
            return inflater.inflate(resid, null);
        }

        protected AlertDialog createDialog(int layout_id) {
            View dialogView = load(layout_id);

            AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
            builder.setView(dialogView);
            return builder.create();
        }

}
