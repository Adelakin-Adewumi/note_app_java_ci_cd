package com.example.noteapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class NoteAdapter extends ListAdapter<Note,
        NoteAdapter.NoteHolder> {

    private onItemClickListener listener;

    protected NoteAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback) {
        super(diffCallback);
    }

    protected NoteAdapter(@NonNull AsyncDifferConfig<Note> config) {
        super(config);
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
    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem == newItem;
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.getInfo().equals(newItem.getInfo()) && oldItem.getCategory().equals(newItem.getCategory()) &&
                            oldItem.getDate().equals(newItem.getDate()) && oldItem.getTime().equals(newItem.getTime());
                }
            };


    public Note getNoteAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public NoteAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteHolder holder, int position) {
        Note note = getItem(position);
        holder.mInfo.setText(note.getInfo());
        holder.mCategory.setText(note.getCategory());
        holder.mDate.setText(note.getDate());
        holder.mTime.setText(note.getTime());
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView mInfo;
        private TextView mCategory;
        private TextView mDate;
        private TextView mTime;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            mInfo=itemView.findViewById(R.id.info);
            mCategory=itemView.findViewById(R.id.category);
            mDate = itemView.findViewById(R.id.date);
            mTime = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position !=
                            RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

}
