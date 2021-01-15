package ro.alexpopa.restaurant.ui;

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

import ro.alexpopa.restaurant.NetworkingManager;
import ro.alexpopa.restaurant.R;

public class StatusActivity extends AppCompatActivity implements MyCallback {

    private NetworkingManager manager;
    private ProgressBar progressBar;
    private TextView label, textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        manager = new NetworkingManager(getApplication());

        progressBar = findViewById(R.id.progressBarReports);

        label = findViewById(R.id.label);
        textView = findViewById(R.id.myTable);

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
                loadData();
            }
        });
        //loadData();

    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        manager.getDetailsForMyTable(progressBar, textView.getText().toString(), label, this);
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