package com.example.smartshopping;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartshopping.Model.Products;
import com.example.smartshopping.Model.Users;
import com.example.smartshopping.Pre.Pre;
import com.example.smartshopping.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;




public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Button searchbtn;
    EditText editText;
    public String searchin="";
    public String cat_name="";
    RecyclerView searchlist;
    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    public static Stack<Products> stack=new Stack<Products>();
    RecyclerView.LayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ProductsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        searchbtn=findViewById(R.id.search_btn);
        if (getIntent().getExtras()!=null) {
            cat_name = getIntent().getStringExtra("categoryy");
            searchin="99999999999999999999";
        }
        editText=findViewById(R.id.search_product_name);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchin=editText.getText().toString();
                onStart();
            }
        });

        Paper.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Basket.class);
                startActivity(intent);
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView=navigationView.getHeaderView(0);
        TextView username=headerView.findViewById(R.id.user_name);
        CircleImageView profileImage=headerView.findViewById(R.id.profile_image);

        username.setText(Pre.onlineUser.getName());
        Picasso.get().load(Pre.onlineUser.getImage()).placeholder(R.drawable.profile).into(profileImage);

        recyclerView = findViewById(R.id.rec_menu);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this,2, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);



/*
        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    Products products=snapshot1.getValue(Products.class);
                    stack.push(products);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
    }


    @Override
    protected void onStart() {
        super.onStart();
         if(searchin.equals("99999999999999999999") && !cat_name.equals("") && !cat_name.equals("every")){
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Products");
            FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(ref.orderByChild("category").equalTo(cat_name),Products.class).build();

            FirebaseRecyclerAdapter<Products,ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                    productViewHolder.ProductName.setText(products.getPname());
                    productViewHolder.ProductDescription.setText(products.getDescription());
                    productViewHolder.ProductPrice.setText("Price = "+products.getPrice()+ "$");
                    Picasso.get().load(products.getImage()).into(productViewHolder.ProductImage);

                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Home.this,Details.class);
                            intent.putExtra("pid",products.getPid());
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                    ProductViewHolder holder = new ProductViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
        else if(searchin.equals("") || cat_name.equals("every")) {
            FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(ProductsRef, Products.class).build();

            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                    productViewHolder.ProductName.setText(products.getPname());
                    productViewHolder.ProductDescription.setText(products.getDescription());
                    productViewHolder.ProductPrice.setText("Price = " + products.getPrice() + "$");
                    Picasso.get().load(products.getImage()).into(productViewHolder.ProductImage);

                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Home.this, Details.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                    ProductViewHolder productViewHolder = new ProductViewHolder(view);
                    return productViewHolder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            cat_name="";
        }

        else{
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Products");
            FirebaseRecyclerOptions<Products>options=new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(reference.orderByChild("pname").startAt(searchin).endAt(searchin+"\uf8ff"),Products.class).build();

            FirebaseRecyclerAdapter<Products,ProductViewHolder>adapter=new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                    productViewHolder.ProductName.setText(products.getPname());
                    productViewHolder.ProductDescription.setText(products.getDescription());
                    productViewHolder.ProductPrice.setText("Price = "+products.getPrice()+ "$");
                    Picasso.get().load(products.getImage()).into(productViewHolder.ProductImage);

                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Home.this,Details.class);
                            intent.putExtra("pid",products.getPid());
                            startActivity(intent);
                        }
                    });
                }

                @NonNull
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                    ProductViewHolder holder = new ProductViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart)
        {
            Intent intent=new Intent(Home.this,Basket.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_orders)
        {

        }
        else if (id == R.id.nav_category)
        {
            Intent intent=new Intent(Home.this,CategoryList.class);
            startActivity(intent);
              /*
            if(!stack.empty()) {
                System.out.println(stack.pop().getPname());
            }
            else{
                System.out.println("Ürün kalmadı");
            }

             */
        }
        else if (id == R.id.nav_settings)
        {
          Intent intent=new Intent(Home.this,Settings.class);
          startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();
            Intent intent = new Intent(Home.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}