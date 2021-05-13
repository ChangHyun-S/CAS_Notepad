package com.casproject.casnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.casproject.casnotepad.Recycler.RecyclerItem;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.BitSet;

import io.realm.Realm;

public class ModNotepadActivity extends AppCompatActivity {
    private String mTitle, mContent, mURI;
    private EditText mTitleText, mContentText;
    private Button editButton, cancelButton, deleteButton, cameraButton, galleryButton;
    private ImageView imageView;
    private Realm realm;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

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
            //mCameraButtonClick();
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
        Uri uri = Uri.parse(mURI);
        Log.d("@@@@@@@@@@@@@@@@@@@@@@@@", uri.toString());
        Glide.with(this).load(uri).into(imageView);
        // imageView.setImageURI(uri);
        // setImage(Uri.parse(mURI));

    }

//    private void setImage(Uri uri) {
//        try{
//            InputStream in = getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(in);
//            imageView.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        }
//    }


    // 수정하기
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
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra((MediaStore.EXTRA_OUTPUT, Uri.fromFile(profile)))
//        startActivityForResult(intent, PIC);
    }

    // 갤러리 ㅂ튼
    private void mGalleryButtonClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }
}