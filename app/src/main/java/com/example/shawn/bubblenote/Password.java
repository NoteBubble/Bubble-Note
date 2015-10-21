package com.example.shawn.bubblenote;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Password extends Activity{

    private EditText password;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);
        addListenerOnButton();
    }

    public void addListenerOnButton() {

        password = (EditText) findViewById(R.id.txtPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
                toast.makeText(Password.this, password.getText(), toast.LENGTH_SHORT).show();

            }

        });

    }
}
