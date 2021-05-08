package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartshopping.Pre.Pre;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    public CircleImageView profile_image;
    public EditText nametxt,phonetxt,addresstxt;
    public TextView imagechange,save,close;
    public StorageTask uploadTask;
    public Uri imageUri;
    public String myUrl="";
    public StorageReference profile_image_ref;
    public String check="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profile_image_ref= FirebaseStorage.getInstance().getReference().child("Profile pictures");

        profile_image=findViewById(R.id.profile_image);
        nametxt=findViewById(R.id.set_name);
        phonetxt=findViewById(R.id.set_phone);
        addresstxt=findViewById(R.id.set_address);
        imagechange=findViewById(R.id.image_change);
        save=findViewById(R.id.update_set);
        close=findViewById(R.id.close_set);

        userscreen(profile_image,nametxt,phonetxt,addresstxt);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.equals("Clicked")){
                    userinfoSave();
                }
                else{
                    updateuserInfo();
                }
            }
        });

        imagechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check="Clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(Settings.this);
            }
        });

    }

    public  void updateuserInfo(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();

        if(TextUtils.isEmpty(nametxt.getText().toString())){
            Toast.makeText(Settings.this,"İsim girilmesi zorunludur...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phonetxt.getText().toString())){
            Toast.makeText(Settings.this,"Telefon numarası girilmesi zorunludur...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addresstxt.getText().toString())){
            Toast.makeText(Settings.this,"Adres girilmesi zorunludur...",Toast.LENGTH_SHORT).show();
        }
        else{
            userMap.put("name",nametxt.getText().toString());
            userMap.put("address",addresstxt.getText().toString());
            userMap.put("phoneOrder",phonetxt.getText().toString());
            Pre.onlineUser.setName(nametxt.getText().toString());
            databaseReference.child(Pre.onlineUser.getPhone()).updateChildren(userMap);

            Intent intent=new Intent(Settings.this,Home.class);
            startActivity(intent);
            Toast.makeText(Settings.this,"Profil güncelleme işlemi başarılı",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profile_image.setImageURI(imageUri);
            Pre.onlineUser.setImage(imageUri.toString());
        }
        else{
            Toast.makeText(Settings.this,"HATA TEKRAR DENE",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this,Settings.class));
            finish();
        }
    }

    public void userinfoSave(){
        if(TextUtils.isEmpty(nametxt.getText().toString())){
            Toast.makeText(Settings.this,"İsim girilmesi zorunludur...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phonetxt.getText().toString())){
            Toast.makeText(Settings.this,"Telefon numarası girilmesi zorunludur...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addresstxt.getText().toString())){
            Toast.makeText(Settings.this,"Adres girilmesi zorunludur...",Toast.LENGTH_SHORT).show();
        }
        else if(check.equals("Clicked")){
            uploadimage();
        }
    }

    private void uploadimage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Resim güncelleniyor");
        progressDialog.setMessage("Resim güncelleniyor lütfen bekleyiniz");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri!=null){
            final StorageReference fileref=profile_image_ref.child(Pre.onlineUser.getPhone() + ".jpg");
            uploadTask=fileref.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri dowloadUrl=task.getResult();
                        myUrl=dowloadUrl.toString();
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("name",nametxt.getText().toString());
                        userMap.put("address",addresstxt.getText().toString());
                        userMap.put("phoneOrder",phonetxt.getText().toString());
                        userMap.put("image",myUrl);
                        Pre.onlineUser.setName(nametxt.getText().toString());
                        databaseReference.child(Pre.onlineUser.getPhone()).updateChildren(userMap);

                        progressDialog.dismiss();

                        Intent intent=new Intent(Settings.this,Home.class);
                        startActivity(intent);
                        Toast.makeText(Settings.this,"Profil güncelleme işlemi başarılı",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(Settings.this,"Hata yeniden deneyiniz  ...",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(Settings.this,"Resim seçilmedi  ...",Toast.LENGTH_SHORT).show();
        }
    }

    public void userscreen(CircleImageView profile_image, EditText nametxt, EditText phonetxt, EditText addresstxt) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(Pre.onlineUser.getPhone());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if (snapshot.child("image").exists()){
                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profile_image);
                        nametxt.setText(name);
                        phonetxt.setText(phone);
                        addresstxt.setText(address);
                    }
                    else if(snapshot.child("address").exists()){
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();

                        nametxt.setText(name);
                        phonetxt.setText(phone);
                        addresstxt.setText(address);
                    }
                    else {
                        String name=snapshot.child("name").getValue().toString();
                        String phone=snapshot.child("phone").getValue().toString();
                        nametxt.setText(name);
                        phonetxt.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


}