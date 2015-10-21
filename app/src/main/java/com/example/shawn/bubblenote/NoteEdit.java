package com.example.shawn.bubblenote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NoteEdit extends Activity {

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);

        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(NoteDbAdapter.KEY_TITLE);
            String body = extras.getString(NoteDbAdapter.KEY_BODY);

            mRowId = extras.getLong(NoteDbAdapter.KEY_ROWID);

            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }

        }



        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(NoteDbAdapter.KEY_TITLE, mTitleText.getText().toString());
                bundle.putString(NoteDbAdapter.KEY_BODY, mBodyText.getText().toString());
                if (mRowId != null) {
                    bundle.putLong(NoteDbAdapter.KEY_ROWID, mRowId);
                }

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }

        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mTitleText.setText(result.get(0));
                    mBodyText.setText(result.get(1));
                }
                break;
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_mic, menu);
        return true;
    }
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_mic:
                promptSpeechInput();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    public void onBackPressed() {
        Intent i = new Intent(NoteEdit.this, Head.class);
        startActivityForResult(i, 0);
    }
}
