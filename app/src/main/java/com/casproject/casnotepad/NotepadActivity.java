package com.casproject.casnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.casproject.casnotepad.Recycler.RecyclerItem;

import io.realm.Realm;

public class NotepadActivity extends AppCompatActivity {
    private EditText titleText, contentText;
    private Button saveButton, cancelButton, cameraButton, galleryButton;;
    private ImageView imageView;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private final int GET_GALLERY_IMAGE = 200;

    String uri1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        init();

        saveButton.setOnClickListener(v -> {
            saveButtonClick();
        });

        cancelButton.setOnClickListener(v -> {
            cancelButtonClick();
        });

        cameraButton.setOnClickListener(v -> {
            //mCameraButtonClick();
        });

        galleryButton.setOnClickListener(v -> {
            mGalleryButtonClick();
        });

    }

    private void init() {
        titleText = findViewById(R.id.mEditTextTitle);
        contentText = findViewById(R.id.mEditTextContent);
        saveButton = findViewById(R.id.mButtonEdit);
        cancelButton = findViewById(R.id.mButtonCancel);
        cameraButton = findViewById(R.id.mButtonCamera);
        galleryButton = findViewById(R.id.mButtonGallery);
        imageView = findViewById(R.id.mImageView);
    }

    private void saveButtonClick() {

        Intent save = new Intent(getApplicationContext(), MainActivity.class);
        save.putExtra("title", titleText.getText().toString());
        save.putExtra("content", contentText.getText().toString());
        Log.d("!!!!!!!!", uri1);
        save.putExtra("URI", uri1);
        setResult(RESULT_OK, save);

        Toast.makeText(this, "SAVE", Toast.LENGTH_SHORT).show();

        finish();
    }

    private void cancelButtonClick() {
        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();

        finish();
    }

    // 카메라 버튼
    private void mCameraButtonClick() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra((MediaStore.EXTRA_OUTPUT, Uri.fromFile(profile)))
//        startActivityForResult(intent, PIC);
    }

    // 갤러리 ㅂ튼
    private void mGalleryButtonClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);

            uri1 = selectedImageUri.toString();
        }
    }




}