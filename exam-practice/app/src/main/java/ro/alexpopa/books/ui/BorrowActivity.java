package ro.alexpopa.books.ui;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ro.alexpopa.books.CallManager;
import ro.alexpopa.books.R;
import ro.alexpopa.books.model.Book;

public class BorrowActivity extends AppCompatActivity implements MyCallback {

    private RecyclerView recyclerView;
    private CallManager manager;
    private String user;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow);

        manager = new CallManager(getApplication());

        recyclerView = findViewById(R.id.available_list);

        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            user = applicationInfo.metaData.getString("user");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        textView = findViewById(R.id.bookToBorrow);

        FloatingActionButton fab = findViewById(R.id.borrowFab);

        fab.setOnClickListener(i->{
            manager.borrow(new Book(Integer.parseInt(textView.getText().toString()),"","",user, 0,0), this);
            finish();
        });


        loadData();

    }


    private void loadData() {
        manager.getAvailableBooks(recyclerView, this);
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
