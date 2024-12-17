package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
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
import com.tvip.surveylance.pojo.armada_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class menu_survey extends AppCompatActivity {
    LinearLayout profile, mapping, historypenjualan, produklain,
            pelangganoutlet, tren, armada, fotooutlet, bantuan, pencatatan;
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_survey);
        HttpsTrustManager.allowAllSSL();
        profile = findViewById(R.id.profile);
        mapping = findViewById(R.id.mapping);
        historypenjualan = findViewById(R.id.historypenjualan);
        produklain = findViewById(R.id.produklain);
        pelangganoutlet = findViewById(R.id.pelangganoutlet);
        tren = findViewById(R.id.tren);
        armada = findViewById(R.id.armada);
        fotooutlet = findViewById(R.id.fotooutlet);
        bantuan = findViewById(R.id.bantuan);
        pencatatan = findViewById(R.id.pencatatan);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                final SweetAlertDialog pDialog = new SweetAlertDialog(menu_survey.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szId = sharedPreferences.getString("szId", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_detail_gudang?szId="+ szId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        pDialog.dismissWithAnimation();

                                        Intent intent = new Intent(getBaseContext(), detail_gudang.class);
                                        intent.putExtra("kepemilikan", movieObject.getString("kepemilikan"));
                                        intent.putExtra("luas", movieObject.getString("luas_gudang"));
                                        intent.putExtra("panjang", movieObject.getString("panjang"));
                                        intent.putExtra("lebar", movieObject.getString("lebar"));
                                        intent.putExtra("galon", movieObject.getString("kapasitas_galon"));
                                        intent.putExtra("sps", movieObject.getString("kapasitas_sps"));
                                        intent.putExtra("botol", movieObject.getString("kapasitas_botol"));
                                        intent.putExtra("condition", movieObject.getString("kondisi"));
                                        startActivity(intent);

                                    }


                                } catch(JSONException e){
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Intent intent = new Intent(getBaseContext(), profile_outlet.class);
                                startActivity(intent);
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
                RequestQueue requestQueue = Volley.newRequestQueue(menu_survey.this);
                requestQueue.add(stringRequest);

            }
        });

        mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), mapping_outlet.class);
                startActivity(intent);
            }
        });

        historypenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), history_penjualan.class);
                startActivity(intent);
            }
        });

        pencatatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                final SweetAlertDialog pDialog = new SweetAlertDialog(menu_survey.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szId = sharedPreferences.getString("szId", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_keuangan?szId="+ szId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        pDialog.dismissWithAnimation();

                                        Intent intent = new Intent(getBaseContext(), detail_pencatatan_outlet.class);
                                        intent.putExtra("jenis_keuangan", movieObject.getString("jenis_keuangan"));

                                        startActivity(intent);

                                    }


                                } catch(JSONException e){
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Intent intent = new Intent(getBaseContext(), pencatatan_outlet.class);
                                startActivity(intent);
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
                RequestQueue requestQueue = Volley.newRequestQueue(menu_survey.this);
                requestQueue.add(stringRequest);


            }
        });

        produklain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                final SweetAlertDialog pDialog = new SweetAlertDialog(menu_survey.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szId = sharedPreferences.getString("szId", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_detail_coverage?szId="+ szId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        pDialog.dismissWithAnimation();

                                        Intent intent = new Intent(getBaseContext(), penjualan_produk.class);
                                        intent.putExtra("area", movieObject.getString("area"));

                                        startActivity(intent);

                                    }


                                } catch(JSONException e){
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Intent intent = new Intent(getBaseContext(), penjualan_produk_lain.class);
                                startActivity(intent);
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
                RequestQueue requestQueue = Volley.newRequestQueue(menu_survey.this);
                requestQueue.add(stringRequest);

            }
        });

        pelangganoutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                pDialog = new SweetAlertDialog(menu_survey.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szId = sharedPreferences.getString("szId", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_detail_coverage?szId="+ szId,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                    }
                                    getComparing();

                                } catch(JSONException e){
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(menu_survey.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Data Pelanggan Belum Ada")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(menu_survey.this);
                requestQueue.add(stringRequest);

            }
        });

        tren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), tren_penjualan.class);
                startActivity(intent);
            }
        });

        armada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), armada_operasional.class);
                startActivity(intent);
            }
        });

        fotooutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                final SweetAlertDialog pDialog = new SweetAlertDialog(menu_survey.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szId = sharedPreferences.getString("szId", null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_fotooutlet?szId="+ szId + "&kode=1",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);
                                    final JSONArray movieArray = obj.getJSONArray("data");
                                    for (int i = 0; i < movieArray.length(); i++) {
                                        final JSONObject movieObject = movieArray.getJSONObject(i);
                                        pDialog.dismissWithAnimation();

                                        Intent intent = new Intent(getBaseContext(), list_foto_outlet.class);
                                        startActivity(intent);

                                    }


                                } catch(JSONException e){
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismissWithAnimation();
                                Intent intent = new Intent(getBaseContext(), foto_outlet.class);
                                startActivity(intent);
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
                RequestQueue requestQueue = Volley.newRequestQueue(menu_survey.this);
                requestQueue.add(stringRequest);

            }
        });

        bantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appPackage="";
                if (isAppInstalled(getBaseContext(), "com.whatsapp")) {
                    appPackage = "com.whatsapp";
                    String number = "628179853402";
                    String url = "https://api.whatsapp.com/send?phone="+number;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setPackage(appPackage);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    String number = "628179853402";
                    String url = "https://api.whatsapp.com/send?phone="+number;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            }
        });


    }

    private void getComparing() {
        HttpsTrustManager.allowAllSSL();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_datapelanggan?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                            }

                            pDialog.dismissWithAnimation();

                            Intent intent = new Intent(getBaseContext(), total_pelanggan.class);
                            startActivity(intent);


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        Intent intent = new Intent(getBaseContext(), pelanggan_outlet.class);
                        startActivity(intent);
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
        RequestQueue requestQueue = Volley.newRequestQueue(menu_survey.this);
        requestQueue.add(stringRequest);
    }

    private boolean isAppInstalled(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}