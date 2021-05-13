package com.casproject.casnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casproject.casnotepad.Recycler.RecyclerItem;

import io.realm.Realm;

public class NotepadActivity extends AppCompatActivity {
    private EditText titleText;
    private EditText contentText;
    private Button saveButton;
    private Button cancelButton;
    private int position;
    private String titles;
    private String contents;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        init();

        saveButton.setOnClickListener( v -> {
            saveButtonClick();
        });

        cancelButton.setOnClickListener( v -> {
            cancelButtonClick();
        });

    }

    private void init() {
        titleText = findViewById(R.id.mEditTextTitle);
        contentText = findViewById(R.id.mEditTextContent);
        saveButton = findViewById(R.id.mButtonEdit);
        cancelButton = findViewById(R.id.mButtonCancel);
    }

    private void saveButtonClick() {

        Intent save = new Intent(getApplicationContext(), MainActivity.class);
        save.putExtra("title", titleText.getText().toString());
        save.putExtra("content", contentText.getText().toString());
        save.putExtra("position", position);
        setResult(RESULT_OK, save);

        Toast.makeText(this, "SAVE", Toast.LENGTH_SHORT).show();

        finish();
    }

    private void cancelButtonClick() {
        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();

        finish();
    }
}