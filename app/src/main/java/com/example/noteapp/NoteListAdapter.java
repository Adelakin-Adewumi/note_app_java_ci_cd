package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NoteListAdapter extends ListAdapter<Note, WritingAdapter>  {
    private ArrayList<Note> mNote;
    private Context mContext;
    private static RecyclerViewClickListener clickListener;
    private RecyclerView mRecyclerView;
    Note note;
    private onItemClickListener listener;


    //mRecyclerView = findViewById(R.id.recyclerView);

    protected NoteListAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback, onItemClickListener listener) {
        super(diffCallback);
    }

    protected NoteListAdapter(@NonNull AsyncDifferConfig<Note> config) {
        super(config);
    }

    public NoteListAdapter(WordDiff wordDiff) {
        super(wordDiff);
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public WritingAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return WritingAdapter.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull WritingAdapter holder, int position) {
        Note note = getItem(position);
        holder.bindTo(note);
    }



    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mInfo;
        private TextView mCategory;
        private TextView mDate;

        public TextView getmInfo() {
            return mInfo;
        }

        public TextView getmCategory() {
            return mCategory;
        }

        public TextView getmDate() {
            return mDate;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mInfo=itemView.findViewById(R.id.info);
            mCategory=itemView.findViewById(R.id.category);
            mDate=itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position !=
                        RecyclerView.NO_POSITION) {
                    listener.onItemClick(mNote.get(position));
                }
            });
        }



        @Override
        public void onClick(View view) {
            Note note = mNote.get(getAdapterPosition());
            Intent data = new Intent(mContext, WritingActivity.class);
            mContext.startActivity(data);
        }

    }
    static class WordDiff extends DiffUtil.ItemCallback<Note> {

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull
                Note newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getInfo().equals(newItem.getInfo());
        }

    }

}