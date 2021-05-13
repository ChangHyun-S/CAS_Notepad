package com.casproject.casnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.casproject.casnotepad.Recycler.RecyclerItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;

import io.realm.Realm;

public class ModNotepadActivity extends AppCompatActivity {
    private String mTitle, mContent, mURI;
    private EditText mTitleText, mContentText;
    private Button editButton, cancelButton, deleteButton, cameraButton, galleryButton;
    private ImageView imageView;
    private Realm realm;
    private int mId;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_notepad);

        temp();

        editButton.setOnClickListener(v -> {
            mEditButtonClick();
        });

        deleteButton.setOnClickListener(v -> {
            mDeleteButtonClick();
        });

        cancelButton.setOnClickListener(v -> {
            mCancelButtonClick();
        });

        cameraButton.setOnClickListener(v -> {
            mCameraButtonClick();
        });

        galleryButton.setOnClickListener(v -> {
            mGalleryButtonClick();
        });

    }

    private void temp() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title").toString();
        mContent = intent.getStringExtra("content").toString();
        mURI = intent.getStringExtra("URI");
        mId = intent.getIntExtra("id", 0);

        mTitleText = findViewById(R.id.mEditTextTitle);
        mContentText = findViewById(R.id.mEditTextContent);
        editButton = findViewById(R.id.mButtonEdit);
        cancelButton = findViewById(R.id.mButtonCancel);
        deleteButton = findViewById(R.id.mButtonDelete);
        cameraButton = findViewById(R.id.mButtonCamera);
        galleryButton = findViewById(R.id.mButtonGallery);
        imageView = findViewById(R.id.mImageView);

        mTitleText.setText(mTitle);
        mContentText.setText(mContent);

        if (mURI != null) {
            Uri uri = Uri.parse(mURI);
            Glide.with(this).load(uri).into(imageView);
        }

    }


    // 수정하기
    private void mEditButtonClick() {
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(r -> {
            RecyclerItem recyclerItem = realm.where(RecyclerItem.class).equalTo("id", mId).findFirst();
            recyclerItem.setTitle(mTitleText.getText().toString());
            recyclerItem.setContent(mContentText.getText().toString());
            Log.d("HERE@@ 2 : ", imagePath);
            recyclerItem.setURI(imagePath);
            Toast.makeText(getApplicationContext(), "EDIT", Toast.LENGTH_SHORT).show();

        });

        realm.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);

        finish();
    }

    // 삭제하기
    private void mDeleteButtonClick() {
        realm = Realm.getDefaultInstance();

        realm.executeTransaction(r -> {
            RecyclerItem recyclerItem = realm.where(RecyclerItem.class).equalTo("title", mTitle).findFirst();
            if (recyclerItem.isValid()) {
                recyclerItem.deleteFromRealm();
            }
            Toast.makeText(getApplicationContext(), "DELETE", Toast.LENGTH_SHORT).show();
        });

        realm.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);

        finish();
    }

    // 취소하기
    private void mCancelButtonClick() {
        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();

        finish();
    }

    // 카메라 버튼
    private void mCameraButtonClick() {
        dispatchTakePictureIntent();
    }

    // 갤러리 ㅂ튼
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
        } else if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            galleryAddPic();
            Log.d("HERE@@ 1 : ", imagePath);
            imageView.setImageURI(Uri.parse(imagePath));

            // uri1 = selectedImageUri.toString();
        }
    }
}