package ro.alexpopa.onlineshop.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ro.alexpopa.onlineshop.NetworkingManager;
import ro.alexpopa.onlineshop.R;

public class StatusActivity extends AppCompatActivity implements MyCallback {

    private NetworkingManager manager;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        manager = new NetworkingManager(getApplication());

        progressBar = findViewById(R.id.progressBarReports);

        progressBar.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.topUsersList);

        loadData();

    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        manager.getTopTenUsers(progressBar,recyclerView,this);
    }


    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void clear() {

    }
}