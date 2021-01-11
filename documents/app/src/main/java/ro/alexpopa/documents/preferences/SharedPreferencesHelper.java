package ro.alexpopa.documents.preferences;

import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String KEY_NAME = "key_name";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean saveUser(SharedPreferencesEntry entry){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, entry.getUser());
        return editor.commit();
    }

    public SharedPreferencesEntry getEntry(){
        String user = sharedPreferences.getString(KEY_NAME,"");
        return new SharedPreferencesEntry(user);
    }
}
