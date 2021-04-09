package com.example.smartshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminKategori extends AppCompatActivity {
    public ImageView tshirt,sports,dress,sweat;
    public ImageView glass,hats,bags,shoes;
    public ImageView headphone,laptop,watches,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kategori);

        tshirt=findViewById(R.id.t_shirt);
        sports=findViewById(R.id.sports_t_shirt);
        dress=findViewById(R.id.female_t_shirt);
        sweat=findViewById(R.id.sweather);
        glass=findViewById(R.id.glasses);
        hats=findViewById(R.id.hats);
        bags=findViewById(R.id.bags);
        shoes=findViewById(R.id.shoes);
        headphone=findViewById(R.id.headphone);
        laptop=findViewById(R.id.laptop);
        watches=findViewById(R.id.watches);
        phone=findViewById(R.id.phone);



        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","T-shirt");
                startActivity(intent);
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Sports T-shirt");
                startActivity(intent);
            }
        });

        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Dresses");
                startActivity(intent);
            }
        });

        sweat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Sweathers");
                startActivity(intent);
            }
        });

        glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Glasses");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Hats");
                startActivity(intent);
            }
        });

        bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Bags");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Shoes");
                startActivity(intent);
            }
        });

        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","HeadPhone");
                startActivity(intent);
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Watches");
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategori.this,Admin.class);
                intent.putExtra("category","Phones");
                startActivity(intent);
            }
        });


    }
}