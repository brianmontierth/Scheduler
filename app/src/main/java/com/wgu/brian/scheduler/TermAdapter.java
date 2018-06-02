package com.wgu.brian.scheduler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgu.brian.scheduler.database.entities.Term;

import java.util.List;

class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {

    private List<Term> terms;
    public static final String POSITION = "POSITION";

    public TermAdapter(List<Term> terms) {
        this.terms = terms;
    }

    @NonNull
    @Override
    public TermAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_row, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TermAdapter.ViewHolder holder, final int position) {
        holder.termName.setText(terms.get(position).getName());
        holder.termStart.setText(terms.get(position).getStart_date());
        holder.termEnd.setText(terms.get(position).getEnd_date());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.view.getContext(),TermDetail.class);
                intent.putExtra(POSITION, terms.get(holder.getAdapterPosition()).getId());
                holder.view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView termName;
        public TextView termStart;
        public TextView termEnd;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            termName = itemView.findViewById(R.id.term_name);
            termStart = itemView.findViewById(R.id.term_start);
            termEnd = itemView.findViewById(R.id.term_end);
            view = itemView;

        }
    }
}
