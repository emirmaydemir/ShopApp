package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smartshopping.Model.Users;
import com.example.smartshopping.Pre.Pre;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;



public class MainActivity extends AppCompatActivity {
    Button girisbtn,kayitbtn;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kayitbtn=findViewById(R.id.main_join_now_btn);
        girisbtn=findViewById(R.id.main_login_btn);
        progressDialog=new ProgressDialog(this);
        Paper.init(this);
        System.out.println("hahahaa");
        girisbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Login.class);
                startActivity(intent);
            }
        });

        kayitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        String phoneKey=Paper.book().read(Pre.UserPhoneKey);
        String passwordKey=Paper.book().read(Pre.UserPasswordKey);
        if(phoneKey!="" && passwordKey!=""){
            if(!TextUtils.isEmpty(phoneKey) && !TextUtils.isEmpty(passwordKey)){
                erisimIzni(phoneKey,passwordKey);

                progressDialog.setTitle("Zaten giriş yapıldı");
                progressDialog.setMessage("Lütfen biraz bekleyiniz");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        }
    }

    public void erisimIzni(final String phone, final String pass){
        final DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(phone).exists()){
                    Users data=snapshot.child("Users").child(phone).getValue(Users.class);

                    if(data.getPhone().equals(phone)){
                        if(data.getPassword().equals(pass)){
                            Toast.makeText(MainActivity.this,"Giriş Başarılı!!!",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                            Intent intent=new Intent(MainActivity.this,Home.class);
                            Pre.onlineUser=data;
                            startActivity(intent);
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Şifre yanlış tekrar deneyiniz!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,phone+"Telefon numarası bulunamadı!!!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
   
}