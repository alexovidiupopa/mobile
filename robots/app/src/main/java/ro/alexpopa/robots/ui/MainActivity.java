package ro.alexpopa.robots.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import ro.alexpopa.robots.MainApp;
import ro.alexpopa.robots.NetworkingManager;
import ro.alexpopa.robots.R;
import ro.alexpopa.robots.adapter.DataAdapter;
import ro.alexpopa.robots.adapter.TypeDataAdapter;
import ro.alexpopa.robots.preferences.SharedPreferencesEntry;
import ro.alexpopa.robots.preferences.SharedPreferencesHelper;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MyCallback {

    private TypeDataAdapter adapterTypes;
    private DataAdapter adapterRobots;
    private ProgressBar progressBar;
    private String user;
    private FloatingActionButton fab;
    private View recyclerView1, recyclerView2;
    private NetworkingManager manager;
    private TextView textView;

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

        recyclerView1 = findViewById(R.id.event_list);
        recyclerView2 = findViewById(R.id.robots_list);

        textView = findViewById(R.id.wantedType);

        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getRobotsOfType();
            }
        });

        NetworkingManager.initializeWebSocket(progressBar);

        setupRecyclerView((RecyclerView) recyclerView1, (RecyclerView) recyclerView2);

        loadData();

    }

    private void getRobotsOfType() {
        manager.getRobotsOfType(progressBar,this, textView.getText().toString());
    }

    private void onReportsClick() {
       // Intent intent = new Intent(getApplication(), StatusActivity.class);
        //startActivityForResult(intent,10001);
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
        manager.getTypes(progressBar, this);
        return connectivity;
    }

    @Override
    public void showError(String error) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(recyclerView1, error, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", view -> loadData()).show();

    }

    @Override
    public void clear() {
        adapterTypes.clear();
    }


    public void onAddClick(View view) {
        Intent intent = new Intent(getApplication(), AddActivity.class);
        startActivityForResult(intent, 10000);
    }

    public void onRefreshClick(View view) {
        manager.getTypes(progressBar, this);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, RecyclerView recyclerView2) {
        adapterTypes = new TypeDataAdapter();

        ((MainApp) getApplication()).db.getTypeDao().getTypes()
                .observe(this, items -> adapterTypes.setItems(items));

        recyclerView.setAdapter(adapterTypes);

        adapterRobots = new DataAdapter();
        adapterRobots.setManager(manager);
        ((MainApp) getApplication()).db.getRobotDao().getRobots()
                .observe(this, items -> adapterRobots.setItems(items));
        recyclerView2.setAdapter(adapterRobots);

    }



}