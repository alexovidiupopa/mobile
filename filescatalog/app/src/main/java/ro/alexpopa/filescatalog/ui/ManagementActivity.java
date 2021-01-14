package ro.alexpopa.filescatalog.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ro.alexpopa.filescatalog.NetworkingManager;
import ro.alexpopa.filescatalog.R;
import ro.alexpopa.filescatalog.model.File;
import ro.alexpopa.filescatalog.preferences.SharedPreferencesHelper;

public class ManagementActivity extends AppCompatActivity implements MyCallback{
    private NetworkingManager manager;
    private TextView textView, textView1;
    private ProgressBar progressBar;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        manager = new NetworkingManager(getApplication());

        progressBar = findViewById(R.id.progressBarBorrow);
        textView = findViewById(R.id.selectedLocation);
        textView1 = findViewById(R.id.idToDelete);

        recyclerView1 = findViewById(R.id.locations_list);
        recyclerView2 = findViewById(R.id.reports_list);
        recyclerView3 = findViewById(R.id.reports_list_second);


        FloatingActionButton fab = findViewById(R.id.borrowFab);

        progressBar.setVisibility(View.GONE);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadForLocation(textView.getText().toString());
            }
        });

        fab.setOnClickListener(i-> deleteById(Integer.parseInt(textView1.getText().toString())));

        loadLocations();

        loadTopTen();

    }

    private void deleteById(int id) {
        progressBar.setVisibility(View.VISIBLE);
        manager.deleteById(progressBar, id, this);
        finish();
    }

    public void loadForLocation(String location){
        progressBar.setVisibility(View.VISIBLE);
        manager.getFilesForLocation(progressBar, location, recyclerView2, this);
        progressBar.setVisibility(View.GONE);
    }

    public void loadLocations(){
        progressBar.setVisibility(View.VISIBLE);
        manager.getAllLocations(progressBar, recyclerView1, this);
        progressBar.setVisibility(View.GONE);
    }

    public void loadTopTen(){
        progressBar.setVisibility(View.VISIBLE);
        manager.getTopExpensive(progressBar, recyclerView3, this);
        progressBar.setVisibility(View.GONE);
    }
    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void clear() {
        textView.setText("");
        textView1.setText("");
    }
}
