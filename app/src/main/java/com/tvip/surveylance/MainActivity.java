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
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements LocationListener {

    SharedPreferences sharedPreferences;
    static TextView txt_nama;
    TextView txt_id;
    ImageButton profiletoko;
    MaterialCardView survey, galonkosong, sfa, analisa_kompetitor;
    SweetAlertDialog success;
    static String longitude, latitude;
    LocationManager locationManager;
    FrameLayout framelayout;

    String nama_karyawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpsTrustManager.allowAllSSL();
        framelayout = findViewById(R.id.framelayout);
        txt_nama = findViewById(R.id.txt_nama);
        txt_id = findViewById(R.id.txt_id);
        profiletoko = findViewById(R.id.profiletoko);
        galonkosong = findViewById(R.id.galonkosong);
        survey = findViewById(R.id.survey);
        sfa = findViewById(R.id.sfa);
        analisa_kompetitor = findViewById(R.id.analisa_kompetitor);
        framelayout.bringToFront();
        getLocation();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        String szName = sharedPreferences.getString("szName", null);

        txt_nama.setText(szName);
        txt_id.setText(szId);

        analisa_kompetitor.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.login);

            Button batal = dialog.findViewById(R.id.batal);
            Button login = dialog.findViewById(R.id.login);

            final EditText editnik = dialog.findViewById(R.id.editnik);
            final EditText editpassword = dialog.findViewById(R.id.editpassword);


            batal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editnik.setError(null);
                    editpassword.setError(null);
                    if(editnik.getText().toString().length() == 0){
                        editnik.setError("Masukkan Nik");
                    } else if(editpassword.getText().toString().length() == 0){
                        editpassword.setError("Masukkan Password");
                    } else {
                        HttpsTrustManager.allowAllSSL();
                        final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Harap Menunggu");

                        pDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + editnik.getText().toString(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            if (obj.getString("status").equals("true")) {
                                                JSONArray movieArray = obj.getJSONArray("data");
                                                for (int i = 0; i < movieArray.length(); i++) {

                                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                                    pDialog.cancel();

                                                    if (movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("nik_karyawan", movieObject.getString("nik_baru"));
                                                        editor.putString("nama_karyawan", movieObject.getString("nama_karyawan_struktur"));
                                                        editor.apply();


                                                        success = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                                        success.setContentText("Selamat Datang");
                                                        success.setCancelable(false);
                                                        success.setConfirmText("OK");
                                                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                Intent intent = new Intent(getBaseContext(), draft_kompetitor.class);
                                                                startActivity(intent);
                                                                dialog.dismiss();
                                                                success.dismissWithAnimation();
                                                            }
                                                        }).show();
                                                    } else if (!movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                .setContentText("Oops... NIK / Password Salah")
                                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                    @Override
                                                                    public void onClick(SweetAlertDialog sDialog) {
                                                                        sDialog.dismissWithAnimation();
                                                                    }
                                                                }).show();
                                                    }

                                                }

                                            } else {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Oops... NIK / Password Salah")
                                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                            @Override
                                                            public void onClick(SweetAlertDialog sDialog) {
                                                                sDialog.dismissWithAnimation();
                                                            }
                                                        })
                                                        .show();
                                                pDialog.cancel();
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
                                        if (error instanceof ServerError) {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setContentText("Nik anda salah!")
                                                    .show();
                                        } else {
                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setContentText("Jaringan sedang bermasalah!")
                                                    .show();
                                        }
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
                        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                        requestQueue.add(stringRequest);
                    }

                }
            });
            dialog.show();
        });

        galonkosong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.login);

                Button batal = dialog.findViewById(R.id.batal);
                Button login = dialog.findViewById(R.id.login);

                final EditText editnik = dialog.findViewById(R.id.editnik);
                final EditText editpassword = dialog.findViewById(R.id.editpassword);


                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editnik.setError(null);
                        editpassword.setError(null);
                        if(editnik.getText().toString().length() == 0){
                            editnik.setError("Masukkan Nik");
                        } else if(editpassword.getText().toString().length() == 0){
                            editpassword.setError("Masukkan Password");
                        } else {
                            HttpsTrustManager.allowAllSSL();
                            final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");

                            pDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + editnik.getText().toString(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                if (obj.getString("status").equals("true")) {
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {

                                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                                        pDialog.cancel();

                                                        if (movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("nik_karyawan", movieObject.getString("nik_baru"));
                                                            editor.putString("nama_karyawan", movieObject.getString("nama_karyawan_struktur"));
                                                            editor.apply();


                                                            success = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                                            success.setContentText("Selamat Datang");
                                                            success.setCancelable(false);
                                                            success.setConfirmText("OK");
                                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    Intent intent = new Intent(getBaseContext(), list_galon_kosong.class);
                                                                    startActivity(intent);
                                                                    dialog.dismiss();
                                                                    success.dismissWithAnimation();
                                                                }
                                                            }).show();
                                                        } else if (!movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                    .setContentText("Oops... NIK / Password Salah")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    }).show();
                                                        }

                                                    }

                                                } else {
                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setContentText("Oops... NIK / Password Salah")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();
                                                    pDialog.cancel();
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
                                            if (error instanceof ServerError) {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Nik anda salah!")
                                                        .show();
                                            } else {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Jaringan sedang bermasalah!")
                                                        .show();
                                            }
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
                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                            requestQueue.add(stringRequest);
                        }

                    }
                });
                dialog.show();
            }
        });

        profiletoko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), menu_profile.class);
                startActivity(intent);
            }
        });

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.login);

                Button batal = dialog.findViewById(R.id.batal);
                Button login = dialog.findViewById(R.id.login);

                final EditText editnik = dialog.findViewById(R.id.editnik);
                final EditText editpassword = dialog.findViewById(R.id.editpassword);


                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editnik.setError(null);
                        editpassword.setError(null);
                        if(editnik.getText().toString().length() == 0){
                            editnik.setError("Masukkan Nik");
                        } else if(editpassword.getText().toString().length() == 0){
                            editpassword.setError("Masukkan Password");
                        } else if(longitude == null){
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Lokasi belum ditemukan")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            HttpsTrustManager.allowAllSSL();
                            final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");

                            pDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + editnik.getText().toString(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                if (obj.getString("status").equals("true")) {
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {

                                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                                        pDialog.cancel();

                                                        if (movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("nik_karyawan", movieObject.getString("nik_baru"));
                                                            editor.apply();


                                                            success = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                                            success.setContentText("Selamat Datang");
                                                            success.setCancelable(false);
                                                            success.setConfirmText("OK");
                                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    Intent intent = new Intent(getBaseContext(), menu_survey.class);
                                                                    startActivity(intent);
                                                                    dialog.dismiss();
                                                                    success.dismissWithAnimation();
                                                                }
                                                            }).show();
                                                        } else if (!movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                    .setContentText("Oops... NIK / Password Salah")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    }).show();
                                                        }

                                                    }

                                                } else {
                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setContentText("Oops... NIK / Password Salah")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();
                                                    pDialog.cancel();
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
                                            if (error instanceof ServerError) {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Nik anda salah!")
                                                        .show();
                                            } else {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Jaringan sedang bermasalah!")
                                                        .show();
                                            }
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
                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                            requestQueue.add(stringRequest);
                        }

                    }
                });
                dialog.show();

            }
        });

        sfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.login);

                Button batal = dialog.findViewById(R.id.batal);
                Button login = dialog.findViewById(R.id.login);

                final EditText editnik = dialog.findViewById(R.id.editnik);
                final EditText editpassword = dialog.findViewById(R.id.editpassword);
                final TextView keterangan = dialog.findViewById(R.id.keterangan);

                keterangan.setText("Silahkan masukan NIK Karyawan dan Password Anda untuk Login Sales Force Audit.");

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editnik.setError(null);
                        editpassword.setError(null);
                        if(editnik.getText().toString().length() == 0){
                            editnik.setError("Masukkan Nik");
                        } else if(editpassword.getText().toString().length() == 0){
                            editpassword.setError("Masukkan Password");
                        } else if(longitude == null){
                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Lokasi belum ditemukan")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            HttpsTrustManager.allowAllSSL();
                            final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Harap Menunggu");

                            pDialog.show();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + editnik.getText().toString(),
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                if (obj.getString("status").equals("true")) {
                                                    JSONArray movieArray = obj.getJSONArray("data");
                                                    for (int i = 0; i < movieArray.length(); i++) {

                                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                                        pDialog.cancel();

                                                        if (movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("nik_karyawan", movieObject.getString("nik_baru"));
                                                            editor.putString("nama_karyawan", movieObject.getString("nama_karyawan_struktur"));
                                                            editor.apply();


                                                            success = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                                            success.setContentText("Selamat Datang");
                                                            success.setCancelable(false);
                                                            success.setConfirmText("OK");
                                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    Intent intent = new Intent(getBaseContext(), list_memento.class);
                                                                    startActivity(intent);
                                                                    dialog.dismiss();
                                                                    success.dismissWithAnimation();
                                                                }
                                                            }).show();
                                                        } else if (!movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                                    .setContentText("Oops... NIK / Password Salah")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    }).show();
                                                        }

                                                    }

                                                } else {
                                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                            .setContentText("Oops... NIK / Password Salah")
                                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                @Override
                                                                public void onClick(SweetAlertDialog sDialog) {
                                                                    sDialog.dismissWithAnimation();
                                                                }
                                                            })
                                                            .show();
                                                    pDialog.cancel();
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
                                            if (error instanceof ServerError) {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Nik anda salah!")
                                                        .show();
                                            } else {
                                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Jaringan sedang bermasalah!")
                                                        .show();
                                            }
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
                            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                            requestQueue.add(stringRequest);
                        }

                    }
                });
                dialog.show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Anda akan keluar dari aplikasi ini")
                .setConfirmText("Yes")
                .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finishAffinity();
                        finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }
    public String md5(String s) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash)
            {
                sb.append(String.format("%02x", b&0xff));
            }
            digest = sb.toString();
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return digest;
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5, MainActivity.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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