package com.casproject.casnotepad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class NotepadActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private String imagePath = null;
    private EditText titleText, contentText;
    private FloatingActionButton saveButton;
    private ImageButton cameraButton, galleryButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        init();

        saveButton.setOnClickListener(v -> {
            saveButtonClick();
        });

        cameraButton.setOnClickListener(v -> {
            mCameraButtonClick();
        });

        galleryButton.setOnClickListener(v -> {
            mGalleryButtonClick();
        });

    }

    private void init() {
        // Binding
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("빈 메모장");

        titleText = findViewById(R.id.mEditTextTitle);
        contentText = findViewById(R.id.mEditTextContent);
        saveButton = findViewById(R.id.mButtonEdit);
        cameraButton = findViewById(R.id.mButtonCamera);
        galleryButton = findViewById(R.id.mButtonGallery);
        imageView = findViewById(R.id.mImageView);
    }

    // 저장 버튼
    private void saveButtonClick() {
        Intent save = new Intent(getApplicationContext(), MainActivity.class);

        // 제목 비어있으면 저장 안함
        if (TextUtils.isEmpty(titleText.getText())) {
            setResult(RESULT_CANCELED, save);
            Toast.makeText(this, "저장되지 않음", Toast.LENGTH_SHORT).show();

            finish();
        }
        else {
            save.putExtra("title", titleText.getText().toString());
            save.putExtra("content", contentText.getText().toString());
            save.putExtra("URI", imagePath);
            setResult(RESULT_OK, save);

            Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    // 카메라 버튼
    private void mCameraButtonClick() {
        dispatchTakePictureIntent();
    }

    // 갤러리 버튼
    private void mGalleryButtonClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    // 이미지 파일로 생성
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = image.getAbsolutePath();
        return image;
    }

    // 카메라 Intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.casproject.casnotepad.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
                imagePath = photoURI.toString();
            }
        }
    }

    // 사진 저장
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 앨범 선택 시 URI 저장 및 imageView 표시
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            imagePath = selectedImageUri.toString();
        }
        // 카메라 선택 시 사진 저장 후 imageView 표시
        else if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            galleryAddPic();
            imageView.setImageURI(Uri.parse(imagePath));
        }
    }
}