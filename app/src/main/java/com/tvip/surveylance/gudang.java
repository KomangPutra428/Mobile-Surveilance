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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.tvip.surveylance.profile_outlet.buttonparkir;
import static com.tvip.surveylance.profile_outlet.chiller1;
import static com.tvip.surveylance.profile_outlet.chiller2;
import static com.tvip.surveylance.profile_outlet.chiller3;
import static com.tvip.surveylance.profile_outlet.chiller4;
import static com.tvip.surveylance.profile_outlet.chiller5;
import static com.tvip.surveylance.profile_outlet.lokasi1;
import static com.tvip.surveylance.profile_outlet.lokasi2;
import static com.tvip.surveylance.profile_outlet.lokasi3;
import static com.tvip.surveylance.profile_outlet.lokasi4;
import static com.tvip.surveylance.profile_outlet.lokasi5;
import static com.tvip.surveylance.profile_outlet.order1;
import static com.tvip.surveylance.profile_outlet.order2;
import static com.tvip.surveylance.profile_outlet.order3;
import static com.tvip.surveylance.profile_outlet.order4;
import static com.tvip.surveylance.profile_outlet.parkir1;
import static com.tvip.surveylance.profile_outlet.parkir2;
import static com.tvip.surveylance.profile_outlet.parkir3;
import static com.tvip.surveylance.profile_outlet.parkir4;

//import static com.tvip.surveylance.profile_outlet.buttonparkir;
//import static com.tvip.surveylance.profile_outlet.lokasi1;
//import static com.tvip.surveylance.profile_outlet.lokasi2;
//import static com.tvip.surveylance.profile_outlet.lokasi3;
//import static com.tvip.surveylance.profile_outlet.lokasi4;
//import static com.tvip.surveylance.profile_outlet.lokasi5;
//import static com.tvip.surveylance.profile_outlet.order1;
//import static com.tvip.surveylance.profile_outlet.order2;
//import static com.tvip.surveylance.profile_outlet.order3;
//import static com.tvip.surveylance.profile_outlet.order4;


public class gudang extends AppCompatActivity {
    CheckBox punya_sendiri, sewa, layak1, layak2, tidaklayak;
    EditText editluas, editpanjang, editlebar, editgalon, editsps, editbotol;
    SharedPreferences sharedPreferences;

    ImageButton minusgalon, addgalon, minussps, addsps, minusbotol, addbotol;
    SweetAlertDialog pDialog;

    Button kembali ,simpan;
    int count = 0;
    int count1 = 0;
    int count2 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gudang);
        HttpsTrustManager.allowAllSSL();
        punya_sendiri = findViewById(R.id.punya_sendiri);
        sewa = findViewById(R.id.sewa);

        editluas = findViewById(R.id.editluas);
        editpanjang = findViewById(R.id.editpanjang);
        editlebar = findViewById(R.id.editlebar);

        minusgalon = findViewById(R.id.minusgalon);
        editgalon = findViewById(R.id.editgalon);
        addgalon = findViewById(R.id.addgalon);

        minussps = findViewById(R.id.minussps);
        editsps = findViewById(R.id.editsps);
        addsps = findViewById(R.id.addsps);

        minusbotol = findViewById(R.id.minusbotol);
        editbotol = findViewById(R.id.editbotol);
        addbotol = findViewById(R.id.addbotol);

        layak1 = findViewById(R.id.layak1);
        layak2 = findViewById(R.id.layak2);
        tidaklayak = findViewById(R.id.tidaklayak);

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
                if(!punya_sendiri.isChecked() && !sewa.isChecked()){
                    new SweetAlertDialog(gudang.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kepemilikan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (editluas.getText().toString().length() == 0){
                    editluas.setError("Isi Luas");
                } else if (editpanjang.getText().toString().length() == 0){
                    editpanjang.setError("Isi Panjang");
                } else if (editlebar.getText().toString().length() == 0){
                    editlebar.setError("Isi Lebar");
                } else if(!layak1.isChecked() && !layak2.isChecked() && !tidaklayak.isChecked()){
                    new SweetAlertDialog(gudang.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kelayakan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    jmlkaryawan();

                }
            }
        });

        punya_sendiri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sewa.setChecked(false);
                }
            }
        });

        sewa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    punya_sendiri.setChecked(false);
                }
            }
        });

        layak1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layak2.setChecked(false);
                    tidaklayak.setChecked(false);
                }
            }
        });

        layak2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layak1.setChecked(false);
                    tidaklayak.setChecked(false);
                }
            }
        });

        tidaklayak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    layak1.setChecked(false);
                    layak2.setChecked(false);
                }
            }
        });



        editgalon.setText("0");
        addgalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                editgalon.setText(String.valueOf(count));
            }

        });
        minusgalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editgalon.setText(String.valueOf(count));
                if (count == 0) {
                    return;
                }
                count--;
            }
        });

        editsps.setText("0");
        addsps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count1++;
                editsps.setText(String.valueOf(count1));
            }

        });
        minussps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsps.setText(String.valueOf(count1));
                if (count1 == 0) {
                    return;
                }
                count1--;
            }
        });

        editbotol.setText("0");
        addbotol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2++;
                editbotol.setText(String.valueOf(count2));
            }

        });
        minusbotol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editbotol.setText(String.valueOf(count2));
                if (count2 == 0) {
                    return;
                }
                count2--;
            }
        });
    }

