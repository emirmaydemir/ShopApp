package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartshopping.Model.Products;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Stack;




public class Admin extends AppCompatActivity {
    public String category_name,Productname,Productprice,Productdescription,savetime,savedate;
    public Button addproduct;
    public ImageView addImage;
    public EditText product_name,product_des,product_price;
    public static final int gallerypick=1;
    public Uri imageUri;
    public String productRandomKey,downloadimageUrl;
    public StorageReference ProductImagesRef;
    public DatabaseReference ProductRef;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        addImage=findViewById(R.id.select_image);
        addproduct=findViewById(R.id.add_product);
        product_name=findViewById(R.id.product_name);
        product_des=findViewById(R.id.product_des);
        product_price=findViewById(R.id.product_price);
        progressDialog=new ProgressDialog(this);


        category_name=getIntent().getExtras().get("category").toString();
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef=FirebaseDatabase.getInstance().getReference().child("Products");

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galeriyiAc();
            }
        });

        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veriKontrol();
            }
        });
    }

    public void galeriyiAc(){
        Intent galerry=new Intent();
        galerry.setAction(Intent.ACTION_GET_CONTENT);
        galerry.setType("image/*");
        startActivityForResult(galerry,gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallerypick && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            addImage.setImageURI(imageUri);
        }
    }

    public void veriKontrol(){
        Productname=product_name.getText().toString();
        Productdescription=product_des.getText().toString();
        Productprice=product_price.getText().toString();

        if (imageUri==null){
            Toast.makeText(this,"Ürüne resim ekleyiniz",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Productname)){
            Toast.makeText(this,"Ürüne isim ekleyiniz",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Productdescription)){
            Toast.makeText(this,"Ürüne açıklama ekleyiniz",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Productprice)){
            Toast.makeText(this,"Ürüne fiyat ekleyiniz",Toast.LENGTH_SHORT).show();
        }
        else{
            bilgileriDepola();
        }
    }
    public void bilgileriDepola(){
        progressDialog.setTitle("Yeni ürün ekleniyor");
        progressDialog.setMessage("Lütfen biraz bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat dateformat=new SimpleDateFormat("MMM dd, yyyy");
        savedate=dateformat.format(calendar.getTime());

        SimpleDateFormat timeformat=new SimpleDateFormat("HH:mm:ss a");
        savetime=timeformat.format(calendar.getTime());

        productRandomKey=savedate+savetime;

        StorageReference filepath=ProductImagesRef.child(imageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask=filepath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mesaj=e.toString();
                Toast.makeText(Admin.this,"Hata: "+mesaj,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admin.this,"İşlem başarılı",Toast.LENGTH_SHORT).show();

                Task<Uri>ırlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                       if(!task.isSuccessful()){
                            throw task.getException();
                       }
                       downloadimageUrl=filepath.getDownloadUrl().toString();
                       return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadimageUrl=task.getResult().toString();
                            Toast.makeText(Admin.this,"Resmin url işlemi başarılı",Toast.LENGTH_SHORT).show();

                            saveProInformation();
                        }
                    }
                });
            }
        });
    }
    public void saveProInformation(){
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",savedate);
        productMap.put("time",savetime);
        productMap.put("description",Productdescription);
        productMap.put("image",downloadimageUrl);
        productMap.put("category",category_name);
        productMap.put("price",Productprice);
        productMap.put("pname",Productname);
        Products products=new Products(category_name,savedate,Productdescription,downloadimageUrl,productRandomKey,Productname,Productprice,savetime);

        ProductRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(Admin.this,AdminKategori.class);
                    startActivity(intent);

                    progressDialog.dismiss();
                    Toast.makeText(Admin.this,"Ürün ekleme işlemi başarılı",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    String mesaj=task.getException().toString();
                    Toast.makeText(Admin.this,"Hata: "+mesaj,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}