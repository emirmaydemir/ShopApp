package com.example.smartshopping.ViewHolder;

import android.content.Context;

import com.example.smartshopping.CategoryList;
import com.example.smartshopping.Home;
import com.example.smartshopping.R;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopping.Model.Category;


import java.util.LinkedList;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    Context context;
    LinkedList<Category> data;

    public Adapter(Context context, LinkedList<Category> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.customer_category,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.background.setImageResource(data.get(position).getBackground());
        holder.category_namee.setText(data.get(position).getCat_name());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView background;
        TextView category_namee;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            background=itemView.findViewById(R.id.category_imagee);
            category_namee=itemView.findViewById(R.id.category_namee);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Home.class);
                    int position=getAdapterPosition();
                    intent.putExtra("categoryy",data.get(position).getCat_id());
                    context.startActivity(intent);
                }
            });
        }
    }
}
