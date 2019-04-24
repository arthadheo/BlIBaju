package com.example.admin_new_order;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdminUserProductsActivity extends AppCompatActivity
{
    private RecyclerView productsList;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference cartListRef;

    private String UserID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        UserID = getIntent().getStringExtra(name: "uid");

        productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context: this);
        productsList.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List").child("Admin View ").child(UserID).child("Products");
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
           @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
           {
               holder.txtProductsQuantity.setText("Quantity = " + model.getQuantity());
               holder.txtProductsPrice.setText("Price " + model.getPrice() + "$");
               holder.txtProductName.setText(model.getPname());
           }

           @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
           {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, attachToRoot false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
           }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }

}
