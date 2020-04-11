package com.example.contactlessshopping.Shops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.Login_customer;
import com.example.contactlessshopping.Customers.Supermarket.Supermarket_MainActivity;
import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Supermarket.SupermarketMainShop;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShopRegistration extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final String TAG = "ShopRegistration";
    public static final String KEY_SHOPNAME = "shop_name";
    public static final String KEY_PHONE = "phone_number";
    public static final String KEY_EMAIL = "email_id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CAPACITY = "capacity";
    public static final String KEY_SHOP_CATEGORY = "shop_category";
    public static final String KEY_FROM_TIME = "from_time";
    public static final String KEY_TO_TIME = "to_time";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_ADDRESS = "address";

    private EditText editTextShopName, editTextFromTime, editTextToTime, editTextCapacity;
    private RadioGroup radioTypeGroup;
    private RadioButton radioButton;
    private RadioButton radioButtonYes, radioButtonNo;
    private ProgressDialog progressDialog;
    MaterialSpinner categorySpinner;
    private Button timeFrom, timeTo, buttonShopRegister, buttonAddSlot;
    public String insertSlot, authid;
    private String LONGITUDE, LATITUDE, shop_name, phone_number, email_id, password, capacity, from_time, to_time, address = "NULL", shop_category;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    String LAT, LON;
    double dlat, dlon;

    FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();

    Map<String, Object> slots = new HashMap<>();
    List<String> empty_array = Collections.<String>emptyList();
    String IntendedauthID, Intendedphonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_registration);

        editTextShopName = (EditText) findViewById(R.id.idShopName);
        editTextFromTime = (EditText) findViewById(R.id.idEtFromTime);
        editTextToTime = (EditText) findViewById(R.id.idEtToTime);
        editTextCapacity = (EditText) findViewById(R.id.idEtCapacity);
        progressDialog = new ProgressDialog(this);


        radioTypeGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioButtonYes = (RadioButton) findViewById(R.id.radioButtonYes);
        radioButtonNo = (RadioButton) findViewById(R.id.radioButtonNo);

        timeFrom = (Button) findViewById(R.id.idBtnFromTime);
        timeTo = (Button) findViewById(R.id.idBtnToTime);
        // buttonAddSlot=findViewById(R.id.add_slot_bt);

        buttonShopRegister = (Button) findViewById(R.id.ShopRegister);
        auth = FirebaseAuth.getInstance();

        final Intent intent = getIntent();
        IntendedauthID = intent.getStringExtra("intendAuthUID");
        Intendedphonenumber = intent.getStringExtra("intentPhoneNumber");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ShopRegistration.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //editTextFromTime.setText(selectedHour + ":" + selectedMinute);
                        editTextFromTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ShopRegistration.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        editTextToTime.setText(selectedHour + ":" + selectedMinute);
                        editTextToTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ShopRegistration.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        }

        radioButtonYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioButtonNo.setChecked(true);
                    Toast.makeText(ShopRegistration.this, "Sorry, this function is under work. Try again later", Toast.LENGTH_SHORT).show();
                    radioButtonYes.setChecked(false);
                }
            }
        });
        radioButtonNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioButtonYes.setChecked(false);
                    if (ContextCompat.checkSelfPermission(
                            getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                ShopRegistration.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION_PERMISSION
                        );
                    } else {
                        //getCurrentLocation();
                    }
                }
            }
        });

        categorySpinner = (MaterialSpinner) findViewById(R.id.spinnerCategory);
        categorySpinner.setItems("Grocery Store",
                "Medical Store",
                "Supermarket",
                "Market",
                "Government / Ration Shops",
                "Butcher shop (meat)",
                "Fishmonger (fish)",
                "Greengrocer (fruits & vegetables)",
                "Petshop");
        categorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                shop_category = item;
            }
        });


        buttonShopRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);
                String dateInString = "2011-11-30";  // Start date
                SimpleDateFormat sdf_incr_date = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c1 = Calendar.getInstance();
                try {
                    c1.setTime(sdf_incr_date.parse(formattedDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c1.add(Calendar.DATE, 1);
                sdf_incr_date = new SimpleDateFormat("dd/MM/yyyy");
                Date resultdate = new Date(c1.getTimeInMillis());
                dateInString = sdf_incr_date.format(resultdate);
                Toast.makeText(ShopRegistration.this, formattedDate + "\n" + dateInString, Toast.LENGTH_SHORT).show();
                String date1 = formattedDate;
                String time1 = editTextFromTime.getText().toString();
                String date2 = dateInString;
                String time2 = editTextToTime.getText().toString();
                String format = "dd/MM/yyyy HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                try {
                    Date dateObj1 = sdf.parse(date1 + " " + time1);
                    Date dateObj2 = sdf.parse(date1 + " " + time2);
                    Log.d("TAG", "Date Start: " + dateObj1);
                    Log.d("TAG", "Date End: " + dateObj2);
                    long dif = dateObj1.getTime();
                    while (dif < dateObj2.getTime()) {
                        Date slot1 = new Date(dif);
                        dif += 3600000;
                        Date slot2 = new Date(dif);

                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                        Log.d("TAG", sdf1.format(slot1) + " - " + sdf2.format(slot2));
                        insertSlot = insertSlot + "," + sdf1.format(slot1) + " - " + sdf2.format(slot2);
                        slots.put(sdf1.format(slot1) + " - " + sdf2.format(slot2), empty_array);


                    }
                    slots.put("date", date1);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                shop_name = editTextShopName.getText().toString();

                capacity = editTextCapacity.getText().toString();
                from_time = editTextFromTime.getText().toString();
                to_time = editTextToTime.getText().toString();

                if (TextUtils.isEmpty(shop_name)) {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(capacity)) {
                    Toast.makeText(getApplicationContext(), "Enter last name!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressDialog.setMessage("Registering ....");
                progressDialog.show();

                Map<String, Object> note = new HashMap<>();
                note.put(KEY_SHOPNAME, shop_name);
                note.put(KEY_PHONE, Intendedphonenumber);
                note.put(KEY_CAPACITY, capacity);
                note.put(KEY_FROM_TIME, from_time);
                note.put(KEY_TO_TIME, to_time);
                note.put(KEY_LONGITUDE, LONGITUDE);
                note.put(KEY_LATITUDE, LATITUDE);
                note.put(KEY_ADDRESS, address);
                note.put(KEY_SHOP_CATEGORY, shop_category);
                progressDialog.setMessage("Registering....");
                progressDialog.show();


                Toast.makeText(ShopRegistration.this, auth.getUid() + " in main", Toast.LENGTH_SHORT).show();
                db.collection("shops").document(IntendedauthID).set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ShopRegistration.this, "Data saved", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(PatientRegister.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });


                if (shop_category == "Supermarket") {
                    slots.put("shop_id", auth.getUid());
                    Toast.makeText(ShopRegistration.this, auth.getUid() + " in cat", Toast.LENGTH_SHORT).show();
                    db.collection("tokens").document(IntendedauthID).set(slots)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(PatientRegister.this, "Data saved", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ShopRegistration.this, SupermarketMainShop.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(PatientRegister.this, "Error!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });

                }

                if (shop_category == "Grocery Store") {
                    slots.put("shop_id", auth.getUid());
                    Toast.makeText(ShopRegistration.this, auth.getUid() + " in cat", Toast.LENGTH_SHORT).show();
                    db.collection("order_slot").document(IntendedauthID).set(slots)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Toast.makeText(PatientRegister.this, "Data saved", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ShopRegistration.this, ShopMainActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Toast.makeText(PatientRegister.this, "Error!", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, e.toString());
                                }
                            });

                }
            }

        });

    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    dlat = location.getLatitude();
                                    LAT = String.valueOf(dlat);
                                    dlon = location.getLongitude();
                                    LON = String.valueOf(dlon);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            dlat = mLastLocation.getLatitude();
            LAT = String.valueOf(dlat);
            dlon = mLastLocation.getLongitude();
            LON = String.valueOf(dlon);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
//            getCurrentLocation();
//        } else {
//            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void getCurrentLocation() {
//
//        final LocationRequest locationRequest = new LocationRequest();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(3000);
//        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
//
//        LocationServices.getFusedLocationProviderClient(ShopRegistration.this)
//                .requestLocationUpdates(locationRequest, new LocationCallback() {
//
//                    @Override
//                    public void onLocationResult(LocationResult locationResult) {
//                        super.onLocationResult(locationResult);
//                        LocationServices.getFusedLocationProviderClient(ShopRegistration.this)
//                                .removeLocationUpdates(this);
//                        if (locationResult != null && locationResult.getLocations().size() > 0) {
//                            int lastestLocationIndex = locationResult.getLocations().size() - 1;
//                            double latitude =
//                                    locationResult.getLocations().get(lastestLocationIndex).getLatitude();
//                            double longitude =
//                                    locationResult.getLocations().get(lastestLocationIndex).getLongitude();
//
//                            LATITUDE = String.format(String.valueOf(latitude));
//                            LONGITUDE = String.format(String.valueOf(longitude));
//
//                        }
//                    }
//                }, Looper.getMainLooper());
//
//    }

}

