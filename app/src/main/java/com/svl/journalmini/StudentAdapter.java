package com.svl.journalmini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
    private List<Student> studentList;
    private Context context;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_layout, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.studentName.setText(student.getName());
        holder.gamePoints.setText(String.valueOf(student.getGamePoints()));
        holder.studentImage.setTooltipText(student.getImageUrl());

        Glide.with(context)
                .load(student.getImageUrl())
                .transform(new CenterCrop(), new RoundedCorners(20))
                .into(holder.studentImage);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
