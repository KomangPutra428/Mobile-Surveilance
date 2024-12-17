package com.tvip.surveylance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login_version extends AppCompatActivity {
    EditText editidpelanggan, editnomortelepon;
    Button login;
    SharedPreferences sharedPreferences;
    TextView tekslupalogin;
    LocationManager locationManager;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_version);
        HttpsTrustManager.allowAllSSL();
        editidpelanggan = findViewById(R.id.editidpelanggan);
        editnomortelepon = findViewById(R.id.editnomortelepon);
        login = findViewById(R.id.login);
        tekslupalogin = findViewById(R.id.tekslupalogin);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    dialog.cancel();
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        tekslupalogin.setOnClickListener(new View.OnClickListener() {
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

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editidpelanggan.getText().toString().length() == 0 ){
                    editidpelanggan.setError("Masukkan ID Pelanggan");
                } else if(editnomortelepon.getText().toString().length() == 0 ){
                    editnomortelepon.setError("Masukkan Nomor Telepon");
                } else {
                    loginpelanggan();
                }
            }
        });

    }

    private void loginpelanggan() {
        pDialog = new SweetAlertDialog(login_version.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        String rest = editidpelanggan.getText().toString();
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        String restnomorbaru = restnomor.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_login?szId=" + editidpelanggan.getText().toString() + "&kode_dms=" + restnomorbaru,
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


                                    if (movieObject.getString("szPhone1").equals(editnomortelepon.getText().toString())) {
                                        final String nama = movieObject.getString("szName");
                                        final String szHierarchyId = movieObject.getString("szHierarchyId");
                                        final String szContactPerson1 = movieObject.getString("szContactPerson1");
                                        final String szId = movieObject.getString("szId");
                                        final String depo = movieObject.getString("depo");

                                        getDataKtp2(nama, szHierarchyId, szContactPerson1, szId, depo);

                                    } else if (!movieObject.getString("szPhone1").equals(editnomortelepon.getText().toString())) {
                                        new SweetAlertDialog(login_version.this, SweetAlertDialog.ERROR_TYPE)
                                                .setContentText("Oops... ID / Nomor Telepon Salah")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();
                                    }

                                }
                            } else {
                                pDialog.cancel();
                                new SweetAlertDialog(login_version.this, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Oops... Nomor Telepon Salah")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
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
                        if (error instanceof ServerError) {
                            new SweetAlertDialog(login_version.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("ID anda salah!")
                                    .show();
                        } else {
                            new SweetAlertDialog(login_version.this, SweetAlertDialog.ERROR_TYPE)
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
        RequestQueue requestQueue = Volley.newRequestQueue(login_version.this);
        requestQueue.add(stringRequest);
    }

    private void getDataKtp2(final String nama, final String szHierarchyId, final String szContactPerson1, String szId, String depo) {
        StringRequest kota = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/file_perid.php?dokumen_npwp=" + editidpelanggan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(String.valueOf(jsonArray).equals("[]")){
                                pDialog.cancel();
                                new SweetAlertDialog(login_version.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Silahkan Submit KTP Terlebih dahulu")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                            } else {
                                SweetAlertDialog success = new SweetAlertDialog(login_version.this, SweetAlertDialog.SUCCESS_TYPE);
                                success.setContentText("Selamat Datang");
                                success.setCancelable(false);
                                success.setConfirmText("OK");
                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("szId", szId);
                                        editor.putString("szName", nama);
                                        editor.putString("szHierarchyId", szHierarchyId);
                                        editor.putString("szContactPerson1", szContactPerson1);
                                        editor.putString("depo", depo);

                                        editor.apply();
                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();

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
        kota.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(login_version.this);
        requestkota.add(kota);
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}