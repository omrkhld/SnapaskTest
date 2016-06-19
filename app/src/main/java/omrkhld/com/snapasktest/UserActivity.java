package omrkhld.com.snapasktest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserActivity extends AppCompatActivity {

    public static final String TAG = "UserActivity";
    private String uID = "";
    private String currentTime;
    private SimpleDateFormat sdf;
    private User user = null;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uID = extras.getString("uID");
        }

        Calendar cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
        currentTime = sdf.format(cal.getTime());
        new DownloadUser().execute();
    }

    //Checks if there's internet connection
    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private class DownloadUser extends AsyncTask<Void,Void,User> {
        protected User doInBackground(Void... params) {
            try {
                long diff = 0;
                SharedPreferences sp = getSharedPreferences("LastModified", 0);
                String lastModified = sp.getString(uID, "0");
                if (!lastModified.isEmpty()) {
                    Date last = sdf.parse(lastModified);
                    Date curr = sdf.parse(currentTime);
                    diff = curr.getTime() - last.getTime();
                }

                Log.e(TAG, "Diff: " + diff);
                if ((diff > 0 && diff < 60000) || !hasConnection()) { // less than 1 minute, still fresh
                    Log.e(TAG, "Cache still fresh or no connection");
                    // download from storage if possible
                    ObjectStorage<User> objectStorage = new ObjectStorage<User>(User.class, UserActivity.this);
                    if (objectStorage.exists(uID)) {
                        user = objectStorage.load(uID);
                        loaded = true;
                        if (user != null) {
                            Log.e(TAG, "Loaded from storage");
                        } else {
                            return null;
                        }
                    }
                }

                if (user == null) {
                    Log.e(TAG, "Storage empty. Load from server");
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        user = mapper.readValue(new URL("https://api.myjson.com/bins/" + uID), User.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return user;
            } catch(Exception e) {
                return null;
            }
        }

        protected void onPostExecute(User u) {
            if (!loaded && u != null) {
                SharedPreferences sp = getSharedPreferences("LastModified", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(uID, currentTime);
                editor.apply();
                saveUser(u);
            }
        }
    }

    private void saveUser(User u) {
        ObjectStorage<User> objectStorage = new ObjectStorage<User>(User.class, UserActivity.this);
        boolean saved = objectStorage.save(uID, u);
        if (saved) {
            Log.e(TAG, "Successfully saved.");
        } else {
            Log.e(TAG, "Unsuccessfully saved.");
        }
    }
}
