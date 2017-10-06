package alek.examen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import alek.examen.model.Description;

public class AddDescriptionActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    EditText editText;
    Intent intent;
    MySQLiteHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);

        intent = getIntent();
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        helper = new MySQLiteHelper(this);

        //Toast.makeText(this, intent.getStringExtra("latitude") + intent.getStringExtra("longitude"), Toast.LENGTH_LONG);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    helper.addDescription(intent.getDoubleExtra("latitude", 0), intent.getDoubleExtra("longitude", 0), textView.getText().toString());
                    moveToNextScreen();
                } catch (Exception e) {
                    Log.e("SQLITE", e.getMessage());
                    hideSoftKeyBoard();
                }
            }
        });
    }

    private void moveToNextScreen() {
        Intent nextScreenIntent = new Intent(this, MainActivity.class);
        startActivity(nextScreenIntent);
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
