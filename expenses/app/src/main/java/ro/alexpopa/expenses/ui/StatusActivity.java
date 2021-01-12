package ro.alexpopa.expenses.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ro.alexpopa.expenses.NetworkingManager;
import ro.alexpopa.expenses.R;

public class StatusActivity extends AppCompatActivity implements MyCallback {

    private RecyclerView recyclerView;
    private NetworkingManager manager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        manager = new NetworkingManager(getApplication());

        progressBar = findViewById(R.id.progressBarReports);

        recyclerView = findViewById(R.id.reports_list);

        loadData();

    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        manager.getTopTenExpenses(progressBar, recyclerView, this);
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