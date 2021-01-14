package ro.alexpopa.printing.ui;

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

import ro.alexpopa.printing.NetworkingManager;
import ro.alexpopa.printing.R;
import ro.alexpopa.printing.model.Model;
import ro.alexpopa.printing.preferences.SharedPreferencesHelper;

public class ManagementActivity extends AppCompatActivity implements MyCallback{
    private NetworkingManager manager;
    private int user;
    private TextView textView, textView1;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        manager = new NetworkingManager(getApplication());

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(this));
        user = sharedPreferencesHelper.getEntry().getUser();

        progressBar = findViewById(R.id.progressBarBorrow);
        textView = findViewById(R.id.expenseToChange);
        textView1 = findViewById(R.id.newStatus);

        FloatingActionButton fab = findViewById(R.id.borrowFab);

        progressBar.setVisibility(View.GONE);
        fab.setOnClickListener(i->{
            progressBar.setVisibility(View.VISIBLE);
            manager.updateRequest(progressBar, new Model(Integer.parseInt(textView.getText().toString()),"",textView1.getText().toString(),0,0,0), this);
            finish();
        });


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
