package com.tvip.surveylance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detail_memento extends AppCompatActivity implements OnMapReadyCallback {
    TextView tanggal, waktu, salesforce, depo, kode, pelanggan, driver;
    TextView kesesuaian, rutinitas, penyampaian, keluhan;
    Chip keterangan_temuan;
    private GoogleMap mMap;
    TextView keterangantemuan, infopelanggan, alamatdetail;
    SharedPreferences sharedPreferences;
    MaterialButton location;
    ImageView fotooutlet, fotooutlet2, fotooutlet3, fotottd;
    Button hapus, edit;
    SweetAlertDialog pDialog;

    ArrayList<String> driver_list = new ArrayList<>();
    ArrayList<String> category = new ArrayList<>();
    ArrayList<String> subcategory = new ArrayList<>();

    MaterialCardView cardOutlet, cardOutlet2, cardOutlet3;

    Bitmap bitmap;
    
    // dialog //
    ImageView uploaddisplay;

    AutoCompleteTextView editdriver;
    AutoCompleteTextView edittemuan;
    AutoCompleteTextView editketerangantemuan;
    EditText editinfo;
    EditText editlonglat;
    EditText editalamat;
    RelativeLayout display;
    TextView teksdisplay1, teksdisplay2, teksdisplay3;
    // ** ** //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_memento);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        hapus = findViewById(R.id.hapus);
        edit = findViewById(R.id.edit);

        fotooutlet = findViewById(R.id.fotooutlet);
        fotooutlet2 = findViewById(R.id.fotooutlet2);
        fotooutlet3 = findViewById(R.id.fotooutlet3);

        teksdisplay1 = findViewById(R.id.teksdisplay1);
        teksdisplay2 = findViewById(R.id.teksdisplay2);
        teksdisplay3 = findViewById(R.id.teksdisplay3);

        fotottd = findViewById(R.id.fotottd);

        tanggal = findViewById(R.id.tanggal);
        waktu = findViewById(R.id.waktu);
        salesforce = findViewById(R.id.salesforce);
        depo = findViewById(R.id.depo);
        kode = findViewById(R.id.kode);
        pelanggan = findViewById(R.id.pelanggan);
        driver = findViewById(R.id.driver);
        alamatdetail = findViewById(R.id.alamatdetail);
        location = findViewById(R.id.location);

        kesesuaian = findViewById(R.id.kesesuaian);
        rutinitas = findViewById(R.id.rutinitas);
        penyampaian = findViewById(R.id.penyampaian);
        keluhan = findViewById(R.id.keluhan);

        keterangan_temuan = findViewById(R.id.keterangan_temuan);
        keterangantemuan = findViewById(R.id.keterangantemuan);

        cardOutlet = findViewById(R.id.cardOutlet);
        cardOutlet2 = findViewById(R.id.cardOutlet2);
        cardOutlet3 = findViewById(R.id.cardOutlet3);


        infopelanggan = findViewById(R.id.infopelanggan);

        tanggal.setText(convertFormatDate(getIntent().getStringExtra("tanggal_waktu")));
        waktu.setText(convertFormatJam(getIntent().getStringExtra("tanggal_waktu")));
        salesforce.setText(getIntent().getStringExtra("sales_force"));
        depo.setText(sharedPreferences.getString("depo", null));
        kode.setText(getIntent().getStringExtra("kode"));

        pelanggan.setText(getIntent().getStringExtra("nama_pelanggan"));
        driver.setText(getIntent().getStringExtra("nama_driver"));

        Glide.with(detail_memento.this)
                .load("http://192.168.4.168/gambar_memento/tanda_tangan/" + getIntent().getStringExtra("ttd"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(fotottd);

        // outlet //
        String aString =getIntent().getStringExtra("outlet");
        String cutString = aString.substring(0, 34);

        ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional
        imagePopup.initiatePopupWithPicasso("http://192.168.4.168/gambar_memento/outlet/" + cutString + "_1.jpeg"); // Load Image from Drawable
        cardOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopup.viewPopup();
            }
        });

        ImagePopup imagePopup2 = new ImagePopup(this);
        imagePopup2.setHideCloseIcon(true);  // Optional
        imagePopup2.setImageOnClickClose(true);  // Optional
        imagePopup2.initiatePopupWithPicasso("http://192.168.4.168/gambar_memento/outlet/" + cutString + "_2.jpeg"); // Load Image from Drawable
        cardOutlet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopup2.viewPopup();
            }
        });

        ImagePopup imagePopup3 = new ImagePopup(this);
        imagePopup3.setHideCloseIcon(true);  // Optional
        imagePopup3.setImageOnClickClose(true);  // Optional
        imagePopup3.initiatePopupWithPicasso("http://192.168.4.168/gambar_memento/outlet/" + cutString + "_3.jpeg"); // Load Image from Drawable

        cardOutlet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopup3.viewPopup();
            }
        });

        Glide.with(detail_memento.this)
                .load("http://192.168.4.168/gambar_memento/outlet/" + cutString + "_1.jpeg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.error_outline)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        cardOutlet.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(fotooutlet);

        Glide.with(detail_memento.this)
                .load("http://192.168.4.168/gambar_memento/outlet/" + cutString + "_2.jpeg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.error_outline)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        cardOutlet2.setVisibility(View.GONE);
                        teksdisplay3.setText("Foto Display Outlet 2");
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(fotooutlet2);

        Glide.with(detail_memento.this)
                .load("http://192.168.4.168/gambar_memento/outlet/" + cutString + "_3.jpeg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.error_outline)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        cardOutlet3.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(fotooutlet3);
        // ***** //

        kesesuaian.setText(getIntent().getStringExtra("qtyvsdo"));
        rutinitas.setText(getIntent().getStringExtra("rutin"));
        penyampaian.setText(getIntent().getStringExtra("penyampaian"));
        keluhan.setText(getIntent().getStringExtra("attitude"));
        keterangan_temuan.setText(getIntent().getStringExtra("kategori"));
        keterangantemuan.setText(getIntent().getStringExtra("keterangan"));
        if(getIntent().getStringExtra("keterangan").equals("Tidak Ada Temuan")){
            int colorInt = getResources().getColor(R.color.background_green);
            ColorStateList csl = ColorStateList.valueOf(colorInt);
            int colorInt2 = getResources().getColor(R.color.border_green);
            ColorStateList csl2 = ColorStateList.valueOf(colorInt2);
            keterangan_temuan.setChipBackgroundColor(csl);
            keterangan_temuan.setChipStrokeColor(csl2);
            keterangan_temuan.setTextColor(Color.parseColor("#2ECC71"));
        } else {
            int colorInt = getResources().getColor(R.color.background_red);
            ColorStateList csl = ColorStateList.valueOf(colorInt);

            int colorInt2 = getResources().getColor(R.color.border_red);
            ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

            keterangan_temuan.setChipBackgroundColor(csl);
            keterangan_temuan.setChipStrokeColor(csl2);
            keterangan_temuan.setTextColor(Color.parseColor("#FB4141"));
        }

        infopelanggan.setText(getIntent().getStringExtra("info"));

        alamatdetail.setText(getIntent().getStringExtra("alamat"));


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://maps.google.com?q="+getIntent().getStringExtra("longitude")+","+getIntent().getStringExtra("latitude")));
                startActivity(i);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(detail_memento.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.show();
                StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "http://192.168.4.168/api_memento/Kunjungan/index_delete",
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                pDialog.dismissWithAnimation();
                                Intent succes = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                                startActivity(succes);
                                finish();

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



                        params.put("id_Auto", getIntent().getStringExtra("ids"));



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
                RequestQueue requestQueue2 = Volley.newRequestQueue(detail_memento.this);
                requestQueue2.add(stringRequest2);
            }
        });

        // dialog show up //
        final Dialog dialog = new Dialog(detail_memento.this);
        dialog.setContentView(R.layout.edit_memento);

        editdriver = dialog.findViewById(R.id.editdriver);
        edittemuan = dialog.findViewById(R.id.edittemuan);
        editketerangantemuan = dialog.findViewById(R.id.editketerangantemuan);
        editinfo = dialog.findViewById(R.id.editinfo);
        editlonglat = dialog.findViewById(R.id.editlonglat);
        editalamat = dialog.findViewById(R.id.editalamat);

        Button batal = dialog.findViewById(R.id.batal);
        Button simpan = dialog.findViewById(R.id.simpan);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editdriver.getText().toString().length() == 0){
                    editdriver.setError("Isi Driver");
                } else if(edittemuan.getText().toString().length() == 0){
                    edittemuan.setError("Pilih Temuan");
                } else if (editketerangantemuan.getText().toString().length() ==0){
                    editketerangantemuan.setError("Pilih Keterangan Temuan");
                } else if (editinfo.getText().toString().length() ==0){
                    editinfo.setError("Isi Info Pelanggan");
                } else if (editalamat.getText().toString().length() ==0){
                    editalamat.setError("Isi Alamat");
                } else {
                    updateData();
                }
            }
        });


        editdriver.setText(getIntent().getStringExtra("nama_driver"));
        edittemuan.setText(getIntent().getStringExtra("kategori"));
        editketerangantemuan.setText(getIntent().getStringExtra("keterangan"));



        editinfo.setText(getIntent().getStringExtra("info"));
        editlonglat.setText(getIntent().getStringExtra("latitude")+","+getIntent().getStringExtra("longitude"));
        editalamat.setText(getIntent().getStringExtra("alamat"));

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
                            editdriver.setAdapter(new ArrayAdapter<String>(detail_memento.this, android.R.layout.simple_expandable_list_item_1, driver_list));
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
        RequestQueue driverqueue = Volley.newRequestQueue(detail_memento.this);
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
                            edittemuan.setAdapter(new ArrayAdapter<String>(detail_memento.this, android.R.layout.simple_expandable_list_item_1, category));
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
        RequestQueue kategori = Volley.newRequestQueue(detail_memento.this);
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
                                    editketerangantemuan.setAdapter(new ArrayAdapter<String>(detail_memento.this, android.R.layout.simple_expandable_list_item_1, subcategory));
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
                RequestQueue kategori = Volley.newRequestQueue(detail_memento.this);
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
        // dialog show up //


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.show();
            }

        });

    }

    private void updateData() {
        pDialog = new SweetAlertDialog(detail_memento.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "http://192.168.4.168/api_memento/Kunjungan/index_update",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        Intent succes = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                        startActivity(succes);
                        finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismissWithAnimation();
                Intent succes = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                startActivity(succes);
                finish();
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



                params.put("id_Auto", getIntent().getStringExtra("ids"));
                params.put("verifikasi_alamat", editalamat.getText().toString());
                params.put("nama_driver", editdriver.getText().toString());
                params.put("info_pelanggan", editinfo.getText().toString());
                params.put("kategori_temuan", edittemuan.getText().toString());
                params.put("keterangan_temuan", editketerangantemuan.getText().toString());



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(detail_memento.this);
        requestQueue2.add(stringRequest2);
    }

    private void updateGambar() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://192.168.4.168/upload_gambar/upload_outlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                pDialog.dismissWithAnimation();
                                Intent succes = new Intent(getApplicationContext(), berhasil_rasio_galon.class);
                                startActivity(succes);
                                finish();
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(detail_memento.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Kesalahan Dalam Sistem")
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

                String nama_foto = getIntent().getStringExtra("outlet");
                String rest = nama_foto;
                String[] parts = rest.split("\\.");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                params.put("nama_foto", restnomorbaru);
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(detail_memento.this);
        requestQueue2.add(stringRequest2);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {

                InputStream inputStream = getContentResolver().openInputStream(path);
                bitmap = BitmapFactory.decodeStream(inputStream);
                uploaddisplay.setImageBitmap(bitmap);
                uploaddisplay.setVisibility(View.VISIBLE);

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


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Double langitude = Double.valueOf(getIntent().getStringExtra("longitude"));
        Double longitude = Double.valueOf(getIntent().getStringExtra("latitude"));


        // Add a marker in Sydney and move the camera
        LatLng zoom = new LatLng(langitude, longitude);

        mMap.addMarker(new MarkerOptions().position(zoom).title("Lokasi Anda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));
        Geocoder gc = new Geocoder(getApplicationContext());


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoom, 10));

    }


    public static String convertFormatDate(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    public static String convertFormatJam(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("HH:mm:ss");
        return convetDateFormat.format(date);
    }

    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}