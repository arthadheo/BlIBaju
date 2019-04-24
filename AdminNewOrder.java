package com.example.admin_new_order;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin_new_order.Model.AdminOrders;

public class AdminNewOrdersActivity extends AppCompatActivity
{
    private RecyclerView orderlist;
    private DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderlist =  findViewById(R.id.orders_list);
        orderlist.setLayoutManager(new LinearLayoutManager( context: this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderRef, AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminNewOrdersActivity holder, int position, @NonNull AdminOrders model)
                    {
                        holder.userName.setText("Name:" + model.getName());
                        holder.userPhoneNumber.setText("Phone:" + model.getPhone());
                        holder.userTotalPrice.setText("Total Amount:" + model.getTotalAmount());
                        holder.userDateTime.setText("Orders at:" + model.getDate() + "  " + model.getTime());
                        holder.userShippingAddress.setText("Shipping Address:" + model.getAddress() + ", " + model.getCity());

                        holder.ShowOrderBtn.setOnClickListener(new View.OnClickListener( new View.OnClickListener())){
                                String uID = getRef(position).getKey();


                                Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                                intent.putExtra(name: "uid", uID);
                                startActivity(intent);

                        };

                        holder.itemView.setonClickListener(new View.OnClickListener());
                        @Override
                        public void onClick(View view)
                        {
                            CharSequence options[] = new CharSequence[]
                                    {
                                            "YES",
                                            "NO"
                                    };
                            AlertDialog.Builder builder = new AlertDialog.Builder(context: AdminNewOrdersActivity);
                            builder.setTitle("Have you shipped this order products ?");

                            builder.setItems(options, new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    if (i == 0)
                                    {
                                        String uID = getRef(position).getKey();

                                        RemoveOrder(uID);
                                    }
                                    else
                                    {
                                        finish();
                                    }
                                }
                            });
                            builder.show();
                        }

                    });
                }


                    @NonNull
                    @Override
                    public AdminNewOrdersActivity onCreateViewHolder(@NonNull ViewGroup parent, int view)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, attachToRoot false)
                        return new AdminOrdersViewHolder(view);
                    }
                };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminOrdersViewHolder(View itemView)
    {
        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button ShowOrdersBtn;

        public AdminOrdersViewHolder(View itemView)
        {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userShippingAddress = itemView.findViewById(R.id.order_address_city);
        ShowOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);

        }
    }


    private void RemoverOrder(String uID)
        {
            orderRef.child(uID).removeValue()
        }
}
