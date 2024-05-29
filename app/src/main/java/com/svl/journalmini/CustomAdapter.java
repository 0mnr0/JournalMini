package com.svl.journalmini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context context;
    private List<DataModel> dataList;

    public CustomAdapter(Context context, List<DataModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shedulestart, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        DataModel data = dataList.get(position);
        holder.subjectName.setText(data.getSubjectName());
        holder.teacherName.setText(data.getTeacherName());
        holder.finishedAt.setText(data.getFinishedAt());
        holder.startedAt.setText(data.getStartedAt());
        holder.auditory.setText(data.getAuditory());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName, teacherName, finishedAt, startedAt, auditory;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subject_nameID);
            teacherName = itemView.findViewById(R.id.teacher_nameID);
            finishedAt = itemView.findViewById(R.id.finished_atID);
            startedAt = itemView.findViewById(R.id.started_atID);
            auditory = itemView.findViewById(R.id.auditoryID);
        }
    }
}
