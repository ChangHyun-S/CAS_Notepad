package com.casproject.casnotepad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.casproject.casnotepad.Recycler.RecyclerAdapter;
import com.casproject.casnotepad.Recycler.RecyclerItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    public List<RecyclerItem> list = new ArrayList<>();
    private Intent intent;
    private Realm realm;
    private RecyclerItem recyclerItem;
    private RecyclerAdapter recyclerAdapter;
    private FloatingActionButton floatingAddButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        permission();

        floatingAddButton.setOnClickListener(v -> {
            startNotepad();
        });
    }

    private void init() {
        // Binding
        floatingAddButton = findViewById(R.id.notepadAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Realm
        Realm.init(this);
        // UiThread true
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(configuration);
        realm = Realm.getDefaultInstance();

        RealmResults<RecyclerItem> realmResults = realm.where(RecyclerItem.class)
                .sort("date", Sort.DESCENDING)
                .findAllAsync();

        // Recyclerview Adapter
        for (RecyclerItem recyclerItem : realmResults) {
            list.add(new RecyclerItem(recyclerItem.getTitle(), recyclerItem.getContent(), recyclerItem.getURI(), recyclerItem.getId(), recyclerItem.getDate()));
            recyclerAdapter = new RecyclerAdapter(this, list);
        }
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Intent -> DB
        if (resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String content = data.getStringExtra("content");
            String uri = data.getStringExtra("URI");
            String date = data.getStringExtra("date");

            realm.beginTransaction();

            // 메모 ID로 구분
            Number maxId = realm.where(RecyclerItem.class).max("id");
            // ID 없으면 1, 있다면 + 1
            int id = maxId == null ? 1 : maxId.intValue() + 1;

            recyclerItem = realm.createObject(RecyclerItem.class);
            recyclerItem.setTitle(title);
            recyclerItem.setContent(content);
            recyclerItem.setURI(uri);
            recyclerItem.setId(id);
            recyclerItem.setDate(date);

            realm.commitTransaction();

            list.add(new RecyclerItem(title, content, uri, id, date));
            recyclerAdapter = new RecyclerAdapter(this, list);
            recyclerView.setAdapter(recyclerAdapter);
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "제목이 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    // goto NotepadActivity
    private void startNotepad() {
        intent = new Intent(getApplicationContext(), NotepadActivity.class);
        startActivityForResult(intent, 1);
    }

    // Chect Permission
    private void permission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}