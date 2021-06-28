 package com.ortbraude.foodanalyzer;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

 public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {
    Context context;
    ArrayList<Bitmap> images;

    public ProgramAdapter(Context context, ArrayList<Bitmap> images) {
        this.context = context;
        this.images = images;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView rowImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowImage = itemView.findViewById(R.id.row_image);
        }
    }


    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_image_recycleview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ProgramAdapter.ViewHolder holder, int position) {
        holder.rowImage.setImageBitmap(images.get(position));
        holder.rowImage.setTag(position);
        holder.rowImage.setOnClickListener((View.OnClickListener) context);
    }

     @Override
    public int getItemCount() {
        if(images!=null)
            return images.size();
        return 0;
    }
}
