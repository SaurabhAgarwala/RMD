package io.codefundo.rmd.dmapp;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getName();
    static final int PICK_CONTACT = 1;

    ConstraintLayout root;

    private SharedPreferences sharedPreferences;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Write External Storage Permission is granted");
            } else {
                Log.v(TAG, "Write External Storage Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Access Coarse Location Permission is granted");
            } else {
                Log.v(TAG, "Access Coarse Location Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Access Fine Location Permission is granted");
            } else {
                Log.v(TAG, "Access Fine Location Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Send SMS Permission is granted");
            } else {
                Log.v(TAG, "Send SMS Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "READ_PHONE_STATE Permission is granted");
            } else {
                Log.v(TAG, "READ_PHONE_STATE Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }
        }

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.main_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Log.v(TAG, menuItem.toString());
                Fragment fragment = null;
                String fragmentName = null;
                switch (menuItem.getItemId()) {
                    case R.id.map:
                        fragmentName = "Map";
                        fragment = new MapFragment();
                        break;
                    case R.id.help_manual:
                        fragmentName = "Help Manual";
                        fragment = new HelpManualFragment();
                        break;
                    case R.id.casualty:
                        fragmentName = "Casualty";
                        fragment = new CasualtyFragment();
                        break;
                    case R.id.helpline:
                        fragmentName = "Helpline";
                        fragment = new EmergencyContactFragment();
                        break;
                    case R.id.about:
                        fragmentName = "About";
                        fragment = new AboutFragment();
                        break;
                }
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_ll_container, fragment, fragmentName)
                            .commit();
                }
                return true;
            }
        });

        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                        Log.v(TAG, "Send SMS Permission is granted");
                        sendSOS();
                    }
                } else {
                    sendSOS();
                }
            }
        });
        sharedPreferences =
                getApplicationContext().getSharedPreferences("in.codefundo.io.rmd",
                        MODE_PRIVATE);
        if (sharedPreferences.getString("sosNumber", null) == null) {
            Uri uri = Uri.parse("content://contacts");
            Toast.makeText(getBaseContext(), "Pick SOS Contact", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, PICK_CONTACT);
        }
        bottomNavigationView.setSelectedItemId(R.id.map);
    }

    public void sendSOS() {
        String sosNumber = sharedPreferences.getString("sosNumber", null);
        if (sosNumber != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        && getApplicationContext().checkSelfPermission(Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager =
                            (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    String longitude = "Longitude: " + loc.getLongitude();
                    Log.v(TAG, longitude);
                    String latitude = "Latitude: " + loc.getLatitude();
                    Log.v(TAG, latitude);

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sosNumber,
                            null,
                            "Help! My position is " + latitude + " , " + longitude,
                            null,
                            null);
                    Snackbar.make(root, "Message Sent!", Snackbar.LENGTH_SHORT).show();
                } else {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sosNumber,
                            null,
                            "Help!",
                            null,
                            null);
                    Snackbar.make(root, "Message Sent!", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                LocationManager locationManager =
                        (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String longitude = "Longitude: " + loc.getLongitude();
                Log.v(TAG, longitude);
                String latitude = "Latitude: " + loc.getLatitude();
                Log.v(TAG, latitude);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(sosNumber,
                        null,
                        "Help! My position is " + latitude + " , " + longitude,
                        null,
                        null);
                Snackbar.make(root, "Message Sent!", Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);

                Log.d(TAG, "ZZZ number : " + number + " , name : " + name);
                sharedPreferences.edit().putString("sosNumber", number).apply();
                cursor.close();

            }
        }
    }

}
