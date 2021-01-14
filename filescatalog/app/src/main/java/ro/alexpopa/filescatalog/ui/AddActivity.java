package ro.alexpopa.filescatalog.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ro.alexpopa.filescatalog.NetworkingManager;
import ro.alexpopa.filescatalog.R;
import ro.alexpopa.filescatalog.model.File;
import ro.alexpopa.filescatalog.preferences.SharedPreferencesHelper;

public class AddActivity extends AppCompatActivity implements MyCallback {

    private TextView textView, textView2, textView3, textView4, textView5;
    private NetworkingManager manager;
    private int user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        manager = new NetworkingManager(getApplication());

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(this));
        user = sharedPreferencesHelper.getEntry().getUser();

        progressBar = findViewById(R.id.progressBarAdd);
        textView = findViewById(R.id.name);
        textView2 = findViewById(R.id.status);
        textView3 = findViewById(R.id.size);
        textView4 = findViewById(R.id.usage);
        textView5 = findViewById(R.id.location);
        FloatingActionButton fab = findViewById(R.id.addFab);

        progressBar.setVisibility(View.GONE);
        fab.setOnClickListener(i->{
            progressBar.setVisibility(View.VISIBLE);
            manager.save(progressBar,new File(0, textView.getText().toString(),textView2.getText().toString(), Integer.parseInt(textView3.getText().toString()),textView5.getText().toString(), Integer.parseInt(textView4.getText().toString())), this);
            finish();
        });


        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

    }


    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void clear() {
        textView.setText("");
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");
    }
}