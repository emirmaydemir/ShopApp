package com.example.smartshopping.ViewHolder;
import com.example.smartshopping.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopping.Interfacee.ItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txt_proname,txt_proprice,txt_proquantity;
    public ItemClickListener itemClickListener;
    public ImageView basket_image;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_proname=itemView.findViewById(R.id.basket_pro_name);
        txt_proprice=itemView.findViewById(R.id.basket_pro_price);
        txt_proquantity=itemView.findViewById(R.id.basket_pro_quantity);
        basket_image=itemView.findViewById(R.id.image11);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
