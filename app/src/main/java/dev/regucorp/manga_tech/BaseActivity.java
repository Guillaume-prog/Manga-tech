package dev.regucorp.manga_tech;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dev.regucorp.manga_tech.data.DataHandler;

public class BaseActivity extends AppCompatActivity {

        protected DataHandler db;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(db == null) db = DataHandler.getInstance(this);
        }

        protected void toast(String text) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

        protected View load(int resid) {
            return getLayoutInflater().inflate(resid, null);
        }

}
