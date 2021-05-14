package com.casproject.casnotepad.Recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.casproject.casnotepad.ModNotepadActivity;
import com.casproject.casnotepad.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    public Activity activity;
    private List<RecyclerItem> recyclerItems;

    public RecyclerAdapter(Activity activity, List<RecyclerItem> recyclerItems) {
        this.activity = activity;
        this.recyclerItems = recyclerItems;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview, parent, false);
        RecyclerAdapter.ViewHolder viewHolder = new RecyclerAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        RecyclerItem dat = recyclerItems.get(position);
        holder.notepadTitle.setText(dat.getTitle());
        holder.notepadContent.setText(dat.getContent());
        holder.notepadDate.setText(dat.getDate());
    }

    @Override
    public int getItemCount() {
        return recyclerItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView notepadTitle, notepadContent, notepadDate;

        public ViewHolder(View view) {
            super(view);

            notepadTitle = view.findViewById(R.id.notepadTitle);
            notepadContent = view.findViewById(R.id.notepadContent);
            notepadDate = view.findViewById(R.id.notepadDate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = view.getContext();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", getBindingAdapterPosition());

                    Intent modIntent = new Intent(context, ModNotepadActivity.class);

                    modIntent.putExtras(bundle);
                    modIntent.putExtra("title", recyclerItems.get(getBindingAdapterPosition()).getTitle());
                    modIntent.putExtra("content", recyclerItems.get(getBindingAdapterPosition()).getContent());
                    modIntent.putExtra("URI", recyclerItems.get(getBindingAdapterPosition()).getURI());
                    modIntent.putExtra("id", recyclerItems.get(getBindingAdapterPosition()).getId());
                    modIntent.putExtra("date", recyclerItems.get(getBindingAdapterPosition()).getDate());

                    context.startActivity(modIntent);
                }
            });
        }
    }
}
