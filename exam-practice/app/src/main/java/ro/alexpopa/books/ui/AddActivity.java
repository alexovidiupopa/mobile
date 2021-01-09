package ro.alexpopa.books.ui;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ro.alexpopa.books.CallManager;
import ro.alexpopa.books.R;
import ro.alexpopa.books.model.Book;

public class AddActivity extends AppCompatActivity implements MyCallback {

    private TextView textView, textView2, textView3;
    private CallManager manager;
    private String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        manager = new CallManager(getApplication());

        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            user = applicationInfo.metaData.getString("user");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        textView = findViewById(R.id.bookTitle);
        textView2 = findViewById(R.id.bookPages);
        textView3 = findViewById(R.id.bookUsedCount);
        FloatingActionButton fab = findViewById(R.id.addFab);

        fab.setOnClickListener(i->{
            manager.save(new Book(0, textView.getText().toString(),"",user, Integer.parseInt(textView2.getText().toString()),Integer.parseInt(textView3.getText().toString())), this);
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
