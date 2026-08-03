package com.example.smartparkingfinder;

// IMPORTS

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity
        extends AppCompatActivity {

    WebView webView;

    DatabaseReference databaseReference;

    @SuppressLint({
            "SetJavaScriptEnabled",
            "JavascriptInterface"
    })

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // APPLY SAVED LANGUAGE

        LocaleHelper.setLocale(

                this,

                LocaleHelper.getSavedLanguage(this)
        );

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // SEARCH BAR

        EditText edtSearch =
                findViewById(R.id.edtSearch);

        // WEBVIEW

        webView =
                findViewById(R.id.webView);

        // WEB SETTINGS

        WebSettings webSettings =
                webView.getSettings();

        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowFileAccess(true);

        webSettings.setAllowContentAccess(true);

        webSettings.setDomStorageEnabled(true);

        webSettings.setLoadsImagesAutomatically(true);

        webSettings.setGeolocationEnabled(true);

        // HARDWARE ACCELERATION

        webView.setLayerType(
                WebView.LAYER_TYPE_HARDWARE,
                null
        );

        // GEOLOCATION SUPPORT

        webView.setWebChromeClient(

                new WebChromeClient() {

                    @Override
                    public void onGeolocationPermissionsShowPrompt(

                            String origin,

                            GeolocationPermissions.Callback callback) {

                        callback.invoke(
                                origin,
                                true,
                                false
                        );
                    }
                });

        // JS INTERFACE

        webView.addJavascriptInterface(
                this,
                "Android"
        );

        // WEBVIEW CLIENT

        webView.setWebViewClient(
                new WebViewClient() {

                    @Override
                    public void onPageFinished(
                            WebView view,
                            String url) {

                        super.onPageFinished(
                                view,
                                url
                        );

                        loadParkingData();
                    }
                });

        // LOAD MAP

        webView.loadUrl(
                "file:///android_asset/map.html"
        );

        // FIREBASE

        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("parkingSpots");

        // LOCATION PERMISSION

        checkLocationPermission();

        // START SMART VERIFICATION

        BookingVerificationService service =
                new BookingVerificationService(this);

        service.verifyBookings();

        // SEARCH PARKING

        edtSearch.addTextChangedListener(

                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {

                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        webView.evaluateJavascript(

                                "searchParking('"
                                        + s.toString()
                                        + "')",

                                null
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {

                    }
                });
    }

    // MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.main_menu,
                menu
        );

        return true;
    }

    // MENU CLICK

    @Override
    public boolean onOptionsItemSelected(
            MenuItem item) {

        // REFRESH

        if(item.getItemId()
                == R.id.menu_refresh) {

            webView.reload();

            return true;
        }

        // PARKING LIST

        if(item.getItemId()
                == R.id.menu_parking_list) {

            startActivity(

                    new Intent(
                            this,
                            ParkingListActivity.class
                    )
            );

            return true;
        }

        // MY BOOKINGS

        if(item.getItemId()
                == R.id.menu_bookings) {

            startActivity(

                    new Intent(
                            this,
                            BookingHistoryActivity.class
                    )
            );

            return true;
        }

        // ABOUT

        if(item.getItemId()
                == R.id.menu_about) {

            startActivity(

                    new Intent(
                            this,
                            AboutActivity.class
                    )
            );

            return true;
        }

        // LANGUAGE

        if(item.getItemId()
                == R.id.menu_language) {

            showLanguageDialog();

            return true;
        }

        // ADMIN LOGIN

        if(item.getItemId()
                == R.id.menu_admin) {

            startActivity(

                    new Intent(
                            this,
                            AdminLoginActivity.class
                    )
            );

            return true;
        }

        // EXIT

        if(item.getItemId()
                == R.id.menu_exit) {

            finishAffinity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // LOAD PARKING

    private void loadParkingData() {

        databaseReference.addValueEventListener(

                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {

                        try {

                            JSONArray jsonArray =
                                    new JSONArray();

                            for(DataSnapshot data :
                                    snapshot.getChildren()) {

                                ParkingModel model =
                                        data.getValue(
                                                ParkingModel.class
                                        );

                                if(model != null) {

                                    JSONObject object =
                                            new JSONObject();

                                    object.put(
                                            "id",
                                            model.getId()
                                    );

                                    object.put(
                                            "name",
                                            model.getName()
                                    );

                                    object.put(
                                            "lat",
                                            model.getLat()
                                    );

                                    object.put(
                                            "lon",
                                            model.getLon()
                                    );

                                    object.put(
                                            "available",
                                            model.getAvailable()
                                    );

                                    jsonArray.put(object);
                                }
                            }

                            webView.post(() ->

                                    webView.evaluateJavascript(

                                            "loadParkingData("
                                                    + jsonArray.toString()
                                                    + ")",

                                            null
                                    )
                            );
                        }

                        catch (Exception e) {

                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error) {

                    }
                });
    }

    // BOOK PARKING

    @JavascriptInterface
    public void bookParking(
            String parkingId) {

        String currentDevice =

                Build.MANUFACTURER
                        + " "
                        + Build.MODEL;

        // CHECK EXISTING BOOKINGS

        FirebaseDatabase
                .getInstance()
                .getReference("bookings")

                .get()

                .addOnSuccessListener(bookingSnapshot -> {

                    boolean alreadyBooked = false;

                    for(DataSnapshot data :
                            bookingSnapshot.getChildren()) {

                        BookingModel booking =
                                data.getValue(
                                        BookingModel.class
                                );

                        if(booking != null) {

                            if(booking.getParkingId() != null

                                    &&

                                    booking.getParkingId()
                                            .equals(parkingId)

                                    &&

                                    booking.getDeviceName()
                                            .equals(currentDevice)

                                    &&

                                    booking.getStatus()
                                            .equals("Booked")) {

                                alreadyBooked = true;

                                break;
                            }
                        }
                    }

                    // DUPLICATE BOOKING

                    if(alreadyBooked) {

                        runOnUiThread(() ->

                                webView.evaluateJavascript(

                                        "alert('You already booked this parking')",

                                        null
                                )
                        );

                        return;
                    }

                    // LOAD PARKING

                    databaseReference
                            .child(parkingId)
                            .get()

                            .addOnSuccessListener(snapshot -> {

                                ParkingModel model =
                                        snapshot.getValue(
                                                ParkingModel.class
                                        );

                                if(model != null
                                        && model.getAvailable() > 0) {

                                    // REAL USER LOCATION

                                    LocationHelper helper =
                                            new LocationHelper(this);

                                    helper.getCurrentLocation(

                                            (userLat, userLon) -> {

                                                // PARKING LOCATION

                                                double parkingLat =
                                                        model.getLat();

                                                double parkingLon =
                                                        model.getLon();

                                                // DISTANCE

                                                float[] results =
                                                        new float[1];

                                                Location.distanceBetween(

                                                        userLat,
                                                        userLon,

                                                        parkingLat,
                                                        parkingLon,

                                                        results
                                                );

                                                // KM

                                                double distanceKm =
                                                        results[0] / 1000;

                                                // AVG SPEED

                                                double averageSpeed = 30;

                                                // TRAVEL TIME

                                                long travelMinutes =

                                                        (long)((distanceKm
                                                                / averageSpeed)
                                                                * 60);

                                                // MINIMUM 5 MINS

                                                if(travelMinutes < 5) {

                                                    travelMinutes = 5;
                                                }

                                                // GRACE TIME

                                                long graceMinutes = 5;

                                                // EXPIRY TIME

                                                long expiryTime =

                                                        System.currentTimeMillis()

                                                                +

                                                                ((travelMinutes
                                                                        + graceMinutes)

                                                                        * 60 * 1000);

                                                // REDUCE SLOT

                                                int updatedSlots =
                                                        model.getAvailable() - 1;

                                                databaseReference
                                                        .child(parkingId)
                                                        .child("available")
                                                        .setValue(updatedSlots);

                                                // SAVE BOOKING

                                                DatabaseReference bookingRef =

                                                        FirebaseDatabase
                                                                .getInstance()
                                                                .getReference("bookings");

                                                String bookingId =
                                                        bookingRef.push().getKey();

                                                String time =

                                                        new SimpleDateFormat(
                                                                "dd MMM yyyy, hh:mm a",
                                                                Locale.getDefault()
                                                        )

                                                                .format(new Date());

                                                BookingModel booking =

                                                        new BookingModel(

                                                                bookingId,

                                                                parkingId,

                                                                model.getName(),

                                                                time,

                                                                currentDevice,

                                                                "Booked",

                                                                parkingLat,

                                                                parkingLon,

                                                                expiryTime,

                                                                false
                                                        );

                                                bookingRef
                                                        .child(bookingId)
                                                        .setValue(booking)

                                                        .addOnSuccessListener(unused -> {

                                                            runOnUiThread(() ->

                                                                    webView.evaluateJavascript(

                                                                            "alert('Parking Slot Booked')",

                                                                            null
                                                                    )
                                                            );
                                                        });
                                            });
                                }

                                else {

                                    runOnUiThread(() ->

                                            webView.evaluateJavascript(

                                                    "alert('Parking Full')",

                                                    null
                                            )
                                    );
                                }
                            });
                });
    }

    // NAVIGATION

    @JavascriptInterface
    public void navigateTo(
            double lat,
            double lon) {

        String uri =

                "google.navigation:q="
                        + lat + "," + lon;

        Intent intent =
                new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(uri)
                );

        intent.setPackage(
                "com.google.android.apps.maps"
        );

        startActivity(intent);
    }

    // LANGUAGE DIALOG

    private void showLanguageDialog() {

        String[] languages = {

                "🇺🇸 English",

                "🇮🇳 हिन्दी",

                "🇮🇳 मराठी",

                "🇮🇳 ગુજરાતી",

                "🇮🇳 தமிழ்"
        };

        String[] codes = {

                "en",

                "hi",

                "mr",

                "gu",

                "ta"
        };

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("🌐 Select Language");

        builder.setItems(languages,

                (dialog, which) -> {

                    LocaleHelper.setLocale(
                            this,
                            codes[which]
                    );

                    Intent intent =
                            getIntent();

                    finish();

                    startActivity(intent);
                });

        builder.show();
    }

    // LOCATION PERMISSION

    private void checkLocationPermission() {

        if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    100
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,

            @NonNull String[] permissions,

            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );
    }
}