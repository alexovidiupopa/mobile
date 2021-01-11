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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ro.alexpopa.documents.NetworkingManager;
import ro.alexpopa.documents.R;
import ro.alexpopa.documents.model.Document;
import ro.alexpopa.documents.preferences.SharedPreferencesHelper;

public class ManagementActivity extends AppCompatActivity implements MyCallback{
    private RecyclerView recyclerView;
    private NetworkingManager manager;
    private String user;
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        manager = new NetworkingManager(getApplication());

        recyclerView = findViewById(R.id.available_list);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(this));
        user = sharedPreferencesHelper.getEntry().getUser();

        progressBar = findViewById(R.id.progressBarBorrow);
        textView = findViewById(R.id.documentToDelete);

        FloatingActionButton fab = findViewById(R.id.borrowFab);

        fab.setOnClickListener(i->{
            progressBar.setVisibility(View.VISIBLE);
            manager.deleteDocument(progressBar, Integer.parseInt(textView.getText().toString()), this);
            finish();
        });


        loadData();

    }


    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        manager.getAvailableDocuments(progressBar, recyclerView, this);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void clear() {
        textView.setText("");
    }
}
