package omrkhld.com.snapasktest;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class QuestionActivity extends AppCompatActivity {

    public static final String TAG = "QuestionActivity";
    private String qID = "";
    private String currentTime;
    private SimpleDateFormat sdf;
    private Question question = null;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            qID = extras.getString("qID");
        }

        Calendar cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
        currentTime = sdf.format(cal.getTime());
        new DownloadQuestion().execute();
    }

    //Checks if there's internet connection
    private boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private class DownloadQuestion extends AsyncTask<Void,Void,Question> {
        protected Question doInBackground(Void... params) {
            try {
                long diff = 0;
                SharedPreferences sp = getSharedPreferences("LastModified", 0);
                String lastModified = sp.getString(qID, "");
                if (!lastModified.isEmpty()) {
                    Date last = sdf.parse(lastModified);
                    Date curr = sdf.parse(currentTime);
                    diff = curr.getTime() - last.getTime();
                }

                Log.e(TAG, "Diff: " + diff);
                if ((diff > 0 && diff < 60000) || !hasConnection()) { // less than 1 minute, still fresh
                    Log.e(TAG, "Cache still fresh or no connection");
                    // download from storage if possible
                    ObjectStorage<Question> objectStorage = new ObjectStorage<Question>(Question.class, QuestionActivity.this);
                    if (objectStorage.exists(qID)) {
                        question = objectStorage.load(qID);
                        loaded = true;
                        if (question != null) {
                            Log.e(TAG, "Loaded from storage");
                        }
                    }
                }

                if (question == null) {
                    Log.e(TAG, "Storage empty. Load from server");
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        question = mapper.readValue(new URL("https://api.myjson.com/bins/" + qID), Question.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return question;
            } catch(Exception e) {
                return null;
            }
        }

        protected void onPostExecute(Question q) {
            if (!loaded && q != null) {
                SharedPreferences sp = getSharedPreferences("LastModified", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(qID, currentTime);
                editor.apply();
                saveQuestion(q);
            }
        }
    }

    private void saveQuestion(Question q) {
        ObjectStorage<Question> objectStorage = new ObjectStorage<Question>(Question.class, QuestionActivity.this);
        boolean saved = objectStorage.save(qID, q);
        if (saved) {
            Log.e(TAG, "Successfully saved.");
        } else {
            Log.e(TAG, "Unsuccessfully saved.");
        }
    }
}
