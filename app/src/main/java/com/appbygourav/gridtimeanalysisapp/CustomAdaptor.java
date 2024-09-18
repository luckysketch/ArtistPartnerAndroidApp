package com.appbygourav.gridtimeanalysisapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appbygourav.Service.CommonUtility;
import com.appbygourav.beans.ProjectDetail;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.viewHolder> {

    private static ClickListener clickListener;

    List<ProjectDetail> list;
    Context context;

    public CustomAdaptor(List<ProjectDetail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recycler_view_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {

        ProjectDetail p1=list.get(position);
        loadImageFromInternal(p1.getReference_img(),holder);
        holder.recycler_project_name.setText(p1.getName());
        String dateString  = CommonUtility.getEnglishDateStrFromOldDbFormat(p1.getLast_activity());
        holder.recycler_last_activity.setText("last active on "+dateString);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        ImageView reference_img1;
        TextView recycler_project_name,recycler_last_activity;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            reference_img1=itemView.findViewById(R.id.reference_img1);
            recycler_project_name=itemView.findViewById(R.id.recycler_project_name);
            recycler_last_activity=itemView.findViewById(R.id.prjCardLastActivityText);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),v);
        }
    }

    private void loadImageFromInternal(String path, viewHolder holder){
        File f= new File(path);
        Bitmap b= null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            holder.reference_img1.setImageBitmap(b);
        } catch (FileNotFoundException e) {

        }
    }
    public interface ClickListener{
        void onItemClick(int position,View v);
    }
    public void setOnItemClickListener(ClickListener clickListener){
        CustomAdaptor.clickListener=clickListener;
    }
}
