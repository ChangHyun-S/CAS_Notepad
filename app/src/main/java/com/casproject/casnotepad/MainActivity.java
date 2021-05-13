package com.casproject.casnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.casproject.casnotepad.Recycler.RecyclerItem;
import com.casproject.casnotepad.Recycler.RecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private Realm realm;
    private RecyclerItem recyclerItem;
    private RecyclerAdapter recyclerAdapter;
    private FloatingActionButton floatingAddButton;

    public List<RecyclerItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        floatingAddButton = findViewById(R.id.notepadAdd);
        floatingAddButton.setOnClickListener( v -> {
            startNotepad();
        });
    }

    private void startNotepad() {
        intent = new Intent(getApplicationContext(), NotepadActivity.class);
        startActivity(intent);
    }
}