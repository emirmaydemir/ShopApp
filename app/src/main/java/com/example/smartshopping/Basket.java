package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartshopping.Model.Cart;
import com.example.smartshopping.Pre.Pre;
import com.example.smartshopping.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Basket extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public Button nextbtn;
    public TextView totalprice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        recyclerView=findViewById(R.id.basket_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextbtn=findViewById(R.id.next_button);
        totalprice=findViewById(R.id.total_price);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference basketref= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(basketref.child("User View")
                        .child(Pre.onlineUser.getPhone()).child("Products"),Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter=new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                cartViewHolder.txt_proquantity.setText("Ürün miktarı: "+cart.getQuantity());
                cartViewHolder.txt_proprice.setText("Ürün fiyatı: "+cart.getPrice()+"$");
                cartViewHolder.txt_proname.setText(cart.getPname());
                Picasso.get().load(cart.getImage()).into(cartViewHolder.basket_image);

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[] {
                                "Duzenle",
                                "Sil"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(Basket.this);
                        builder.setTitle("Sepet ayarları");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {
                                    Intent intent = new Intent(Basket.this, Details.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                }
                                if(which==1){
                                    basketref.child("User View")
                                            .child(Pre.onlineUser.getPhone())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        basketref.child("Admin View").child(Pre.onlineUser.getPhone())
                                                                .child("Products").child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(Basket.this,"Ürün silindi",Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(Basket.this, Home.class);
                                                                    startActivity(intent);
                                                                }
                                                                else{
                                                                    Toast.makeText(Basket.this,"Ürün silme işlemi başarısız",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }


                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_items_layout,parent,false);
                CartViewHolder viewHolder=new CartViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}