package com.wgu.brian.scheduler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgu.brian.scheduler.database.entities.Assessment;

import java.util.List;

class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {

    private List<Assessment> assessments;
    public static final String POSITION = "POSITION";

    public AssessmentAdapter(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    @NonNull
    @Override
    public AssessmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_row, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AssessmentAdapter.ViewHolder holder, final int position) {
        holder.name.setText(assessments.get(position).getName());
        holder.type.setText(assessments.get(position).getType());
        holder.due.setText(assessments.get(position).getDue_date());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.view.getContext(),AssessmentDetail.class);
                intent.putExtra(POSITION, assessments.get(holder.getAdapterPosition()).getId());
                holder.view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView type;
        public TextView due;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.assessment_name);
            type = itemView.findViewById(R.id.assessment_type);
            due = itemView.findViewById(R.id.assessment_due);
            view = itemView;
        }
    }
}
