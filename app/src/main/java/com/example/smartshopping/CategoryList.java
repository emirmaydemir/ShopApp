package com.example.smartshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.smartshopping.Model.Category;
import com.example.smartshopping.ViewHolder.Adapter;

import java.util.LinkedList;

public class CategoryList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Window w=getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        RecyclerView recyclerView=findViewById(R.id.category_list);
        LinkedList<Category> mList=new LinkedList<>();
        mList.add(new Category(R.drawable.every,"Tüm Kategoriler","every"));
        mList.add(new Category(R.drawable.phonee,"Telefonlar","Phones"));
        mList.add(new Category(R.drawable.watch,"Saatler","Watches"));
        mList.add(new Category(R.drawable.keyboard,"Bilgisayarlar","Laptops"));
        mList.add(new Category(R.drawable.headphonesss,"Kulaklıklar","HeadPhone"));
        mList.add(new Category(R.drawable.shoess,"Ayakkabılar","Shoes"));
        mList.add(new Category(R.drawable.hatts,"Şapkalar","Bags"));
        mList.add(new Category(R.drawable.glass,"Çantalar","Hats"));
        mList.add(new Category(R.drawable.sunglass,"Gözlükler","Glasses"));
        mList.add(new Category(R.drawable.suitttt,"Ceketler","T-shirt"));
        mList.add(new Category(R.drawable.dressss,"Elbiseler","Dresses"));
        mList.add(new Category(R.drawable.tsh,"Tişörtler","Sports T-shirt"));
        mList.add(new Category(R.drawable.sweateeee,"Kazaklar","Sweathers"));

        Adapter adapter=new Adapter(this,mList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));






    }
}