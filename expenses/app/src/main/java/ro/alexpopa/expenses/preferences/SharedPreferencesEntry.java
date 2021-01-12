package ro.alexpopa.expenses.preferences;

public class SharedPreferencesEntry {
    private String user;

    public String getUser() {
        return user;
    }

    public SharedPreferencesEntry(String user) {
        this.user = user;
    }
}
