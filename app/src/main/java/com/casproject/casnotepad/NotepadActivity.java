package com.casproject.casnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class NotepadActivity extends AppCompatActivity {
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        saveButton = (Button) findViewById(R.id.buttonSave);
        cancelButton = (Button) findViewById(R.id.buttonCancel);

        saveButton.setOnClickListener( v -> {
            saveButtonClick();
        });

        cancelButton.setOnClickListener( v -> {
            cancelButtonClick();
        });

    }

    public void saveButtonClick() {
        Toast.makeText(this, "SAVE", Toast.LENGTH_SHORT).show();
    }

    public void cancelButtonClick() {
        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();

    }
}