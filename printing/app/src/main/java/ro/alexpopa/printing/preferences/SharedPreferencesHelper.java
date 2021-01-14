package ro.alexpopa.printing.preferences;

import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String KEY_CLIENT_ID = "key_client_id";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean saveUser(SharedPreferencesEntry entry){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_CLIENT_ID, entry.getUser());
        return editor.commit();
    }

    public SharedPreferencesEntry getEntry(){
        int user = sharedPreferences.getInt(KEY_CLIENT_ID,0);
        return new SharedPreferencesEntry(user);
    }
}
