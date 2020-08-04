package com.example.nile2;

import android.app.ProgressDialog;
import android.content.Intent;
//import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignInUIOptions;
import com.amazonaws.mobile.client.UserStateDetails;



import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
//    private Button CreateAccountButton;
//    private EditText InputName, InputPhoneNumber, InputPassword;
//    private ProgressDialog loadingBar;

    private final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, userStateDetails.getUserState().toString());
                switch (userStateDetails.getUserState()) {
                    case SIGNED_IN:
                        Intent i = new Intent(RegisterActivity.this, HomeActivity.class);


                        startActivity(i);
                        break;
                    case SIGNED_OUT:
                        showSignIn();
                    default:
                        AWSMobileClient.getInstance().signOut();
                        showSignIn();
                        break;
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());

            }
        });
    }


    private void showSignIn(){
        try {
            AWSMobileClient.getInstance().showSignIn(this,
                    SignInUIOptions.builder().nextActivity(MainActivity.class).build());
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}

//End of AWS



//
//        CreateAccountButton = (Button) findViewById(R.id.register_btn);
//        InputName = (EditText) findViewById(R.id.register_username_input);
//        InputPassword = (EditText) findViewById(R.id.register_password_input);
//        InputPhoneNumber = (EditText) findViewById(R.id.register_phone_number_input);
//        loadingBar = new ProgressDialog(this);
//
//
//        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                CreateAccount();
//            }
//        });
//    }
//
//
//
//    private void CreateAccount()
//    {
//        String name = InputName.getText().toString();
//        String phone = InputPhoneNumber.getText().toString();
//        String password = InputPassword.getText().toString();
//
//        if (TextUtils.isEmpty(name))
//        {
//            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(phone))
//        {
//            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
//        }
//        else if (TextUtils.isEmpty(password))
//        {
//            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            loadingBar.setTitle("Create Account");
//            loadingBar.setMessage("Please wait, while we are checking the credentials.");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();
//
//            ValidatephoneNumber(name, phone, password);
//        }
//    }
//
//
//
//    private void ValidatephoneNumber(final String name, final String phone, final String password)
//    {
//
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//                if (!(dataSnapshot.child("Users").child(phone).exists()))
//                {
////                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
////                            .addOnCompleteListener(new OnCompleteListener<Void>() {
////                                @Override
////                                public void onComplete(Task<Void> task)
////                                {
////                                    if (task.isSuccessful())
////                                    {
////                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
////                                        loadingBar.dismiss();
////
////                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
////                                        startActivity(intent);
////                                    }
////                                    else
////                                    {
////                                        loadingBar.dismiss();
////                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
////                                    }
////                                }
////                            });
//                }
//                else
//                {
//                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//}
