package com.example.smartshopping.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.smartshopping.Interfacee.ItemClickListener;
import com.example.smartshopping.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView ProductDescription,ProductName,ProductPrice;
    public ImageView ProductImage;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        ProductImage=itemView.findViewById(R.id.product_imagee);
        ProductName=itemView.findViewById(R.id.product_namee);
        ProductDescription=itemView.findViewById(R.id.product_desc);
        ProductPrice=itemView.findViewById(R.id.product_pricee);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
