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

public class NoteAdapter extends ListAdapter<Note,
        NoteAdapter.NoteHolder> {
    private ArrayList<Note> mNote;
    private Context mContext;
    private WritingAdapter.onItemClickListener listener;
    Note note;
    Intent data;

    private TextView mInfo;
    private TextView mCategory;
    private TextView mDate;

    protected NoteAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback) {
        super(diffCallback);
    }

    protected NoteAdapter(@NonNull AsyncDifferConfig<Note> config) {
        super(config);
    }

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.getInfo().equals(newItem.getInfo()) && oldItem.getCategory().
                            equals(newItem.getCategory()) && oldItem.getDate().equals(newItem.getDate());
                }
            };


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        mInfo.setText(note.getInfo());
        //note.setInfo(mInfo.toString());
        mCategory.setText(note.getCategory());
        //note.setCategory(mCategory.toString());
        mDate.setText(note.getDate());
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.listener = listener;
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView mInfo;
        private TextView mCategory;
        private TextView mDate;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            mInfo=itemView.findViewById(R.id.info);
            mCategory=itemView.findViewById(R.id.category);
            mDate=itemView.findViewById(R.id.date);
            itemView.setOnClickListener((View.OnClickListener) this);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position !=
                        RecyclerView.NO_POSITION) {
                    listener.onItemClick(mNote.get(position));
                    String title = data.getStringExtra(WritingActivity.EXTRA_TITLE);
                    String category = data.getStringExtra(WritingActivity.EXTRA_CATEGORY);
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                    String date = dateFormat.format(calendar.getTime());
                    //note = new Note(title, category, date);
                    mNote.add(note);
                }
            });
        }

        /**@Override
        public void onClick(View view) {
            Note note = mNote.get(getAdapterPosition());
            Intent data = new Intent(mContext, WritingActivity.class);
            mContext.startActivity(data);
        }*/
    }

    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(WritingAdapter.onItemClickListener listener) {
        this.listener = listener;
    }
}
