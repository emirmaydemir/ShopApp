package com.example.smartshopping.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.smartshopping.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopping.Interfacee.ItemClickListener;

public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemClickListener itemClickListener;
    public TextView comment_uname,comment_ucontent,udate;
    public ImageView comment_uimg;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        comment_uname=itemView.findViewById(R.id.comment_name);
        comment_uimg=itemView.findViewById(R.id.comment_img);
        comment_ucontent=itemView.findViewById(R.id.comment_content);
        udate=itemView.findViewById(R.id.comment_date);
    }

    @Override
    public void onClick(View v) {

    }
}
