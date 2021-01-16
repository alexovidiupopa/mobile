package ro.alexpopa.onlineshop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import ro.alexpopa.onlineshop.MainApp;
import ro.alexpopa.onlineshop.NetworkingManager;
import ro.alexpopa.onlineshop.R;
import ro.alexpopa.onlineshop.adapter.DataAdapter;
import ro.alexpopa.onlineshop.preferences.SharedPreferencesEntry;
import ro.alexpopa.onlineshop.preferences.SharedPreferencesHelper;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MyCallback {

    private DataAdapter adapter;
    private ProgressBar progressBar;
    private String user;
    private FloatingActionButton fab;
    private View recyclerView;
    private NetworkingManager manager;


    private Button reportsButton;
    private Button borrowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = new NetworkingManager(getApplication());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        borrowButton = findViewById(R.id.manageButton);
        reportsButton = findViewById(R.id.reportsButton);

        borrowButton.setOnClickListener(i->onBorrowClick());
        reportsButton.setOnClickListener(i->onReportsClick());
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(i->{
            onAddClick(fab);
        });

        progressBar = findViewById(R.id.progress);

        recyclerView = findViewById(R.id.event_list);

        NetworkingManager.initializeWebSocket(progressBar);

        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(PreferenceManager.getDefaultSharedPreferences(this));
        user = sharedPreferencesHelper.getEntry().getUser();
        if (user.equals("")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Current user:");

            // Set up the input
            final EditText input = new EditText(this);

            builder.setView(input);

            // Set up the button
            builder.setPositiveButton("OK", (dialog, which) -> {
                user = input.getText().toString();
                sharedPreferencesHelper.saveUser(new SharedPreferencesEntry(user));
                setupRecyclerView((RecyclerView) recyclerView);
                loadData();
            });

            builder.show();

        }
        else {
            setupRecyclerView((RecyclerView) recyclerView);
            loadData();
        }




    }

    private void onReportsClick() {
        Intent intent = new Intent(getApplication(), StatusActivity.class);
        startActivityForResult(intent,10001);
    }

    private void onBorrowClick() {
        Intent intent = new Intent(getApplication(), ManagementActivity.class);
        startActivityForResult(intent,10002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Timber.d("Back in main activity");
        loadData();
    }

    private boolean loadData() {
        boolean connectivity = manager.networkConnectivity(getApplicationContext());
        if (connectivity) {
            fab.setVisibility(View.VISIBLE);
            borrowButton.setVisibility(View.VISIBLE);
            reportsButton.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
            borrowButton.setVisibility(View.GONE);
            reportsButton.setVisibility(View.GONE);
            showError("No internet connection!");
        }
        progressBar.setVisibility(View.VISIBLE);
        manager.loadOrdersForClient(progressBar, this, Integer.parseInt(user));
        return connectivity;
    }

    @Override
    public void showError(String error) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadData();
                    }
                }).show();

    }

    @Override
    public void clear() {
        adapter.clear();
    }


    public void onAddClick(View view) {
        Intent intent = new Intent(getApplication(), AddActivity.class);
        startActivityForResult(intent, 10000);
    }

    public void onRefreshClick(View view) {
        manager.loadOrdersForClient(progressBar, this, Integer.parseInt(user));
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new DataAdapter();
        adapter.setManager(manager);
        ((MainApp) getApplication()).db.getOrderDao().getOrders()
                .observe(this, items -> adapter.setItems(items));
        recyclerView.setAdapter(adapter);
    }



}