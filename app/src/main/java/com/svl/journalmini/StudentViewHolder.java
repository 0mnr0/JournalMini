package com.svl.journalmini;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class StudentViewHolder extends RecyclerView.ViewHolder {
    public ImageView studentImage;
    public TextView studentName;
    public TextView gamePoints;

    public StudentViewHolder(View itemView) {
        super(itemView);
        studentImage = itemView.findViewById(R.id.StudentImage);
        studentName = itemView.findViewById(R.id.StudentName);
        gamePoints = itemView.findViewById(R.id.GamePoints);
    }
}