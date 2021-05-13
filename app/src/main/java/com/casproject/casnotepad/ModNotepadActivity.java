package com.casproject.casnotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.casproject.casnotepad.Recycler.RecyclerItem;

import io.realm.Realm;

public class ModNotepadActivity extends AppCompatActivity {
    private String mTitle, mContent, mPosition;
    private EditText mTitleText, mContentText;
    private Button editButton, cancelButton, deleteButton;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_notepad);

        temp();

        editButton.setOnClickListener( v -> {
            mEditButtonClick();
        });

        deleteButton.setOnClickListener( v -> {
            mDeleteButtonClick();
        });

        cancelButton.setOnClickListener( v -> {
            mCancelButtonClick();
        });

    }

    private void temp() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title").toString();
        mContent = intent.getStringExtra("content").toString();
        mPosition = intent.getStringExtra("position").toString();

        mTitleText = findViewById(R.id.mEditTextTitle);
        mContentText = findViewById(R.id.mEditTextContent);
        editButton = findViewById(R.id.mButtonEdit);
        cancelButton = findViewById(R.id.mButtonCancel);
        deleteButton = findViewById(R.id.mButtonDelete);

        mTitleText.setText(mTitle);
        mContentText.setText(mContent);

    }

    private void mEditButtonClick() {
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(r -> {
            RecyclerItem recyclerItem = realm.where(RecyclerItem.class).equalTo("title", mTitle).findFirst();
            recyclerItem.setTitle(mTitleText.getText().toString());
            recyclerItem.setContent(mContentText.getText().toString());
            Toast.makeText(getApplicationContext(), "EDIT", Toast.LENGTH_SHORT).show();

        });

        realm.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 1);

        finish();
    }

    private void mDeleteButtonClick() {
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(r -> {
            RecyclerItem recyclerItem = realm.where(RecyclerItem.class).equalTo("title", mTitle).findFirst();
            if(recyclerItem.isValid()) {
                recyclerItem.deleteFromRealm();
            }
            Toast.makeText(getApplicationContext(), "DELETE", Toast.LENGTH_SHORT).show();
        });

        realm.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 1);

        finish();
    }

    private void mCancelButtonClick() {
        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();

        finish();
    }
}