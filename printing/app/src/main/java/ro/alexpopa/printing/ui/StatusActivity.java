package ro.alexpopa.printing.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ro.alexpopa.printing.NetworkingManager;
import ro.alexpopa.printing.R;

public class StatusActivity extends AppCompatActivity implements MyCallback {

    private RecyclerView recyclerView, recyclerView2;
    private NetworkingManager manager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        manager = new NetworkingManager(getApplication());

        progressBar = findViewById(R.id.progressBarReports);

        recyclerView = findViewById(R.id.reports_list);
        recyclerView2 = findViewById(R.id.reports_list_second);

        loadData();

    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        manager.getTopExpensive(progressBar, recyclerView, this);
        progressBar.setVisibility(View.VISIBLE);
        manager.getTopEasiest(progressBar, recyclerView2, this);
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