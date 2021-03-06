package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartshopping.Model.Users;
import com.example.smartshopping.Pre.Pre;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    public EditText tel,sifre;
    public Button grsbtn;
    public TextView admin,notadmin;
    public ProgressDialog progressDialog;
    public String parentname="Users";
    public CheckBox hatirla;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        grsbtn=findViewById(R.id.main_login_btn);
        sifre=findViewById(R.id.login_password_input);
        tel=findViewById(R.id.login_phone_number_input);
        admin=findViewById(R.id.admin_panel_link);
        notadmin=findViewById(R.id.not_admin_panel_link);
        progressDialog=new ProgressDialog(this);
        hatirla=findViewById(R.id.remember);
        Paper.init(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInAnonymously();

        grsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kullaniciGirisi();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grsbtn.setText("Login Admin");
                admin.setVisibility(View.INVISIBLE);
                notadmin.setVisibility(View.VISIBLE);
                parentname="Admins";
            }
        });

        notadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grsbtn.setText("Login");
                admin.setVisibility(View.VISIBLE);
                notadmin.setVisibility(View.INVISIBLE);
                parentname="Users";
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void kullaniciGirisi(){
        String phone=tel.getText().toString();
        String pass=sifre.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Telefon numaran??z?? giriniz!!!",Toast.LENGTH_LONG).show();
        }

        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"??ifrenizi giriniz!!!",Toast.LENGTH_LONG).show();
        }

        else{
            progressDialog.setTitle("Giri?? Yap??l??yor");
            progressDialog.setMessage("L??tfen biraz bekleyiniz");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            hesapErisim(phone,pass);
        }
    }

    public void hesapErisim(String phone, String pass){

        if(hatirla.isChecked()){
            Paper.book().write(Pre.UserPhoneKey,phone);
            Paper.book().write(Pre.UserPasswordKey,pass);
        }

        final DatabaseReference ref;
        ref= FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentname).child(phone).exists()){
                    Users data=snapshot.child(parentname).child(phone).getValue(Users.class);

                    if(data.getPhone().equals(phone)){
                        if(data.getPassword().equals(pass)){
                            mAuth.signInAnonymously().addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                }
                            });
                            if(parentname.equals("Admins")){
                                Toast.makeText(Login.this,"Admin giri??i ba??ar??l??!!!",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent=new Intent(Login.this,AdminKategori.class);
                                startActivity(intent);
                            }
                            else if(parentname.equals("Users")){
                                Toast.makeText(Login.this,"Giri?? Ba??ar??l??!!!",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                Intent intent=new Intent(Login.this,Home.class);
                                Pre.onlineUser=data;
                                startActivity(intent);
                            }
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"??ifre yanl???? tekrar deneyiniz!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(Login.this,phone+"Telefon numaras?? bulunamad??!!!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}