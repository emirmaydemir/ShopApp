package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.smartshopping.Model.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {
    //FloatingActionButton cart_add;
    ImageView pro_image;
    ElegantNumberButton number_btn;
    TextView pro_name,pro_descrition,pro_price;
    String ID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ID=getIntent().getStringExtra("pid");

        //cart_add=findViewById(R.id.cart);
        pro_image=findViewById(R.id.product_detailll);
        pro_name=findViewById(R.id.pro_detail_name);
        pro_descrition=findViewById(R.id.pro_detail_des);
        pro_price=findViewById(R.id.pro_detail_price);
        number_btn=findViewById(R.id.number_detail);

        getproDetails(ID);
    }

    private void getproDetails(String id) {
        DatabaseReference proref= FirebaseDatabase.getInstance().getReference().child("Products");
        proref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products=snapshot.getValue(Products.class);

                    pro_name.setText(products.getPname());
                    pro_descrition.setText(products.getDescription());
                    pro_price.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(pro_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}