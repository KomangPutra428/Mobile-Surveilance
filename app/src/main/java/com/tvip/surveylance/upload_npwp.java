package com.tvip.surveylance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.tvip.surveylance.detail_pelanggan.editKodePosoutlet;
import static com.tvip.surveylance.detail_pelanggan.editkecamatanoutlet;
import static com.tvip.surveylance.detail_pelanggan.editkelurahanoutlet;
import static com.tvip.surveylance.detail_pelanggan.editkotaoutlet;

public class upload_npwp extends AppCompatActivity {
    RelativeLayout npwp;
    Bitmap npwpbitmap;
    ImageView uploadnpwp;
    TextView textnpwp;
    EditText nomornpwp;

    Button batal, proses;
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_npwp);
        HttpsTrustManager.allowAllSSL();
        npwp = findViewById(R.id.npwp);
        textnpwp = findViewById(R.id.textnpwp);
        uploadnpwp = findViewById(R.id.uploadnpwp);
        nomornpwp = findViewById(R.id.nomornpwp);
        batal = findViewById(R.id.batal);
        proses = findViewById(R.id.proses);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nomornpwp.getText().toString().length() == 0){
                    nomornpwp.setError("Masukkan nomor NPWP");
                } else if(nomornpwp.getText().toString().length() < 15){
                    nomornpwp.setError("Nomor NPWP minimal 15");
                } else if(nomornpwp.getText().toString().length() > 15){
                    nomornpwp.setError("Nomor maksimal 15");
                } else if(textnpwp.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(upload_npwp.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Upload NPWP")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    pDialog = new SweetAlertDialog(upload_npwp.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setCancelable(false);
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.show();
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_npwp.php",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    dms_update_npwp();


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.cancel();
                            new SweetAlertDialog(upload_npwp.this, SweetAlertDialog.ERROR_TYPE)
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
                            String nik_baru = sharedPreferences.getString("nik_baru", null);

                            params.put("szId", nik_baru);
                            params.put("npwp", nomornpwp.getText().toString());
                            params.put("dokumen_npwp", nomornpwp.getText().toString() + "_" + nik_baru + ".jpeg");


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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(upload_npwp.this);
                    requestQueue2.add(stringRequest2);

                }
            }
        });




        npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(upload_npwp.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

            }
        });

    }

    private void dms_update_npwp() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_UpdateNPWP",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        upload_image_npwp();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        upload_image_npwp();
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
                params.put("szNPWP", nomornpwp.getText().toString());

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

        RequestQueue requestQueue = Volley.newRequestQueue(upload_npwp.this);
        requestQueue.add(stringRequest);
    }

    private void upload_image_npwp() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_file.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/history_update.php",
                                        new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        pDialog.cancel();
                                        new SweetAlertDialog(upload_npwp.this, SweetAlertDialog.ERROR_TYPE)
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
                                        String szId = sharedPreferences.getString("nik_baru", null);;

                                        params.put("no_pelanggan", szId);
                                        params.put("alamat_pelanggan", detail_pelanggan.editalamatoutlet.getText().toString() + " RT" + detail_pelanggan.editRToutlet.getText().toString() + " /RW" + detail_pelanggan.editRWoutlet.getText().toString() + ", " + editkotaoutlet.getText().toString() + ", " + editkecamatanoutlet.getText().toString() + ", " + editkelurahanoutlet.getText().toString() + ", " + editKodePosoutlet.getText().toString());
                                        params.put("no_telepon", detail_pelanggan.editteleponoutlet.getText().toString());

                                        params.put("nik_ktp", upload_ktp.nomorktp.getText().toString());
                                        params.put("npwp", nomornpwp.getText().toString());
                                        if(detail_pelanggan.longitude == null){
                                            params.put("longitude",detail_pelanggan.longitudeawal);
                                            params.put("langitude", detail_pelanggan.latitudeawal);
                                        } else {
                                            params.put("longitude", detail_pelanggan.longitude);
                                            params.put("langitude", detail_pelanggan.latitude);
                                        }


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
                                RequestQueue requestQueue2 = Volley.newRequestQueue(upload_npwp.this);
                                requestQueue2.add(stringRequest2);

                                pDialog.cancel();
                                Intent intent = new Intent(getBaseContext(), status_complete.class);
                                startActivity(intent);

                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(upload_npwp.this, SweetAlertDialog.ERROR_TYPE)
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
                        new SweetAlertDialog(upload_npwp.this, SweetAlertDialog.ERROR_TYPE)
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
                String nik_baru = sharedPreferences.getString("nik_baru", null);
                String gambar = imagetoString(npwpbitmap);

                params.put("no_pengajuan_full_day", nomornpwp.getText().toString() + "_" + nik_baru);
                params.put("jenis", "NPWP");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(upload_npwp.this);
        requestQueue2.add(stringRequest2);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);
            }else{
                Toast.makeText(getApplicationContext(), "You don't have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                textnpwp.setVisibility(View.GONE);
                InputStream inputStream = getContentResolver().openInputStream(path);
                npwpbitmap = BitmapFactory.decodeStream(inputStream);
                uploadnpwp.setImageBitmap(npwpbitmap);
                uploadnpwp.setVisibility(View.VISIBLE);

                ViewGroup.LayoutParams paramnpwp = uploadnpwp.getLayoutParams();

                double sizeInDP = 226;
                int marginInDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                                .getDisplayMetrics());

                double sizeInDP2 = 226;
                int marginInDp2 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                                .getDisplayMetrics());

                paramnpwp.width = marginInDp;
                paramnpwp.height = marginInDp2;
                uploadnpwp.setLayoutParams(paramnpwp);


            } catch (IOException e) {
                e.printStackTrace();
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
}