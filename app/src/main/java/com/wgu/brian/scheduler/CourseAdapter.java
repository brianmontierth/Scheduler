package com.wgu.brian.scheduler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgu.brian.scheduler.database.entities.Course;

import java.util.List;

class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    
    private List<Course> courses;
    
    public CourseAdapter(List<Course> courses) {
        this.courses = courses;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_row, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        holder.courseName.setText(courses.get(position).getName());
        holder.courseStart.setText(courses.get(position).getStart_date());
        holder.courseEnd.setText(courses.get(position).getEnd_date());

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public TextView courseStart;
        public TextView courseEnd;
        
        public ViewHolder(View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name);
            courseStart = itemView.findViewById(R.id.course_start);
            courseEnd = itemView.findViewById(R.id.course_end);

        }
    }
}
