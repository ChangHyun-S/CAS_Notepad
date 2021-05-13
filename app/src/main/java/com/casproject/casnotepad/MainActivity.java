package com.casproject.casnotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.casproject.casnotepad.Recycler.RecyclerItem;
import com.casproject.casnotepad.Recycler.RecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private Realm realm;

    private RecyclerItem recyclerItem;
    private RecyclerAdapter recyclerAdapter;

    private FloatingActionButton floatingAddButton;
    private RecyclerView recyclerView;

    public List<RecyclerItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();
        permission();

        floatingAddButton.setOnClickListener(v -> {
            startNotepad();
        });

    }

    private void test() {
        floatingAddButton = findViewById(R.id.notepadAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(configuration);
        realm = Realm.getDefaultInstance();

        RealmResults<RecyclerItem> realmResults = realm.where(RecyclerItem.class).findAllAsync();

        for (RecyclerItem recyclerItem : realmResults) {
            list.add(new RecyclerItem(recyclerItem.getTitle(), recyclerItem.getContent(), recyclerItem.getURI(), recyclerItem.getId()));
            recyclerAdapter = new RecyclerAdapter(this, list);
        }
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            String uri = data.getStringExtra("URI");

            realm.beginTransaction();
            Number maxId = realm.where(RecyclerItem.class).max("id");
            int id = maxId == null ? 1 : maxId.intValue() + 1;
            recyclerItem = realm.createObject(RecyclerItem.class);
            recyclerItem.setTitle(title);
            recyclerItem.setContent(content);
            recyclerItem.setURI(uri);
            recyclerItem.setId(id);
            realm.commitTransaction();

            list.add(new RecyclerItem(title, content, uri, id));
            recyclerAdapter = new RecyclerAdapter(this, list);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    private void startNotepad() {
        intent = new Intent(getApplicationContext(), NotepadActivity.class);
        startActivityForResult(intent, 1);
    }

    private void permission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }


}