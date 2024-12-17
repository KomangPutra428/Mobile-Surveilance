package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class pencatatan_outlet extends AppCompatActivity {
    CheckBox sosmed1, sosmed2, sosmed3, sosmed4, sosmed5;
    RadioGroup buttonmencatat;
    RadioButton catat1, catat2, catat3, catat4;

    Button kembali, simpan;
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencatatan_outlet);
        sosmed1 = findViewById(R.id.sosmed1);
        sosmed2 = findViewById(R.id.sosmed2);
        sosmed3 = findViewById(R.id.sosmed3);
        sosmed4 = findViewById(R.id.sosmed4);
        sosmed5 = findViewById(R.id.sosmed5);

        buttonmencatat = findViewById(R.id.buttonmencatat);

        catat1 = findViewById(R.id.catat1);
        catat2 = findViewById(R.id.catat2);
        catat3 = findViewById(R.id.catat3);
        catat4 = findViewById(R.id.catat4);

        kembali = findViewById(R.id.kembali);
        simpan = findViewById(R.id.simpan);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sosmed1.isChecked() && !sosmed2.isChecked() && !sosmed3.isChecked() &&
                        !sosmed4.isChecked() && !sosmed5.isChecked()){
                    new SweetAlertDialog(pencatatan_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Aplikasi")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (buttonmencatat.getCheckedRadioButtonId() == -1){
                    new SweetAlertDialog(pencatatan_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Metode Pencatatan Keuangan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    apps1();
                }
            }
        });

    }

    private void apps1() {
        pDialog = new SweetAlertDialog(pencatatan_outlet.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        if(!sosmed1.isChecked()){
            apps2();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Apps",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            apps2();
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
                    params.put("app", "1");
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(pencatatan_outlet.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void apps2() {
        if(!sosmed2.isChecked()){
            apps3();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Apps",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            apps3();
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
                    params.put("app", "2");
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(pencatatan_outlet.this);
            requestQueue2.add(stringRequest2);
        }
    }
    private void apps3() {
        if(!sosmed3.isChecked()){
            apps4();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Apps",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            apps4();
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
                    params.put("app", "3");
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(pencatatan_outlet.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void apps4() {
        if(!sosmed4.isChecked()){
            apps5();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Apps",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            apps5();
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
                    params.put("app", "4");
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(pencatatan_outlet.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void apps5() {
        if(!sosmed5.isChecked()){
            catat();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Apps",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            catat();
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
                    params.put("app", "5");
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(pencatatan_outlet.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void catat() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Keuangan",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.setCancelable(false);
                        SweetAlertDialog success = new SweetAlertDialog(pencatatan_outlet.this, SweetAlertDialog.SUCCESS_TYPE);
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
                if(catat1.isChecked()){
                    params.put("keuangan", "1");
                } else if(catat2.isChecked()){
                    params.put("keuangan", "2");
                } else if(catat3.isChecked()){
                    params.put("keuangan", "3");
                } else if(catat4.isChecked()){
                    params.put("keuangan", "4");
                }

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(pencatatan_outlet.this);
        requestQueue2.add(stringRequest2);
    }

}