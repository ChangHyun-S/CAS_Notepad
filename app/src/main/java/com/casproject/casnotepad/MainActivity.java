package com.casproject.casnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.casproject.casnotepad.Realm.Notepad;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private Realm realm;
    private Notepad notepad;
    private FloatingActionButton floatingAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        floatingAddButton.setOnClickListener( v -> {
            emptyNotepad();
        });
    }

    private void emptyNotepad() {
        intent = new Intent(getApplicationContext(), NotepadActivity.class);
        startActivity(intent);
    }
}