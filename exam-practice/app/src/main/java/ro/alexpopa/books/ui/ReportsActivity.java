package ro.alexpopa.books.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ro.alexpopa.books.CallManager;
import ro.alexpopa.books.R;

public class ReportsActivity extends AppCompatActivity implements MyCallback {

    private RecyclerView recyclerView;
    private CallManager manager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        manager = new CallManager(getApplication());

        recyclerView = findViewById(R.id.reports_list);

        loadData();

    }

    private void loadData() {
        manager.getTopTenBooks(recyclerView, this);
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
