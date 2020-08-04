package com.example.nile2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.nile2.Model.Products;
import com.example.nile2.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageView;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//import android.icu.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private Button addToCartButton;//updated on vid 22
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");//newly added from vid 21


        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);//added on 22
        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);

        getProductDetails(productID); //newly added on 22

        addToCartButton.setOnClickListener(new View.OnClickListener() { // new 22 all the way to
            @Override
            public void onClick(View v) {
                addingToCartList();
            }

            private void addingToCartList() //when and at what time did the user add to cart
            {
                String saveCurrentTime, saveCurrentDate;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentDate.format(calForDate.getTime());

                final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");// data stored in firebase

                final HashMap<String, Object> cartMap = new HashMap<>();
                cartMap.put("pid", productID);
                cartMap.put("pname", productName.getText().toString());
                cartMap.put("price", productPrice.getText().toString());
                cartMap.put("date", saveCurrentDate);
                cartMap.put("time", saveCurrentTime);
                cartMap.put("quantity", numberButton.getNumber());
                cartMap.put("discount", "");

                cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products").child(productID)
                        .updateChildren(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products").child(productID)
                                            .updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ProductDetailsActivity.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                }


                            }
                        });
            }
        }); //stop

    }

    private void getProductDetails(String productID) //newly added method
    {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);//by using picaso library display the image of product using image view
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
