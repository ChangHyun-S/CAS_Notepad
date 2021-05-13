package com.casproject.casnotepad.Recycler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casproject.casnotepad.R;

import java.util.List;

import io.realm.Realm;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Activity activity;
    private List<RecyclerItem> recyclerItems;
    private Realm realm;

    public RecyclerAdapter(Activity activity, List<RecyclerItem> recyclerItems) {
        this.activity = activity;
        this.recyclerItems = recyclerItems;
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notepad, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return null;
    }

    @org.jetbrains.annotations.NotNull
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull RecyclerAdapter.ViewHolder holder, int position) {
        RecyclerItem recyclerItem = recyclerItems.get(position);
        holder.title.setText(recyclerItem.getTitle());
        holder.content.setText(recyclerItem.getContent());
    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.notepadTitle);
            content = view.findViewById(R.id.notepadContent);

        }
    }

}
