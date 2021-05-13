package com.casproject.casnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NotepadActivity extends AppCompatActivity {
    private EditText titleText;
    private EditText contentText;
    private Button saveButton;
    private Button cancelButton;

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
        titleText = findViewById(R.id.editTextTitle);
        contentText = findViewById(R.id.editTextContent);
        saveButton = findViewById(R.id.buttonSave);
        cancelButton = findViewById(R.id.buttonCancel);
    }

    private void saveButtonClick() {
        Toast.makeText(this, "SAVE", Toast.LENGTH_SHORT).show();
        Intent save = new Intent(getApplicationContext(), MainActivity.class);
        save.putExtra("title", titleText.getText().toString());
        save.putExtra("content", contentText.getText().toString());
        setResult(Activity.RESULT_OK, save);

        finish();
    }

    private void cancelButtonClick() {
        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();

        finish();
    }
}