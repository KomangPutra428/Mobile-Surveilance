package com.tvip.surveylance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.gu.toolargetool.TooLargeTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class tambah_memento extends AppCompatActivity implements OnMapReadyCallback {

    EditText editalamat, editlonglat;
    AutoCompleteTextView edittemuan, editdriver, editfisikvsso, editrutin, editpenyampaian, editpelayanan, editketerangantemuan;
    ArrayList<String> driver_list = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    ArrayList<String> subcategory = new ArrayList<>();
    Button batal, simpan;
    SharedPreferences sharedPreferences;
    ContentValues cv;
    Uri imageUri;
    ImageButton refresh;
    MaterialButton location;

    EditText editinfo;
    SignaturePad signature_pad;

    String[] decisions = {
            "YA",
            "TIDAK"
    };
    RelativeLayout display, display2, display3;
    ImageView uploaddisplay, uploaddisplay2, uploaddisplay3;
    TextView textkamera, textkamera2, textkamera3;
    Bitmap bitmap, bitmap2, bitmap3;
    SweetAlertDialog pDialog;
    String currentDateandTime;

    ImageButton hapus2, hapus3;
    TextView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(SavedInstanceFragment.getInstance(getFragmentManager()).popData());
        setContentView(R.layout.activity_tambah_memento);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        currentDateandTime = sdf.format(new Date());

        HttpsTrustManager.allowAllSSL();
        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);
        editinfo = findViewById(R.id.editinfo);
        refresh = findViewById(R.id.refresh);

        display = findViewById(R.id.display);
        uploaddisplay = findViewById(R.id.uploaddisplay);
        textkamera = findViewById(R.id.textkamera);

        add = findViewById(R.id.add);
        hapus2 = findViewById(R.id.hapus2);
        hapus3 = findViewById(R.id.hapus3);

        display2 = findViewById(R.id.display2);
        uploaddisplay2 = findViewById(R.id.uploaddisplay2);
        textkamera2 = findViewById(R.id.textkamera2);

        display3 = findViewById(R.id.display3);
        uploaddisplay3 = findViewById(R.id.uploaddisplay3);
        textkamera3 = findViewById(R.id.textkamera3);

        editalamat = findViewById(R.id.editalamat);
        editlonglat = findViewById(R.id.editlonglat);
        editdriver = findViewById(R.id.editdriver);

        location = findViewById(R.id.location);


        editfisikvsso = findViewById(R.id.editfisikvsso);
        editrutin = findViewById(R.id.editrutin);
        editpenyampaian = findViewById(R.id.editpenyampaian);
        editpelayanan = findViewById(R.id.editpelayanan);
        editketerangantemuan = findViewById(R.id.editketerangantemuan);
        edittemuan = findViewById(R.id.edittemuan);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, decisions);

        editfisikvsso.setAdapter(adapter);
        editrutin.setAdapter(adapter);
        editpenyampaian.setAdapter(adapter);
        editpelayanan.setAdapter(adapter);

        refresh.bringToFront();
        add.bringToFront();
        hapus2.bringToFront();
        hapus3.bringToFront();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(display2.getVisibility() == View.VISIBLE){
                    display3.setVisibility(View.VISIBLE);
                }
                display2.setVisibility(View.VISIBLE);

            }
        });

        hapus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display2.setVisibility(View.GONE);
                textkamera2.setVisibility(View.VISIBLE);

                uploaddisplay2.setImageResource(R.drawable.icon_kamera);

            }
        });

        hapus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display3.setVisibility(View.GONE);
                textkamera3.setVisibility(View.VISIBLE);

                uploaddisplay3.setImageResource(R.drawable.icon_kamera);


            }
        });

        signature_pad = findViewById(R.id.signature_pad);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature_pad.clear();
            }
        });

        batal.setOnClickListener(v -> finish());

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://maps.google.com?q="+MainActivity.latitude+","+MainActivity.longitude));
                startActivity(i);
            }
        });

        simpan.setOnClickListener(v -> {
            if(editdriver.getText().toString().length() == 0){
                editdriver.setError("Isi Nama Driver");
            } else if(edittemuan.getText().toString().length() == 0){
                edittemuan.setError("Isi Pilihan");
            } else if(editketerangantemuan.getText().toString().length() == 0){
                editketerangantemuan.setError("Isi Pilihan");
            } else if (editinfo.getText().toString().length() == 0){
                editinfo.setText("Isi Info Pelanggan");
            } else if (textkamera.getVisibility() == View.VISIBLE){
                new SweetAlertDialog(tambah_memento.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Upload Outlet")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else if (signature_pad.getSignatureBitmap() == null){
                new SweetAlertDialog(tambah_memento.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Isi Tanda Tangan")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            } else {
                Toast.makeText(this, String.valueOf(signature_pad.getSignatureBitmap()), Toast.LENGTH_SHORT).show();
                postmemento();
            }
        });


        display.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                cv = new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE, "My Picture");
                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
            }
        });

        display2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                cv = new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE, "My Picture");
                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 2);

            }
        });

        display3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                cv = new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE, "My Picture");
                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 3);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        StringRequest driver = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/karyawan/index",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String nama = jsonObject1.getString("nama_karyawan_struktur");
                                    String jabatan = jsonObject1.getString("jabatan_karyawan");
                                    if(jabatan.equals("DRIVER")){
                                        driver_list.add(nama);
                                    }
                                }
                            }
                            editdriver.setAdapter(new ArrayAdapter<String>(tambah_memento.this, android.R.layout.simple_expandable_list_item_1, driver_list));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        driver.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue driverqueue = Volley.newRequestQueue(tambah_memento.this);
        driverqueue.add(driver);

        StringRequest kategori_pertama = new StringRequest(Request.Method.GET, "http://192.168.4.168/api_memento/Kunjungan/index_Category",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String kategori = jsonObject1.getString("category");
                                    category.add(kategori);
                                }
                            }
                            edittemuan.setAdapter(new ArrayAdapter<String>(tambah_memento.this, android.R.layout.simple_expandable_list_item_1, category));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        kategori_pertama.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue kategori = Volley.newRequestQueue(tambah_memento.this);
        kategori.add(kategori_pertama);

        edittemuan.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                subcategory.clear();
                editketerangantemuan.setText("");

                StringRequest kategori_pertama = new StringRequest(Request.Method.GET, "http://192.168.4.168/api_memento/Kunjungan/index_Sub_Category?kode=" + edittemuan.getText().toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String kategori = jsonObject1.getString("sub_category");
                                            subcategory.add(kategori);
                                        }
                                    }
                                    editketerangantemuan.setAdapter(new ArrayAdapter<String>(tambah_memento.this, android.R.layout.simple_expandable_list_item_1, subcategory));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        String creds = String.format("%s:%s", "admin", "Databa53");
                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                        params.put("Authorization", auth);
                        return params;
                    }
                };
                kategori_pertama.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue kategori = Volley.newRequestQueue(tambah_memento.this);
                kategori.add(kategori_pertama);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

    }

    private void postmemento() {
        pDialog = new SweetAlertDialog(tambah_memento.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://192.168.4.168/api_memento/Kunjungan/index_kunjungan",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Hasil Response = " + response);
                        uploadoutlet();
                        uploadttd();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Hasil Error = " + error.getMessage());
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

                params.put("nik_sf", sharedPreferences.getString("nik_karyawan", null));
                params.put("nama_sf", sharedPreferences.getString("szName", null));
                params.put("kode_pelanggan", sharedPreferences.getString("szId", null));

                params.put("verifikasi_alamat", editalamat.getText().toString());
                params.put("nama_survey", sharedPreferences.getString("nama_karyawan", null));
                params.put("nama_driver", editdriver.getText().toString());

//                params.put("qty_fisik_vs_do_vs_so", editfisikvsso.getText().toString());
//                params.put("rutin_kunjungan_sales", editrutin.getText().toString());
//                params.put("penyampaian_program", editpenyampaian.getText().toString());
//                params.put("pelayanan_attitude_dan_keluhan", editpelayanan.getText().toString());

                params.put("info_pelanggan", editinfo.getText().toString());
                params.put("kategori_temuan", edittemuan.getText().toString());
                params.put("keterangan_temuan", editketerangantemuan.getText().toString());

                params.put("longitude", MainActivity.latitude);
                params.put("latitude", MainActivity.longitude);
                params.put("display_outlet", "outlet_" + currentDateandTime + "_" + sharedPreferences.getString("szId", null) + "_1.jpeg");
                params.put("tanda_tangan", "tandatangan_" + currentDateandTime + "_" + sharedPreferences.getString("szId", null) + ".jpeg");



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(tambah_memento.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadttd() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://192.168.4.168/upload_gambar/upload_tanda_tangan.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {

                            } else if (status.contains("Image not uploaded")){

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
                String gambar = imagetoString(signature_pad.getSignatureBitmap());

                params.put("nama_foto", "tandatangan_" + currentDateandTime + "_" + sharedPreferences.getString("szId", null));
                params.put("foto", gambar);


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

        RequestQueue requestQueue2 = Volley.newRequestQueue(tambah_memento.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadoutlet() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://192.168.4.168/upload_gambar/upload_outlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        uploadoutlet2();
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
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
                String gambar = imagetoString(bitmap);


                params.put("nama_foto", "outlet_" + currentDateandTime + "_" + sharedPreferences.getString("szId", null)+"_1");
                params.put("foto", gambar);


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

        RequestQueue requestQueue2 = Volley.newRequestQueue(tambah_memento.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadoutlet2() {
        if(textkamera2.getVisibility() == View.VISIBLE || display2.getVisibility() == View.INVISIBLE){
            uploadoutlet3();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://192.168.4.168/upload_gambar/upload_outlet.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            uploadoutlet3();
                            try {
                                JSONObject json = new JSONObject(response);
                                String status = json.getString("response");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
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
                    String gambar = imagetoString(bitmap2);


                    params.put("nama_foto", "outlet_" + currentDateandTime + "_" + sharedPreferences.getString("szId", null)+"_2");
                    params.put("foto", gambar);


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

            RequestQueue requestQueue2 = Volley.newRequestQueue(tambah_memento.this);
            requestQueue2.add(stringRequest2);
        }

    }

    private void uploadoutlet3() {
        if(textkamera3.getVisibility() == View.VISIBLE || display3.getVisibility() == View.INVISIBLE){
            pDialog.dismissWithAnimation();
            new SweetAlertDialog(tambah_memento.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Data Sudah Diupload")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://192.168.4.168/upload_gambar/upload_outlet.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(tambah_memento.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Data Sudah Diupload")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            finish();
                                        }
                                    })
                                    .show();
                        }
                    },
                    new Response.ErrorListener() {
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
                    String gambar = imagetoString(bitmap3);


                    params.put("nama_foto", "outlet_" + currentDateandTime + "_" + sharedPreferences.getString("szId", null)+"_3");
                    params.put("foto", gambar);


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

            RequestQueue requestQueue2 = Volley.newRequestQueue(tambah_memento.this);
            requestQueue2.add(stringRequest2);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Double langitude = Double.valueOf(MainActivity.latitude);
        Double longitude = Double.valueOf(MainActivity.longitude);

        // Add a marker in Sydney and move the camera
        LatLng zoom = new LatLng(langitude, longitude);

        googleMap.addMarker(new MarkerOptions().position(zoom).title("Lokasi Anda"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));
        Geocoder gc = new Geocoder(getApplicationContext());

            List<Address> list = null;
            try {
                list = gc.getFromLocation(langitude, longitude,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final Address address = list.get(0);
            editalamat.setText(address.getAddressLine(0));
            editlonglat.setText(MainActivity.latitude + ", " + MainActivity.longitude);

//            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                @Override
//                public void onInfoWindowClick(Marker marker) {
//                    showBottomSheetDialog();
//                }
//            });
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoom, 10));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    uploaddisplay.setImageBitmap(bitmap);
                    textkamera.setVisibility(View.GONE);

                    ViewGroup.LayoutParams paramktp = uploaddisplay.getLayoutParams();

                    double sizeInDP = 226;
                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                                    .getDisplayMetrics());

                    double sizeInDP2 = 226;
                    int marginInDp2 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                                    .getDisplayMetrics());

                    paramktp.width = marginInDp;
                    paramktp.height = marginInDp2;
                    uploaddisplay.setLayoutParams(paramktp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    uploaddisplay2.setImageBitmap(bitmap2);
                    textkamera2.setVisibility(View.GONE);
                    ViewGroup.LayoutParams paramktp = uploaddisplay2.getLayoutParams();

                    double sizeInDP = 226;
                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                                    .getDisplayMetrics());

                    double sizeInDP2 = 226;
                    int marginInDp2 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                                    .getDisplayMetrics());

                    paramktp.width = marginInDp;
                    paramktp.height = marginInDp2;
                    uploaddisplay2.setLayoutParams(paramktp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 3){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bitmap3 = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    uploaddisplay3.setImageBitmap(bitmap3);
                    textkamera3.setVisibility(View.GONE);

                    ViewGroup.LayoutParams paramktp = uploaddisplay3.getLayoutParams();

                    double sizeInDP = 226;
                    int marginInDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                                    .getDisplayMetrics());

                    double sizeInDP2 = 226;
                    int marginInDp2 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                                    .getDisplayMetrics());

                    paramktp.width = marginInDp;
                    paramktp.height = marginInDp2;
                    uploaddisplay3.setLayoutParams(paramktp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            super.onSaveInstanceState(outState);

            SavedInstanceFragment.getInstance(getFragmentManager()).pushData((Bundle) outState.clone());
            outState.clear();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(SavedInstanceFragment.getInstance(getFragmentManager()).popData());
        }
    }

}