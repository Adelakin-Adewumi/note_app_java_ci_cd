package com.example.noteapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WritingAdapter extends RecyclerView.Adapter<WritingAdapter.ViewHolder> {
    private final ArrayList<Note> mNote;
    private Context mContext;
    private onItemClickListener listener;

    public WritingAdapter(ArrayList<Note> mNote, Context mContext) {
        this.mNote = mNote;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = mNote.get(position);
        String info = mNote.get(position).getInfo();
        String category = mNote.get(position).getCategory();
        String date = mNote.get(position).getDate();
        holder.mInfo.setText(info);
        holder.mCategory.setText(category);
        holder.mDate.setText(date);
        //holder.bindTo(note);
    }

    @Override
    public int getItemCount() {
        return mNote.size();
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position !=
                            RecyclerView.NO_POSITION) {
                        listener.onItemClick(mNote.get(position));
                    }
                }
            });
        }


        void bindTo(Note note) {
            mInfo.setText(note.getInfo());
            note.setInfo(mInfo.toString());
            mCategory.setText(note.getCategory());
            note.setCategory(mCategory.toString());
            mDate.setText(note.getDate());
            note.setDate(mDate.getText().toString());
        }
        @Override
        public void onClick(View view) {
            Note note = mNote.get(getAdapterPosition());
            Intent data = new Intent(mContext, WritingActivity.class);
            mContext.startActivity(data);
        }

    }


    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
