package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;

public class Register extends AppCompatActivity {
    Button hesap_olustur;
    EditText isim,tel,sifre;
    LinkedList<HashMap<String,Object>> linkedList=new LinkedList<HashMap<String,Object>>();
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hesap_olustur=findViewById(R.id.register_btn);
        isim=findViewById(R.id.register_username_input);
        tel=findViewById(R.id.register_phone_number_input);
        sifre=findViewById(R.id.register_password_input);
        progressDialog=new ProgressDialog(this);

        hesap_olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hesapOlustur();
            }
        });
    }
    public void hesapOlustur(){
        String name=isim.getText().toString();
        String phone=tel.getText().toString();
        String pass=sifre.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"İsminizi giriniz!!!",Toast.LENGTH_LONG).show();
        }

        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Telefon numaranızı giriniz!!!",Toast.LENGTH_LONG).show();
        }

        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Şifrenizi giriniz!!!",Toast.LENGTH_LONG).show();
        }
        else {
            progressDialog.setTitle("Hesap oluşturuluyor");
            progressDialog.setMessage("Lütfen biraz bekleyiniz");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Kontrol(name,phone,pass);
        }
    }

    public void Kontrol(String name, String phone, String pass){
        final DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> kullanici_verisi=new HashMap<>();
                    kullanici_verisi.put("phone",phone);
                    kullanici_verisi.put("password",pass);
                    kullanici_verisi.put("name",name);
                    linkedList.add(kullanici_verisi);

                    ref.child("Users").child(phone).updateChildren(kullanici_verisi)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register.this,"Kayıt işlemi tamamlanmıştır",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();

                                        Intent intent=new Intent(Register.this,Login.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this,"Ağ hatası",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(Register.this,"Bu numara zaten mevcut!!!",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Toast.makeText(Register.this,"Başka bir numara giriniz!!!",Toast.LENGTH_LONG).show();

                    Intent intent=new Intent(Register.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}