//    private void postsekarang() {

//        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_detailpelanggan",
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        jmlkaryawan();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "admin", "Databa53");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
//                String szId = sharedPreferences.getString("szId", null);
//
//                params.put("szId", szId);
//                params.put("nama_pelanggan", MainActivity.txt_nama.getText().toString());
//                params.put("alamat_pelanggan", profile_outlet.editalamatoutlet.getText().toString() + " RT " + profile_outlet.editRToutlet.getText().toString() + " / RW " + profile_outlet.editRWoutlet.getText().toString()
//                        + ", " + profile_outlet.editkotaoutlet.getText().toString() + ", " + profile_outlet.editkecamatanoutlet.getText().toString() + ", "
//                        + profile_outlet.editkelurahanoutlet.getText().toString() + ", " + profile_outlet.editKodePosoutlet.getText().toString());
//
//                params.put("nik", nik);
//                params.put("longitude", MainActivity.longitude);
//                params.put("latitude", MainActivity.latitude);
//
//                return params;
//            }
//
//        };
//        stringRequest2.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        500000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                )
//        );
//        RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
//        requestQueue2.add(stringRequest2);
//    }

    private void jmlkaryawan() {
        pDialog = new SweetAlertDialog(gudang.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_jmlkaryawan",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        joinpelanggan();
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
                params.put("jml_karyawan", profile_outlet.editkaryawan.getText().toString());

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
        requestQueue2.add(stringRequest2);
    }

    private void joinpelanggan() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_join",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        postoutlet1();
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
                params.put("join_date", convertFormat(profile_outlet.editberlangganan.getText().toString()));

                params.put("jarak", profile_outlet.editjaraktempuh.getText().toString());
                if(profile_outlet.ya.isChecked()){
                    params.put("bongkar_muat", "ya");
                } else {
                    params.put("bongkar_muat", "tidak");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
        requestQueue2.add(stringRequest2);
    }

    private void postoutlet1() {
        if(profile_outlet.check1.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_createoutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postoutlet2();
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
                    params.put("jenis_outlet", "1");
                    

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        } else {
            postoutlet2();
        }
    }

    private void postoutlet2() {
        if(profile_outlet.check2.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_createoutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postoutlet3();
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
                    params.put("jenis_outlet", "2");


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        } else {
            postoutlet3();
        }
    }

    private void postoutlet3() {
        if(profile_outlet.check3.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_createoutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postoutlet4();
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
                    params.put("jenis_outlet", "3");


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        } else {
            postoutlet4();
        }
    }

    private void postoutlet4() {
        if(profile_outlet.check4.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_createoutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postoutlet5();
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
                    params.put("jenis_outlet", "4");


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        } else {
            postoutlet5();
        }
    }

    private void postoutlet5() {
        if(profile_outlet.check5.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_createoutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postoutlet6();
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
                    params.put("jenis_outlet", "5");


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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        } else {
            postoutlet6();
        }
    }

    private void postoutlet6() {
        if(profile_outlet.check6.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_createoutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postgudang();
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
                    params.put("jenis_outlet", "6");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        } else {
            postgudang();
        }
    }

    private void postgudang() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_gudang",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        lokasioutlet();
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
                if(punya_sendiri.isChecked()){
                    params.put("kepemilikan", "Punya Sendiri");
                } else {
                    params.put("kepemilikan", "Sewa / Kontrak");
                }


                params.put("luas_gudang", editluas.getText().toString());
                params.put("kapasitas_galon", editgalon.getText().toString());
                params.put("kapasitas_sps", editsps.getText().toString());
                params.put("kapasitas_botol", editbotol.getText().toString());

                params.put("panjang", editpanjang.getText().toString());
                params.put("lebar", editlebar.getText().toString());

                if(layak1.isChecked()){
                    params.put("kondisi", "1");
                } else if (layak2.isChecked()){
                    params.put("kondisi", "2");
                } else {
                    params.put("kondisi", "3");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
        requestQueue2.add(stringRequest2);
    }

    private void lokasioutlet() {
        if(!lokasi1.isChecked()){
            lokasioutlet2();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_LokasiOutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            lokasioutlet2();
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
                    params.put("lokasi_outlet", "1");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void lokasioutlet2() {
        if(!lokasi2.isChecked()){
            lokasioutlet3();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_LokasiOutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            lokasioutlet3();
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
                    params.put("lokasi_outlet", "2");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void lokasioutlet3() {
        if(!lokasi3.isChecked()){
            lokasioutlet4();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_LokasiOutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            lokasioutlet4();
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
                    params.put("lokasi_outlet", "3");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void lokasioutlet4() {
        if(!lokasi4.isChecked()){
            lokasioutlet5();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_LokasiOutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            lokasioutlet5();
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
                    params.put("lokasi_outlet", "4");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void lokasioutlet5() {
        if(!lokasi5.isChecked()){
            parkir();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_LokasiOutlet",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            parkir();
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
                    params.put("lokasi_outlet", "5");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void parkir() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Parkir",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        getorder();
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
                int a = buttonparkir.getCheckedRadioButtonId();

                params.put("szId", szId);

                if(parkir1.isChecked()){
                    params.put("area", "1");
                } else if(parkir2.isChecked()){
                    params.put("area", "2");
                } else if(parkir3.isChecked()){
                    params.put("area", "3");
                } else if(parkir4.isChecked()){
                    params.put("area", "4");

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
        requestQueue2.add(stringRequest2);
    }

    private void getorder() {
        if(!order1.isChecked()){
            getorder2();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PenjualanAmdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            getorder2();
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
                    params.put("hasil", "1");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void getorder2() {
        if(!order2.isChecked()){
            getorder3();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PenjualanAmdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            getorder3();
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
                    params.put("hasil", "2");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void getorder3() {
        if(!order3.isChecked()){
            getorder4();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PenjualanAmdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            getorder4();
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
                    params.put("hasil", "3");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void getorder4() {
        if(!order4.isChecked()){
            postchiller();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PenjualanAmdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postchiller();
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
                    params.put("hasil", "4");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postchiller() {
        if(!chiller1.isChecked()){
            postchiller2();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Chiller",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postchiller2();
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
                    params.put("alat_penyimpanan", "1");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postchiller2() {
        if(!chiller2.isChecked()){
            postchiller3();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Chiller",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postchiller3();
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
                    params.put("alat_penyimpanan", "2");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postchiller3() {
        if(!chiller3.isChecked()){
            postchiller4();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Chiller",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postchiller4();
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
                    params.put("alat_penyimpanan", "3");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postchiller4() {
        if(!chiller4.isChecked()){
            postchiller5();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Chiller",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postchiller5();
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
                    params.put("alat_penyimpanan", "4");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void postchiller5() {
        if(!chiller5.isChecked()){
            pDialog.cancel();
            SweetAlertDialog success = new SweetAlertDialog(gudang.this, SweetAlertDialog.SUCCESS_TYPE);
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
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Chiller",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            pDialog.cancel();
                            SweetAlertDialog success = new SweetAlertDialog(gudang.this, SweetAlertDialog.SUCCESS_TYPE);
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
                    params.put("alat_penyimpanan", "5");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(gudang.this);
            requestQueue2.add(stringRequest2);
        }
    }

//    private void updateoutlet() {
//        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_updateoutlet",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        pDialog.cancel();
//                        SweetAlertDialog success = new SweetAlertDialog(gudang.this, SweetAlertDialog.SUCCESS_TYPE);
//                        success.setContentText("Berhasil disimpan");
//                        success.setCancelable(false);
//                        success.setConfirmText("OK");
//                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                                Intent i = new Intent(getApplicationContext(), menu_survey.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(i);
//                            }
//                        });
//                        success.show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pDialog.cancel();
//                        SweetAlertDialog success = new SweetAlertDialog(gudang.this, SweetAlertDialog.SUCCESS_TYPE);
//                        success.setContentText("Berhasil disimpan");
//                        success.setCancelable(false);
//                        success.setConfirmText("OK");
//                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                                Intent i = new Intent(getApplicationContext(), menu_survey.class);
//                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                startActivity(i);
//                            }
//                        });
//                        success.show();
//                    }
//                }
//        ) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "admin", "Databa53");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                String id = sharedPreferences.getString("szId", null);
//
//                String rest = id;
//                String[] parts = rest.split("-");
//                String restnomor = parts[0];
//                String restnomorbaru = restnomor.replace(" ", "");
//
//                params.put("szId", id);
//                params.put("kode_dms", restnomorbaru);
//                if(profile_outlet.check1.isChecked()){
//                    params.put("szHierarchyId", "O1");
//                } else if (profile_outlet.check2.isChecked()){
//                    params.put("szHierarchyId", "O2");
//                } else if (profile_outlet.check3.isChecked()){
//                    params.put("szHierarchyId", "O3");
//                } else if (profile_outlet.check4.isChecked()){
//                    params.put("szHierarchyId", "M1");
//                }
//
//                return params;
//            }
//
//        };
//        stringRequest.setRetryPolicy(
//                new DefaultRetryPolicy(
//                        500000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//                )
//        );
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return convetDateFormat.format(date);
    }
}