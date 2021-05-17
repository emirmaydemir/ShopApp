package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.smartshopping.Model.Cart;
import com.example.smartshopping.Model.Comment;
import com.example.smartshopping.Model.Products;
import com.example.smartshopping.Pre.Pre;
import com.example.smartshopping.ViewHolder.CommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    DatabaseReference fire;
    ImageView pro_image,det_img;
    ElegantNumberButton number_btn;
    TextView pro_name,pro_descrition,pro_price,det_add;
    EditText det_comment;
    final HashMap<String,Object> cartMap=new HashMap<>();
    String ID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ID=getIntent().getStringExtra("pid");
        det_img=findViewById(R.id.detail_image);
        det_add=findViewById(R.id.detail_btn);
        det_comment=findViewById(R.id.detail_comment);
        addtobasket=findViewById(R.id.add_tobasket);
        pro_image=findViewById(R.id.product_detailll);
        pro_name=findViewById(R.id.pro_detail_name);
        pro_descrition=findViewById(R.id.pro_detail_des);
        pro_price=findViewById(R.id.pro_detail_price);
        number_btn=findViewById(R.id.number_detail);
        det_comment.setFocusable(true);
        det_comment.requestFocus();

        recyclerView=findViewById(R.id.comment_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getproDetails(ID);

        addtobasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBasket();
            }
        });

        det_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference commentreff= FirebaseDatabase.getInstance().getReference().child("Comment");
        FirebaseRecyclerOptions<Comment> options=new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(commentreff.child(ID),Comment.class).build();
        FirebaseRecyclerAdapter<Comment, CommentViewHolder>adapter=new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i, @NonNull Comment comment) {
                commentViewHolder.comment_uname.setText(comment.getUname());
                commentViewHolder.comment_ucontent.setText(comment.getContent());
                commentViewHolder.udate.setText(comment.getDate());
                Picasso.get().load(comment.getUimg()).into(commentViewHolder.comment_uimg);

            }


            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment,parent,false);
                CommentViewHolder viewHolder=new CommentViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }






    private void addComment() {
        HashMap<String,Object> kullanici_yorumu=new HashMap<>();
        String comment=det_comment.getText().toString();
        String savedate;
        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat dateformat=new SimpleDateFormat("MMM dd, yyyy");
        savedate=dateformat.format(calendar.getTime());

        kullanici_yorumu.put("content",comment);
        kullanici_yorumu.put("uimg",Pre.onlineUser.getImage());
        kullanici_yorumu.put("uname",Pre.onlineUser.getName());
        kullanici_yorumu.put("pid",ID);
        kullanici_yorumu.put("date",savedate);
        DatabaseReference commentref=FirebaseDatabase.getInstance().getReference("Comment").child(ID).push();
        commentref.setValue(kullanici_yorumu).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Details.this,"Yorum gönderme işlemi başarılı!!!",Toast.LENGTH_SHORT).show();
                    det_comment.setText("");
                    det_add.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Details.this,"Yorum gönderme işleminde bir hata oluştu!!!",Toast.LENGTH_SHORT).show();
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
                    fire=FirebaseDatabase.getInstance().getReference().child("Users").child(Pre.onlineUser.getPhone());
                    fire.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                if(snapshot.child("image").exists()){
                                    String image=snapshot.child("image").getValue().toString();
                                    Picasso.get().load(image).into(det_img);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}