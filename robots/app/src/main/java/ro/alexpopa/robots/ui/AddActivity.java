package ro.alexpopa.robots.ui;

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

import ro.alexpopa.robots.NetworkingManager;
import ro.alexpopa.robots.R;
import ro.alexpopa.robots.model.Robot;
import ro.alexpopa.robots.preferences.SharedPreferencesHelper;

public class AddActivity extends AppCompatActivity implements MyCallback {

    private TextView textView, textView2, textView3, textView4, textView5, textView6;
    private NetworkingManager manager;
    private ProgressBar progressBar;
    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        manager = new NetworkingManager(getApplication());

        progressBar = findViewById(R.id.progressBarAdd);

        textView2 = findViewById(R.id.orderDetails);
        textView3 = findViewById(R.id.orderStatus);
        textView6 = findViewById(R.id.robotHeight);
        textView4 = findViewById(R.id.orderAge);
        textView5 = findViewById(R.id.orderType);
        FloatingActionButton fab = findViewById(R.id.addFab);

        progressBar.setVisibility(View.GONE);
        fab.setOnClickListener(i->{
            progressBar.setVisibility(View.VISIBLE);
            manager.save(progressBar,new Robot(0, textView2.getText().toString(), textView3.getText().toString(),Integer.parseInt(textView6.getText().toString()),Integer.parseInt(textView4.getText().toString()),textView5.getText().toString()), this);
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
        textView2.setText("");
        textView3.setText("");
        textView4.setText("");
        textView5.setText("");
        textView6.setText("");
    }
}