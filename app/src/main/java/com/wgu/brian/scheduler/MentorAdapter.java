package com.wgu.brian.scheduler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgu.brian.scheduler.database.entities.Mentor;

import java.util.List;

class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.ViewHolder> {
    private List<Mentor> mentors;
    public static final String POSITION = "POSITION";
    public static final String PARENT_ID = "PARENT_ID";

    public MentorAdapter(List<Mentor> mentors) {
        this.mentors = mentors;
    }

    @NonNull
    @Override
    public MentorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_row, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mentors.get(position).getName());
        holder.phone.setText(mentors.get(position).getPhone());
        holder.email.setText(mentors.get(position).getEmail());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.view.getContext(),MentorDetail.class);
                intent.putExtra(POSITION, mentors.get(holder.getAdapterPosition()).getId());
                intent.putExtra(PARENT_ID, mentors.get(holder.getAdapterPosition()).getCourse_id());
                holder.view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView phone;
        public TextView email;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mentor_name);
            phone = itemView.findViewById(R.id.mentor_phone);
            email = itemView.findViewById(R.id.mentor_email);
            view = itemView;
        }
    }
}
