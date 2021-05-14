package com.casproject.casnotepad;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.casproject.casnotepad.Recycler.RecyclerItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class ModNotepadActivity extends AppCompatActivity {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private String mTitle, mContent, mURI;
    private int mId;
    private String imagePath = null;
    private EditText mTitleText, mContentText;
    private FloatingActionButton editButton, deleteButton;
    private ImageButton cameraButton, galleryButton;
    private ImageView imageView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_notepad);

        init();

        editButton.setOnClickListener(v -> {
            mEditButtonClick();
        });

        deleteButton.setOnClickListener(v -> {
            mDeleteButtonClick();
        });

        cameraButton.setOnClickListener(v -> {
            mCameraButtonClick();
        });

        galleryButton.setOnClickListener(v -> {
            mGalleryButtonClick();
        });

    }

    private void init() {
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();

        mTitle = intent.getStringExtra("title").toString();
        mContent = intent.getStringExtra("content").toString();
        mURI = intent.getStringExtra("URI");
        mId = intent.getIntExtra("id", 0);

        actionBar.setTitle(mTitle);

        mTitleText = findViewById(R.id.mEditTextTitle);
        mContentText = findViewById(R.id.mEditTextContent);
        editButton = findViewById(R.id.mButtonEdit);
        deleteButton = findViewById(R.id.mButtonDelete);
        cameraButton = findViewById(R.id.mButtonCamera);
        galleryButton = findViewById(R.id.mButtonGallery);
        imageView = findViewById(R.id.mImageView);

        mTitleText.setText(mTitle);
        mContentText.setText(mContent);

        // URI 없으면 이미지 로드 안함
        if (mURI != null) {
            Uri uri = Uri.parse(mURI);
            Glide.with(this).load(uri).into(imageView);
        }

    }


    // 수정하기
    private void mEditButtonClick() {
        Intent intent = new Intent(this, MainActivity.class);

        // 제목 비어있으면 저장 안함
        if (TextUtils.isEmpty(mTitleText.getText())) {
            setResult(RESULT_CANCELED, intent);
            Toast.makeText(this, "저장되지 않음", Toast.LENGTH_SHORT).show();

            finish();
        }
        else {

            realm = Realm.getDefaultInstance();

            // ID 찾아서 값 변경
            realm.executeTransaction(r -> {
                RecyclerItem recyclerItem = realm.where(RecyclerItem.class).equalTo("id", mId).findFirst();
                recyclerItem.setTitle(mTitleText.getText().toString());
                recyclerItem.setContent(mContentText.getText().toString());
                recyclerItem.setURI(imagePath);
                Toast.makeText(getApplicationContext(), "수정되었습니다", Toast.LENGTH_SHORT).show();

            });

            realm.close();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, 1);

            finish();
        }
    }

    // 삭제하기
    private void mDeleteButtonClick() {
        realm = Realm.getDefaultInstance();

        // ID 찾아서 삭제
        realm.executeTransaction(r -> {
            RecyclerItem recyclerItem = realm.where(RecyclerItem.class).equalTo("title", mTitle).findFirst();
            if (recyclerItem.isValid()) {
                recyclerItem.deleteFromRealm();
            }
            Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();
        });

        realm.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);

        finish();
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