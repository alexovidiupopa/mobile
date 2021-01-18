package ro.alexpopa.exams.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ro.alexpopa.exams.NetworkingManager;
import ro.alexpopa.exams.R;

public class StatusActivity extends AppCompatActivity implements MyCallback {

    private RecyclerView recyclerView;
    private NetworkingManager manager;
    private ProgressBar progressBar;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        manager = new NetworkingManager(getApplication());

        progressBar = findViewById(R.id.progressBarReports);

        recyclerView = findViewById(R.id.reports_list);

        button = findViewById(R.id.reportsButton);

        textView = findViewById(R.id.selectedGroup);

        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(i-> loadData());


    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        manager.getTopExams(progressBar, recyclerView, this, textView.getText().toString());
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