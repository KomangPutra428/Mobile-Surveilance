package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.tvip.surveylance.foto_outlet.bitmapdalam;
import static com.tvip.surveylance.foto_outlet.bitmapdepan;
import static com.tvip.surveylance.foto_outlet.bitmapkanan;
import static com.tvip.surveylance.foto_outlet.bitmapkiri;
import static com.tvip.surveylance.foto_outlet.bitmapmasuk;
import static com.tvip.surveylance.foto_outlet.bitmapparkir;

public class kondisi_outlet extends AppCompatActivity {
    CheckBox layakdepan, tidaklayakdepan;
    CheckBox layakgudang, tidaklayakgudang;
    CheckBox layakparkir, tidaklayakparkir;
    CheckBox layakmasuk, tidaklayakmasuk;
    CheckBox layakkanan, tidaklayakkanan;
    CheckBox layakkiri, tidaklayakkiri;

    EditText editdepan, editgudang, editparkir, editmasuk, editkanan, editkiri;

    Button batal, simpan;

    SweetAlertDialog pDialog;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kondisi_outlet);
        HttpsTrustManager.allowAllSSL();
        layakdepan = findViewById(R.id.layakdepan);
        tidaklayakdepan = findViewById(R.id.tidaklayakdepan);
        editdepan = findViewById(R.id.editdepan);

        layakgudang = findViewById(R.id.layakgudang);
        tidaklayakgudang = findViewById(R.id.tidaklayakgudang);
        editgudang = findViewById(R.id.editgudang);

        layakparkir = findViewById(R.id.layakparkir);
        tidaklayakparkir = findViewById(R.id.tidaklayakparkir);
        editparkir = findViewById(R.id.editparkir);

        layakmasuk = findViewById(R.id.layakmasuk);
        tidaklayakmasuk = findViewById(R.id.tidaklayakmasuk);
        editmasuk = findViewById(R.id.editmasuk);

        layakkanan = findViewById(R.id.layakkanan);
        tidaklayakkanan = findViewById(R.id.tidaklayakkanan);
        editkanan = findViewById(R.id.editkanan);

        layakkiri = findViewById(R.id.layakkiri);
        tidaklayakkiri = findViewById(R.id.tidaklayakkiri);
        editkiri = findViewById(R.id.editkiri);

        batal = findViewById(R.id.batal);
        simpan = findViewById(R.id.simpan);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!layakdepan.isChecked() && !tidaklayakdepan.isChecked()){
                    new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kondisi Bagian Depan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(!layakgudang.isChecked() && !tidaklayakgudang.isChecked()){
                    new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kondisi Bagian Dalam Gudang")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(!layakparkir.isChecked() && !tidaklayakparkir.isChecked()){
                    new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kondisi Bagian Parkir")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(!layakmasuk.isChecked() && !tidaklayakmasuk.isChecked()){
                    new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kondisi Jalan Masuk")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(!layakkanan.isChecked() && !tidaklayakkanan.isChecked()){
                    new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kondisi Kanan Outlet")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(!layakkiri.isChecked() && !tidaklayakkiri.isChecked()){
                    new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kondisi Kiri Outlet")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    uploaddepan();
                }
            }
        });

        layakdepan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidaklayakdepan.setChecked(false);
                }
            }
        });
        tidaklayakdepan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layakdepan.setChecked(false);
                }
            }
        });

        layakgudang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidaklayakgudang.setChecked(false);
                }
            }
        });
        tidaklayakgudang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layakgudang.setChecked(false);
                }
            }
        });

        layakparkir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidaklayakparkir.setChecked(false);
                }
            }
        });
        tidaklayakparkir.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layakparkir.setChecked(false);
                }
            }
        });

        layakmasuk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidaklayakmasuk.setChecked(false);
                }
            }
        });
        tidaklayakmasuk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layakmasuk.setChecked(false);
                }
            }
        });

        layakkanan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidaklayakkanan.setChecked(false);
                }
            }
        });
        tidaklayakkanan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layakkanan.setChecked(false);
                }
            }
        });

        layakkiri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidaklayakkiri.setChecked(false);
                }
            }
        });
        tidaklayakkiri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layakkiri.setChecked(false);
                }
            }
        });

    }

    private void uploaddepan() {
        pDialog = new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_fotooutlet",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploaddalam();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(String.valueOf(error))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

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
                params.put("dokumen", szId + "_Depan.jpeg");
                if(layakdepan.isChecked()){
                    params.put("hasil", "Layak");
                } else {
                    params.put("hasil", "Tidak Layak");
                }
                if(editdepan.getText().toString().length()==0){
                    params.put("keterangan", "Tidak Ada Keterangan");
                } else {
                    params.put("keterangan", editdepan.getText().toString());
                }
                params.put("kode_upload", "1");


                params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                params.put("longitude", MainActivity.longitude);
                params.put("latitude", MainActivity.latitude);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploaddalam() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_fotooutlet",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadparkir();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(String.valueOf(error))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

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
                params.put("dokumen", szId + "_Dalam.jpeg");
                if(layakgudang.isChecked()){
                    params.put("hasil", "Layak");
                } else {
                    params.put("hasil", "Tidak Layak");
                }

                if(editgudang.getText().toString().length()==0){
                    params.put("keterangan", "Tidak Ada Keterangan");
                } else {
                    params.put("keterangan", editgudang.getText().toString());
                }
                params.put("kode_upload", "2");

                params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                params.put("longitude", MainActivity.longitude);
                params.put("latitude", MainActivity.latitude);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadparkir() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_fotooutlet",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadmasuk();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(String.valueOf(error))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

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
                params.put("dokumen", szId + "_Parkir.jpeg");
                if(layakparkir.isChecked()){
                    params.put("hasil", "Layak");
                } else {
                    params.put("hasil", "Tidak Layak");
                }

                if(editparkir.getText().toString().length()==0){
                    params.put("keterangan", "Tidak Ada Keterangan");
                } else {
                    params.put("keterangan", editparkir.getText().toString());
                }
                params.put("kode_upload", "3");

                params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                params.put("longitude", MainActivity.longitude);
                params.put("latitude", MainActivity.latitude);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadmasuk() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_fotooutlet",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadkanan();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(String.valueOf(error))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

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
                params.put("dokumen", szId + "_Masuk.jpeg");
                if(layakmasuk.isChecked()){
                    params.put("hasil", "Layak");
                } else {
                    params.put("hasil", "Tidak Layak");
                }

                if(editmasuk.getText().toString().length()==0){
                    params.put("keterangan", "Tidak Ada Keterangan");
                } else {
                    params.put("keterangan", editmasuk.getText().toString());
                }
                params.put("kode_upload", "4");

                params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                params.put("longitude", MainActivity.longitude);
                params.put("latitude", MainActivity.latitude);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadkanan() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_fotooutlet",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadkiri();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(String.valueOf(error))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

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
                params.put("dokumen", szId + "_Kanan.jpeg");
                if(layakkanan.isChecked()){
                    params.put("hasil", "Layak");
                } else {
                    params.put("hasil", "Tidak Layak");
                }

                if(editkanan.getText().toString().length()==0){
                    params.put("keterangan", "Tidak Ada Keterangan");
                } else {
                    params.put("keterangan", editkanan.getText().toString());
                }
                params.put("kode_upload", "5");

                params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                params.put("longitude", MainActivity.longitude);
                params.put("latitude", MainActivity.latitude);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadkiri() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_fotooutlet",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadgambardepan();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(String.valueOf(error))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

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
                params.put("dokumen", szId + "_Kiri.jpeg");
                if(layakkiri.isChecked()){
                    params.put("hasil", "Layak");
                } else {
                    params.put("hasil", "Tidak Layak");
                }

                if(editkiri.getText().toString().length()==0){
                    params.put("keterangan", "Tidak Ada Keterangan");
                } else {
                    params.put("keterangan", editkiri.getText().toString());
                }
                params.put("kode_upload", "6");

                params.put("nik", sharedPreferences.getString("nik_karyawan", null));
                params.put("longitude", MainActivity.longitude);
                params.put("latitude", MainActivity.latitude);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadgambardepan() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_fotooutlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                uploadgambardalam();
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
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
                        pDialog.cancel();
                        new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Kesalahan Dalam Sistem")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
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
                String gambar = imagetoString(bitmapdepan);


                params.put("nik", szId + "_Depan");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadgambardalam() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_fotooutlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                uploadgambarparkir();
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
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
                        pDialog.cancel();
                        new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Kesalahan Dalam Sistem")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
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
                String gambar = imagetoString(bitmapdalam);


                params.put("nik", szId + "_Dalam");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadgambarparkir() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_fotooutlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                uploadgambarmasuk();
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
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
                        pDialog.cancel();
                        new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Kesalahan Dalam Sistem")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
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
                String gambar = imagetoString(bitmapparkir);


                params.put("nik", szId + "_Parkir");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadgambarmasuk() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_fotooutlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                uploadgambarkanan();

                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
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
                        pDialog.cancel();
                        new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Kesalahan Dalam Sistem")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
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
                String gambar = imagetoString(bitmapmasuk);


                params.put("nik", szId + "_Masuk");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadgambarkanan() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_fotooutlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                uploadgambarkiri();
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
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
                        pDialog.cancel();
                        new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Kesalahan Dalam Sistem")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
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
                String gambar = imagetoString(bitmapkanan);


                params.put("nik", szId + "_Kanan");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private void uploadgambarkiri() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_fotooutlet.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                pDialog.cancel();
                                SweetAlertDialog success = new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.SUCCESS_TYPE);
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
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
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
                        pDialog.cancel();
                        new SweetAlertDialog(kondisi_outlet.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Kesalahan Dalam Sistem")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
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
                String gambar = imagetoString(bitmapkiri);


                params.put("nik", szId + "_Kiri");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(kondisi_outlet.this);
        requestQueue2.add(stringRequest2);
    }

    private String imagetoString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}