package com.example.shawn.bubblenote;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class BubbleNote extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startService(new Intent(BubbleNote.this, NoteHead.class));
    }


    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
