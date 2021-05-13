package com.casproject.casnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.casproject.casnotepad.Recycler.RecyclerItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class NotepadActivity extends AppCompatActivity {
    private EditText titleText, contentText;
    private Button saveButton, cancelButton, cameraButton, galleryButton;;
    private ImageView imageView;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    Realm realm;

    String imagePath = null;

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
            mCameraButtonClick();
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
        save.putExtra("URI", imagePath);
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
        dispatchTakePictureIntent();
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    // 갤러리 버튼
    private void mGalleryButtonClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }



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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진 저장됨", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);

            imagePath = selectedImageUri.toString();
        }
        else if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            galleryAddPic();
//            Log.d("HERE@@", data.getData().toString())
            imageView.setImageURI(Uri.parse(imagePath));

            // uri1 = selectedImageUri.toString();
        }
    }




}