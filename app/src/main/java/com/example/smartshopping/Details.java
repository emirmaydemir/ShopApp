package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.smartshopping.Model.Products;
import com.example.smartshopping.Pre.Pre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Details extends AppCompatActivity {
    public Button addtobasket;
    ImageView pro_image;
    ElegantNumberButton number_btn;
    TextView pro_name,pro_descrition,pro_price;
    final HashMap<String,Object> cartMap=new HashMap<>();
    String ID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ID=getIntent().getStringExtra("pid");
        addtobasket=findViewById(R.id.add_tobasket);
        pro_image=findViewById(R.id.product_detailll);
        pro_name=findViewById(R.id.pro_detail_name);
        pro_descrition=findViewById(R.id.pro_detail_des);
        pro_price=findViewById(R.id.pro_detail_price);
        number_btn=findViewById(R.id.number_detail);

        getproDetails(ID);

        addtobasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBasket();
            }
        });

    }

    private void addBasket() {
        DatabaseReference databaseReferencee= FirebaseDatabase.getInstance().getReference().child("Products").child(ID);
        String savetime,savedate;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM dd, yyyy");
        savedate=simpleDateFormat.format(calendar.getTime());

        SimpleDateFormat simpleTimeFormat=new SimpleDateFormat("HH:mm:ss a");
        savetime=simpleDateFormat.format(calendar.getTime());

        databaseReferencee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String imagee=snapshot.child("image").getValue().toString();
                    cartMap.put("image",imagee);
                    cartMap.put("pid",ID);
                    cartMap.put("pname",pro_name.getText().toString());
                    cartMap.put("price",pro_price.getText().toString());
                    cartMap.put("date",savedate);
                    cartMap.put("time",savetime);
                    cartMap.put("quantity",number_btn.getNumber());
                    cartMap.put("discount",ID);

                    final DatabaseReference basketRef=FirebaseDatabase.getInstance().getReference().child("Cart List");

                    basketRef.child("User View").child(Pre.onlineUser.getPhone())
                            .child("Products").child(ID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                basketRef.child("Admin View").child(Pre.onlineUser.getPhone())
                                        .child("Products").child(ID).updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Details.this,"Sepete eklendi",Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(Details.this,Home.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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