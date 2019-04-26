package com.example.adminremove;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    private Button SearchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String SearchInput;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);


        inputText = findViewById(R.id.search_product_name);
        SearchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(context: SearchProductsActivity.this));

        SearchBtn.setOnClickListener(new View.OnClickListener()) {
            @Override
            public void onClick(View view)
             {
                SearchInput = inputText.getText().toString();

                onStart();
             }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");


        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("pname").startAt(SearchInput) , Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductsViewHolder> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Price = " + model.getPrice() + "$");
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public <SearchProductsActivity> void onClick(View view)
                            {
                                Intent intent = new Intent(packageContext: SearchProductsActivity.this, ProductsDetailActivity.this);
                                intent.putExtra(name: "pid", model.getPid());
                                startActivity(intent);
                            }
                        }
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
                    {
                        View view = layoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, attachToRoot false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                }
    }
}
