package com.tvip.surveylance;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationListener;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class mapping_outlet extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    BottomSheetBehavior sheetBehavior;
    View bottom_sheet;
    BottomSheetDialog sheetDialog;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_outlet);
        HttpsTrustManager.allowAllSSL();
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double langitude = Double.valueOf(MainActivity.latitude);
        Double longitude = Double.valueOf(MainActivity.longitude);

        // Add a marker in Sydney and move the camera
        LatLng zoom = new LatLng(langitude, longitude);

        mMap.addMarker(new MarkerOptions().position(zoom).title("Lokasi Anda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));
        Geocoder gc = new Geocoder(getApplicationContext());

            List<Address> list = null;
            try {
                list = gc.getFromLocation(langitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final Address address = list.get(0);

            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    showBottomSheetDialog(address.getAddressLine(0), Double.valueOf(MainActivity.latitude), Double.valueOf(MainActivity.longitude));
                }
            });
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoom, 20));

    }

    private void showBottomSheetDialog(String addressLine, final double latitude, final double longitude) {
        View view = getLayoutInflater().inflate(R.layout.mapping, null);


        TextView namatoko = view.findViewById(R.id.nama_toko);
        final TextView alamat = view.findViewById(R.id.alamat);
        Button setlokasi = view.findViewById(R.id.setlokasi);

        namatoko.setText(MainActivity.txt_nama.getText().toString());
        alamat.setText(addressLine);

        setlokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(mapping_outlet.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setCancelable(false);
                pDialog.setTitleText("Harap Menunggu");
                pDialog.show();
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_mapping",
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                pDialog.cancel();
                                SweetAlertDialog success = new SweetAlertDialog(mapping_outlet.this, SweetAlertDialog.SUCCESS_TYPE);
                                success.setContentText("Berhasil disimpan");
                                success.setCancelable(false);
                                success.setConfirmText("OK");
                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        Intent i = new Intent(getApplicationContext(), menu_survey.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                    }
                                });
                                success.show();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        String creds = String.format("%s:%s", "admin", "Databa53");
                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                        params.put("Authorization", auth);
                        return params;
                    }


                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                        String szId = sharedPreferences.getString("szId", null);

                        params.put("szId", szId);
                        params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                        params.put("longitude", MainActivity.longitude);
                        params.put("latitude", MainActivity.latitude);
                        params.put("alamat", alamat.getText().toString());

                        return params;
                    }

                };
                stringRequest2.setRetryPolicy(
                        new DefaultRetryPolicy(
                                500000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                );
                RequestQueue requestQueue2 = Volley.newRequestQueue(mapping_outlet.this);
                requestQueue2.add(stringRequest2);
            }
        });



        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }

}