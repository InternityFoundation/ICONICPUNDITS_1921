package com.example.contactlessshopping.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.ShopRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Customer_registration extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText emailid, password, name, custno;
    String sname, semail, spass, scustno;
    Button submit;
    String IntendedauthID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

//        emailid=(EditText)findViewById(R.id.email);
//        password=(EditText)findViewById(R.id.pass);
        name = (EditText) findViewById(R.id.name);
        custno = (EditText) findViewById(R.id.custno);
        submit = (Button) findViewById(R.id.submitbt);


// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        final Intent intent = getIntent();
        IntendedauthID = intent.getStringExtra("intendAuthUID");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sname = name.getText().toString();
                scustno = custno.getText().toString();


                Map<String, Object> note = new HashMap<>();
                note.put("Name", sname);
                note.put("emailid", semail);
                note.put("Customer_no", scustno);

                db.collection("customers").document(IntendedauthID).set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Customer_registration.this, "Data saved", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(PatientRegister.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d("log", e.toString());
                            }
                        });


                // Sign in success, update UI with the signed-in user's information
                Log.d("Successfull:", "createUserWithEmail:success");
                FirebaseUser user = mAuth.getCurrentUser();
                Intent intent2 = new Intent(Customer_registration.this, Customer_MainActivity.class);
                startActivity(intent2);


            }
        });


    }

}
