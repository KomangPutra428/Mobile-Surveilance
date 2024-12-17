package com.tvip.surveylance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detail_pelanggan extends AppCompatActivity implements LocationListener {
    static TextView idpelanggan, namaoutlet, segment, pemilik, editalamatoutlet, disarankan;
    SharedPreferences sharedPreferences;
    static EditText editKodePosoutlet, editteleponoutlet;
    static AutoCompleteTextView editkotaoutlet, editkecamatanoutlet, editkelurahanoutlet;
    ArrayList<String> Kota = new ArrayList<>();
    ArrayList<String> Kecamatan = new ArrayList<>();
    ArrayList<String> Kelurahan = new ArrayList<>();
    Button batal, selanjutnya;
    SweetAlertDialog success;
    static String longitude, latitude, longitudeawal, latitudeawal;
    LocationManager locationManager;
    static EditText editRToutlet;
    static EditText editRWoutlet;
    SweetAlertDialog pDialog;
    static String szAddress, szCity, szDistrict, szSubDistrict, szZipCode;

    static String szHierarchyId, szHierarchyFull, depo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pelanggan);
        HttpsTrustManager.allowAllSSL();
        idpelanggan = findViewById(R.id.idpelanggan);
        namaoutlet = findViewById(R.id.namaoutlet);
        segment = findViewById(R.id.segment);
        pemilik = findViewById(R.id.pemilik);
        disarankan = findViewById(R.id.disarankan);
        batal = findViewById(R.id.batal);
        selanjutnya = findViewById(R.id.selanjutnya);
        getDisarankan();
        editalamatoutlet = findViewById(R.id.editalamatoutlet);

        editkotaoutlet = findViewById(R.id.editkotaoutlet);
        editkecamatanoutlet = findViewById(R.id.editkecamatanoutlet);
        editkelurahanoutlet = findViewById(R.id.editkelurahanoutlet);
        editKodePosoutlet = findViewById(R.id.editKodePosoutlet);
        editteleponoutlet = findViewById(R.id.editteleponoutlet);

        editRToutlet = findViewById(R.id.editRToutlet);
        editRWoutlet = findViewById(R.id.editRWoutlet);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getLocation();
        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editalamatoutlet.setError(null);
                editkotaoutlet.setError(null);
                editkecamatanoutlet.setError(null);
                editkelurahanoutlet.setError(null);
                editRToutlet.setError(null);
                editRWoutlet.setError(null);
                editKodePosoutlet.setError(null);
                editteleponoutlet.setError(null);

                if (editteleponoutlet.getText().toString().length() == 0){
                    editteleponoutlet.setError("Masukkan nomor Telepon");
                } else if (!TextUtils.isDigitsOnly(editteleponoutlet.getText().toString())){
                    editteleponoutlet.setError("Nomor Harus Angka");
                } else if (editteleponoutlet.getText().toString().length() < 10){
                    editteleponoutlet.setError("Nomor Telepon Minimal 10 Digit");
                } else if (editteleponoutlet.getText().toString().length() > 15){
                    editteleponoutlet.setError("Nomor Telepon Maksimal 15 Digit");
                } else if(latitude == null){
                    new SweetAlertDialog(detail_pelanggan.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Toko Ini Tidak Ada GeoTag")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();
                } else {
                    new SweetAlertDialog(detail_pelanggan.this, SweetAlertDialog.WARNING_TYPE)
                            .setContentText("Apakah Lokasi Anda Berada Di Toko ?")
                            .setConfirmText("Ya")
                            .setCancelText("Tidak")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    pDialog = new SweetAlertDialog(detail_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("Harap Menunggu");
                                    pDialog.show();
                                    updateData();
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    pDialog = new SweetAlertDialog(detail_pelanggan.this, SweetAlertDialog.PROGRESS_TYPE);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                    pDialog.setTitleText("Harap Menunggu");
                                    pDialog.show();
                                    updateDataNoLongLat();
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }
            }
        });

        editkotaoutlet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editkotaoutlet.showDropDown();
            }
        });
        editkotaoutlet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editkotaoutlet.showDropDown();
                return false;
            }
        });

        editkelurahanoutlet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editkelurahanoutlet.showDropDown();
            }
        });
        editkelurahanoutlet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editkelurahanoutlet.showDropDown();
                return false;
            }
        });

        editkecamatanoutlet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editkecamatanoutlet.showDropDown();
            }
        });
        editkecamatanoutlet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editkecamatanoutlet.showDropDown();
                return false;
            }
        });

        editkotaoutlet.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                editkecamatanoutlet.setText("");
                editkelurahanoutlet.setText("");
                editKodePosoutlet.setText("");
                Kecamatan.clear();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("nik_baru", null);
                String rest = nik_baru;
                String[] parts = rest.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");
                StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_kecamatan?kota=" + editkotaoutlet.getText().toString() +"&kode_dms=" + restnomorbaru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String city = jsonObject1.getString("szDistrict");
                                            Kecamatan.add(city);

                                        }
                                    }
                                    editkecamatanoutlet.setAdapter(new ArrayAdapter<String>(detail_pelanggan.this, android.R.layout.simple_expandable_list_item_1, Kecamatan));
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
                kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestkota = Volley.newRequestQueue(detail_pelanggan.this);
                requestkota.add(kecamatan);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        editkecamatanoutlet.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                editkelurahanoutlet.setText("");
                editKodePosoutlet.setText("");
                Kelurahan.clear();
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("nik_baru", null);
                String rest = nik_baru;
                String[] parts = rest.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");
                StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_kelurahan?kelurahan=" + editkecamatanoutlet.getText().toString() +"&kode_dms=" + restnomorbaru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String city = jsonObject1.getString("szSubDistrict");
                                            Kelurahan.add(city);

                                        }
                                    }
                                    editkelurahanoutlet.setAdapter(new ArrayAdapter<String>(detail_pelanggan.this, android.R.layout.simple_expandable_list_item_1, Kelurahan));
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
                kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestkota = Volley.newRequestQueue(detail_pelanggan.this);
                requestkota.add(kecamatan);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        editkelurahanoutlet.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                editKodePosoutlet.setText("");
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String nik_baru = sharedPreferences.getString("nik_baru", null);
                String rest = nik_baru;
                String[] parts = rest.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");
                System.out.println("Hasil Nomor = " + restnomorbaru);
                StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_kodepos?kelurahan="+ editkecamatanoutlet.getText().toString()+"&kecamatan=" +editkelurahanoutlet.getText().toString() +"&kode_dms=" + restnomorbaru,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String nomor = jsonObject1.getString("szZipCode");
                                            editKodePosoutlet.setText(nomor);
                                        }
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
                kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestkota = Volley.newRequestQueue(detail_pelanggan.this);
                requestkota.add(kecamatan);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("nik_baru", null);
        String rest = nik_baru;
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        String restnomorbaru = restnomor.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_login?szId=" + nik_baru + "&kode_dms=" + restnomorbaru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    idpelanggan.setText(movieObject.getString("szId"));
                                    namaoutlet.setText(movieObject.getString("szName"));
                                    szHierarchyId = movieObject.getString("id_hirarki");
                                    szHierarchyFull = movieObject.getString("szHierarchyFull");
                                    depo = movieObject.getString("depo");

                                    if(movieObject.getString("szHierarchyId").equals("")){
                                        segment.setText("-");
                                    } else {
                                        segment.setText(movieObject.getString("szHierarchyId"));
                                    }

                                    if(movieObject.getString("szContactPerson1").equals("")){
                                        pemilik.setText("-");
                                    } else {
                                        pemilik.setText(movieObject.getString("szContactPerson1"));
                                    }
                                    editalamatoutlet.setText(movieObject.getString("szAddress"));
                                    editkotaoutlet.setText(movieObject.getString("szCity"));
                                    editkecamatanoutlet.setText(movieObject.getString("szDistrict"));
                                    editkelurahanoutlet.setText(movieObject.getString("szSubDistrict"));
                                    editKodePosoutlet.setText(movieObject.getString("szZipCode"));
                                    editteleponoutlet.setText(movieObject.getString("szPhone1"));

                                    longitudeawal = movieObject.getString("szLongitude");
                                    latitudeawal = movieObject.getString("szLatitude");

                                    szAddress = movieObject.getString("szAddress");
                                    szCity = movieObject.getString("szCity");
                                    szDistrict = movieObject.getString("szDistrict");
                                    szSubDistrict = movieObject.getString("szSubDistrict");
                                    szZipCode = movieObject.getString("szZipCode");


                                }
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
        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(detail_pelanggan.this);
        requestQueue.add(stringRequest);


        StringRequest kota = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_kota?kode_dms=" + restnomorbaru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String city = jsonObject1.getString("szCity");
                                    Kota.add(city);

                                }
                            }
                            editkotaoutlet.setAdapter(new ArrayAdapter<String>(detail_pelanggan.this, android.R.layout.simple_expandable_list_item_1, Kota));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
        kota.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(this);
        requestkota.add(kota);
    }

    private void getDisarankan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("nik_baru", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_segment?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                if(movieObject.getString("jenis_outlet").equals("1")) {
                                    disarankan.setText("STAR OUTLET");
                                } else if(movieObject.getString("jenis_outlet").equals("2")) {
                                    disarankan.setText("WHOLESALER");
                                } else if(movieObject.getString("jenis_outlet").equals("3")){
                                    disarankan.setText("MANAGE OUTLET");
                                } else if(movieObject.getString("jenis_outlet").equals("4")){
                                    disarankan.setText("AHS");
                                }



                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        disarankan.setText("-");
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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(detail_pelanggan.this);
        requestQueue.add(stringRequest);
    }

    private void updateDataNoLongLat() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("nik_baru", null);
        String rest = nik_baru;
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        final String restnomorbaru = restnomor.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_alamat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getCurrentDate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getCurrentDate();

                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String id = sharedPreferences.getString("nik_baru", null);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                params.put("szId", id);
                params.put("szAddress", szAddress);
                params.put("szCity", szCity);
                params.put("szDistrict", szDistrict);
                params.put("szSubDistrict", szSubDistrict);
                params.put("szZipCode", szZipCode);
                params.put("szPhone1", editteleponoutlet.getText().toString());
                params.put("kode_dms", restnomorbaru);
                params.put("szLongitude", longitudeawal);
                params.put("szLatitude", latitudeawal);
                params.put("dtmLastUpdated", currentDateandTime);



                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCurrentDate() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_tanggal",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.cancel();
                        success = new SweetAlertDialog(detail_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Data Sudah Disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent biodata =new Intent(getBaseContext(), upload_ktp.class);
                                startActivity(biodata);
                                success.dismissWithAnimation();
                            }
                        });
                        success.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.cancel();
                        success = new SweetAlertDialog(detail_pelanggan.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Data Sudah Disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent biodata =new Intent(getBaseContext(), upload_ktp.class);
                                startActivity(biodata);
                                success.dismissWithAnimation();
                            }
                        });
                        success.show();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String id = sharedPreferences.getString("nik_baru", null);
                String rest = id;
                String[] parts = rest.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                params.put("szId", id);
                params.put("kode_dms", restnomorbaru);
                params.put("dtmLastUpdated", currentDateandTime);

                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void updateData() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("nik_baru", null);
        String rest = nik_baru;
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        final String restnomorbaru = restnomor.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_alamat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getCurrentDate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getCurrentDate();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String id = sharedPreferences.getString("nik_baru", null);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());

                params.put("szId", id);
                params.put("szAddress", szAddress);
                params.put("szCity", szCity);
                params.put("szDistrict", szDistrict);
                params.put("szSubDistrict", szSubDistrict);
                params.put("szZipCode", szZipCode);
                params.put("szPhone1", editteleponoutlet.getText().toString());
                params.put("kode_dms", restnomorbaru);
                params.put("szLongitude", longitude);
                params.put("szLatitude", latitude);
                params.put("dtmLastUpdated", currentDateandTime);


                return params;
            }

        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5, detail_pelanggan.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(detail_pelanggan.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);


            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}