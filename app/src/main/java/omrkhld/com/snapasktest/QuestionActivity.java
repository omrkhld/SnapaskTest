package omrkhld.com.snapasktest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity {

    public static final String TAG = "QuestionActivity";
    private String qID = "";
    private String currentTime;
    private SimpleDateFormat sdf;
    private Question question = null;
    private boolean loaded = false;

    @Bind(R.id.asker_name) TextView askerName;
    @Bind(R.id.answer_name) TextView answerName;
    @Bind(R.id.asker_img) CircularImageView askerImg;
    @Bind(R.id.answer_img) CircularImageView answerImg;
    @Bind(R.id.question_img) ImageView questionImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);

        Calendar cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
        currentTime = sdf.format(cal.getTime());

        onNewIntent(getIntent());
    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            qID = extras.getString("qID");
            new DownloadQuestion().execute();
        } else {
            qID = data.split("/")[3];
            if (Intent.ACTION_VIEW.equals(action) && !qID.isEmpty()) {
                new DownloadQuestion().execute();
            }
        }
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
                if ((diff != 0 && diff < 60000)|| !hasConnection()) { // less than 1 minute, still fresh
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
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
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

                Picasso.with(getBaseContext()).load(q.getData().getAskedBy().getImgURL()).into(askerImg);
                Picasso.with(getBaseContext()).load(q.getData().getAnsweredBy().getImgURL()).into(answerImg);
                Picasso.with(getBaseContext()).load(q.getData().getImgURL()).into(questionImg);
                askerName.setText(q.getData().getAskedBy().getUsername());
                answerName.setText(q.getData().getAnsweredBy().getUsername());
            }

            if (q == null) {
                //show error page
                setContentView(R.layout.activity_error);
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
