package omrkhld.com.snapasktest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToQuestion(View view) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("qID", "1qzf3");
        startActivity(intent);
    }

    public void goToUser(View view) {
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("uID", "mosv");
        startActivity(intent);
    }
}
