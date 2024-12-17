package com.tvip.surveylance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
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

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {
    EditText idpelanggan;
    Button submit;
    TextView login;
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;
    private static final int MY_REQUEST_CODE = 999 ;
    AppUpdateManager appUpdateManager;
    com.google.android.play.core.tasks.Task appUpdateInfoTask;
    InstallStateUpdatedListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        idpelanggan = findViewById(R.id.idpelanggan);
        submit = findViewById(R.id.submit);
        login = findViewById(R.id.login);
//        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
//        mUpdateManager.start();

//        cekversi();
        

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(idpelanggan.getText().toString().length() == 0 ){
                    idpelanggan.setError("Masukkan ID Pelanggan");
                } else {
                    submitpelanggan();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), login_version.class);
                startActivity(intent);
            }
        });
        if (ActivityCompat.checkSelfPermission(
                login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

//    private void cekversi(){
//        appUpdateManager = AppUpdateManagerFactory.create(login.this);
//        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                try {
//                    listener = state -> {
//                        if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                            popupSnackbarForCompleteUpdate();
//                        }
//
//                        if (state.installStatus() == InstallStatus.INSTALLED){
//                            appUpdateManager.unregisterListener(listener);
//                        }
//                    };
//
//                    appUpdateManager.registerListener(listener);
//                    appUpdateManager.startUpdateFlowForResult(
//                            (AppUpdateInfo) appUpdateInfo, AppUpdateType.IMMEDIATE, login.this, MY_REQUEST_CODE);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MY_REQUEST_CODE) {
//            if (resultCode != RESULT_OK) {
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void popupSnackbarForCompleteUpdate() {
////        Snackbar snackbar =
////                Snackbar.make(
////                        findViewById(R.id.laymain),
////                        "An update has just been downloaded.",
////                        Snackbar.LENGTH_INDEFINITE);
////        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
////        snackbar.setActionTextColor(
////                getResources().getColor(R.color.black));
////        snackbar.show();
//    }



    private void getDataKTP2() {
        StringRequest kota = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/file_perid.php?dokumen_npwp=" + idpelanggan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(String.valueOf(jsonArray).equals("[]")){
                                pDialog.cancel();
                                new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
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
                                pDialog.cancel();
                                Intent intent = new Intent(getBaseContext(), login_version.class);
                                startActivity(intent);
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
        RequestQueue requestkota = Volley.newRequestQueue(com.tvip.surveylance.login.this);
        requestkota.add(kota);
    }


    private void submitpelanggan() {
        HttpsTrustManager.allowAllSSL();
        pDialog = new SweetAlertDialog(login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        String rest = idpelanggan.getText().toString();
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        String restnomorbaru = restnomor.replace(" ", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_login?szId=" + idpelanggan.getText().toString() + "&kode_dms=" + restnomorbaru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    getDataKtp(movieObject.getString("szId"));



                                }

                            } else {
                                new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
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
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("ID anda salah!")
                                    .show();
                        } else {
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
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
        RequestQueue requestQueue = Volley.newRequestQueue(login.this);
        requestQueue.add(stringRequest);
    }

    private void getDataKtp(String szId) {
        StringRequest kota = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/file_perid.php?dokumen_npwp=" + idpelanggan.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(String.valueOf(jsonArray).equals("[]")){
                                pDialog.cancel();
                                Intent intent = new Intent(login.this, detail_pelanggan.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("nik_baru", szId);
                                editor.apply();
                                startActivity(intent);
                            } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    pDialog.cancel();
                                    new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Data Sudah Diupload")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                }
                                            })
                                            .show();
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
        RequestQueue requestkota = Volley.newRequestQueue(com.tvip.surveylance.login.this);
        requestkota.add(kota);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
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

}