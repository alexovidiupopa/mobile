package ro.alexpopa.restaurant.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ro.alexpopa.restaurant.NetworkingManager;
import ro.alexpopa.restaurant.R;
import ro.alexpopa.restaurant.model.Order;

public class AddActivity extends AppCompatActivity implements MyCallback {

    private TextView textView, textView2, textView3, textView4, textView5;
    private NetworkingManager manager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        manager = new NetworkingManager(getApplication());


        progressBar = findViewById(R.id.progressBarAdd);
        textView = findViewById(R.id.tableName);
        textView2 = findViewById(R.id.tableDetails);
        textView3 = findViewById(R.id.tableStatus);
        textView4 = findViewById(R.id.tableTime);
        textView5 = findViewById(R.id.tableType);
        FloatingActionButton fab = findViewById(R.id.addFab);

        progressBar.setVisibility(View.GONE);
        fab.setOnClickListener(i->{
            progressBar.setVisibility(View.VISIBLE);
            manager.save(progressBar,new Order(0, textView.getText().toString(), textView2.getText().toString(),textView3.getText().toString(),Integer.parseInt(textView4.getText().toString()),textView5.getText().toString()), this);
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