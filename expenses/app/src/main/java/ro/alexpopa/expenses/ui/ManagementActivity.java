package ro.alexpopa.expenses.ui;

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

import ro.alexpopa.expenses.NetworkingManager;
import ro.alexpopa.expenses.R;
import ro.alexpopa.expenses.model.Expense;
import ro.alexpopa.expenses.preferences.SharedPreferencesHelper;

public class ManagementActivity extends AppCompatActivity implements MyCallback{
    private RecyclerView recyclerView;
    private NetworkingManager manager;
    private String user;
    private TextView textView, textView1, textView2;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        manager = new NetworkingManager(getApplication());

        recyclerView = findViewById(R.id.available_list);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(this));
        user = sharedPreferencesHelper.getEntry().getUser();

        progressBar = findViewById(R.id.progressBarBorrow);
        textView = findViewById(R.id.expenseToChange);
        textView1 = findViewById(R.id.newStatus);
        textView2 = findViewById(R.id.newCost);
        FloatingActionButton fab = findViewById(R.id.borrowFab);

        fab.setOnClickListener(i->{
            progressBar.setVisibility(View.VISIBLE);
            manager.updateRequest(progressBar, new Expense(Integer.parseInt(textView.getText().toString()),"",textView1.getText().toString(),"",0,Integer.parseInt(textView2.getText().toString())), this);
            finish();
        });


        loadData();

    }


    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        manager.getAvailableExpenses(progressBar, recyclerView, this);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void clear() {
        textView.setText("");
    }
}
