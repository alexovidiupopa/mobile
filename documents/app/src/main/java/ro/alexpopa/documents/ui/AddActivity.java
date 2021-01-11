package ro.alexpopa.documents.ui;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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

import ro.alexpopa.documents.NetworkingManager;
import ro.alexpopa.documents.R;
import ro.alexpopa.documents.model.Document;
import ro.alexpopa.documents.preferences.SharedPreferencesHelper;

public class AddActivity extends AppCompatActivity implements MyCallback {

    private TextView textView, textView2, textView3;
    private NetworkingManager manager;
    private String user;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        manager = new NetworkingManager(getApplication());

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(this));
        user = sharedPreferencesHelper.getEntry().getUser();

        progressBar = findViewById(R.id.progressBarAdd);
        textView = findViewById(R.id.documentName);
        textView2 = findViewById(R.id.documentSize);
        textView3 = findViewById(R.id.documentScore);
        FloatingActionButton fab = findViewById(R.id.addFab);

        progressBar.setVisibility(View.GONE);
        fab.setOnClickListener(i->{
            progressBar.setVisibility(View.VISIBLE);
            manager.save(progressBar,new Document(0, textView.getText().toString(),"",user, Integer.parseInt(textView2.getText().toString()),Integer.parseInt(textView3.getText().toString())), this);
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
    }
}