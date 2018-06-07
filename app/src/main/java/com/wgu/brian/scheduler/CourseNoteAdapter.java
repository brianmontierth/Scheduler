package com.wgu.brian.scheduler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgu.brian.scheduler.database.entities.CourseNote;

import java.util.List;

class CourseNoteAdapter extends RecyclerView.Adapter<CourseNoteAdapter.ViewHolder> {
    public static final String POSITION = "POSITION";
    private List<CourseNote> courseNoteList;

    public CourseNoteAdapter(List<CourseNote> courseNoteList) {
        this.courseNoteList = courseNoteList;
    }

    @NonNull
    @Override
    public CourseNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.note.setText(courseNoteList.get(position).getNote());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.view.getContext(), CourseNoteDetail.class);
                intent.putExtra(POSITION, courseNoteList.get(holder.getAdapterPosition()).getId());
                holder.view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return courseNoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView note;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note_text);
            view = itemView;
        }
    }
}
