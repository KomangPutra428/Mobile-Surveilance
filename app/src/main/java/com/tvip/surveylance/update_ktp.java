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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.tvip.surveylance.menu_profile.idnpwp;
import static com.tvip.surveylance.update_pelanggan.alamat;
import static com.tvip.surveylance.update_pelanggan.latitudepost;
import static com.tvip.surveylance.update_pelanggan.longitudepost;
import static com.tvip.surveylance.update_pelanggan.telepon;

public class update_ktp extends AppCompatActivity {
    RelativeLayout ktp;
    Bitmap ktpbitmap;
    ImageView uploadktp;
    TextView textktp;
    CheckBox iya, tidak;
    static EditText nomorktp;
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;
    Button batal, selanjutnya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ktp);
        HttpsTrustManager.allowAllSSL();
        ktp = findViewById(R.id.ktp);
        textktp = findViewById(R.id.textktp);
        uploadktp = findViewById(R.id.uploadktp);
        nomorktp = findViewById(R.id.nomorktp);
        batal = findViewById(R.id.batal);
        selanjutnya = findViewById(R.id.selanjutnya);

        iya = findViewById(R.id.iya);
        tidak = findViewById(R.id.tidak);

        iya.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tidak.setChecked(false);
                }
            }
        });

        tidak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    iya.setChecked(false);
                }
            }
        });

        ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(update_ktp.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomorktp.setError(null);
                if(nomorktp.getText().toString().length() == 0){
                    nomorktp.setError("Isi NIK KTP");
                } else if(nomorktp.getText().toString().length() < 16){
                    nomorktp.setError("Nik ktp minimal 16");
                } else if(nomorktp.getText().toString().length() > 16){
                    nomorktp.setError("Nik ktp maksimal 16");
                } else if(textktp.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(update_ktp.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Upload KTP")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (!iya.isChecked() && !tidak.isChecked()){
                    new SweetAlertDialog(update_ktp.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Centang salah satu pilihan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    pDialog = new SweetAlertDialog(update_ktp.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setCancelable(false);
                    pDialog.setTitleText("Harap Menunggu");
                    pDialog.show();

                    StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_KTP",
                            new Response.Listener<String>() {

                                @Override
                                public void onResponse(String response) {
                                    upload_image_ktp();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                    upload_image_ktp();
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
                            String nik_baru = sharedPreferences.getString("szId", null);

                            params.put("szId", nik_baru);
                            params.put("nik_ktp", nomorktp.getText().toString());
                            params.put("dokumen_ktp",  nomorktp.getText().toString() + "_" + nik_baru + ".jpeg");


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
                    RequestQueue requestQueue2 = Volley.newRequestQueue(update_ktp.this);
                    requestQueue2.add(stringRequest2);

                }
            }
        });

    }

    private void upload_image_ktp() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_file.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("response");
                            if (status.contains("Success")) {
                                pDialog.cancel();
                                if(iya.isChecked()){
                                    update_ktp_dms2();
                                    Intent npwp = new Intent(getBaseContext(), update_npwp_ulang.class);
                                    startActivity(npwp);
                                } else {
                                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_npwp.php",
                                            new Response.Listener<String>() {

                                                @Override
                                                public void onResponse(String response) {
                                                    historyupdate();

                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            pDialog.cancel();
                                            new SweetAlertDialog(update_ktp.this, SweetAlertDialog.ERROR_TYPE)
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
                                            if(!idnpwp.getText().toString().equals("Tidak Ada Dokumen")){
                                                params.put("npwp", idnpwp.getText().toString());
                                                params.put("dokumen_npwp", idnpwp.getText().toString() + "_" + szId + ".jpeg");
                                            } else {
                                                params.put("npwp", "null");
                                                params.put("dokumen_npwp", "null");
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
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(update_ktp.this);
                                    requestQueue2.add(stringRequest2);
//                                    historyupdate();

                                }
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(update_ktp.this, SweetAlertDialog.ERROR_TYPE)
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
                        new SweetAlertDialog(update_ktp.this, SweetAlertDialog.ERROR_TYPE)
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
                String nik_baru = sharedPreferences.getString("szId", null);
                String gambar = imagetoString(ktpbitmap);

                params.put("no_pengajuan_full_day", nomorktp.getText().toString() + "_" + nik_baru);
                params.put("jenis", "KTP");
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(update_ktp.this);
        requestQueue2.add(stringRequest2);
    }

    private void historyupdate2() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/history_update.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        update_ktp_dms2();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(update_ktp.this, SweetAlertDialog.ERROR_TYPE)
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

                params.put("no_pelanggan", szId);
                params.put("alamat_pelanggan", alamat);
                params.put("no_telepon", telepon);

                params.put("nik_ktp", menu_profile.idktp.getText().toString());
                params.put("npwp", idnpwp.getText().toString());
                params.put("longitude", longitudepost);
                params.put("langitude", latitudepost);

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(update_ktp.this);
        requestQueue2.add(stringRequest2);
    }

    private void update_ktp_dms2() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_UpdateKTP",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Intent npwp = new Intent(getBaseContext(), update_npwp_ulang.class);
//                        startActivity(npwp);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Intent npwp = new Intent(getBaseContext(), update_npwp_ulang.class);
//                        startActivity(npwp);
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
                String id = sharedPreferences.getString("szId", null);

                String rest = id;
                String[] parts = rest.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                params.put("szId", id);
                params.put("kode_dms", restnomorbaru);
                params.put("szIDCard", nomorktp.getText().toString());

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

    private void historyupdate() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/history_update.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        update_ktp_dms();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(update_ktp.this, SweetAlertDialog.ERROR_TYPE)
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

                params.put("no_pelanggan", szId);
                params.put("alamat_pelanggan", update_pelanggan.editalamatoutlet.getText().toString() + " RT" + update_pelanggan.editRToutlet.getText().toString() + " /RW" + update_pelanggan.editRWoutlet.getText().toString() + ", " + update_pelanggan.editkotaoutlet.getText().toString() + ", " + update_pelanggan.editkecamatanoutlet.getText().toString() + ", " + update_pelanggan.editkelurahanoutlet.getText().toString() + ", " + update_pelanggan.editKodePosoutlet.getText().toString());
                params.put("no_telepon", update_pelanggan.editteleponoutlet.getText().toString());

                if(menu_profile.idktp.getText().toString().equals("Tidak Ada Dokumen")){
                    params.put("nik_ktp", "Tidak Ada KTP");
                } else {
                    params.put("nik_ktp", menu_profile.idktp.getText().toString());
                }

                if(menu_profile.idnpwp.getText().toString().equals("Tidak Ada Dokumen")){
                    params.put("npwp", "Tidak Ada Dokumen");
                } else {
                    params.put("npwp", menu_profile.idnpwp.getText().toString());
                }


                params.put("longitude",longitudepost);
                params.put("langitude", latitudepost);



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
        RequestQueue requestQueue2 = Volley.newRequestQueue(update_ktp.this);
        requestQueue2.add(stringRequest2);

    }

    private void update_ktp_dms() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_UpdateKTP",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(getBaseContext(), update_complete.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent intent = new Intent(getBaseContext(), update_complete.class);
                        startActivity(intent);
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
                String id = sharedPreferences.getString("szId", null);

                String rest = id;
                String[] parts = rest.split("-");
                String restnomor = parts[0];
                String restnomorbaru = restnomor.replace(" ", "");

                params.put("szId", id);
                params.put("kode_dms", restnomorbaru);
                params.put("szIDCard", nomorktp.getText().toString());

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
                textktp.setVisibility(View.GONE);
                InputStream inputStream = getContentResolver().openInputStream(path);
                ktpbitmap = BitmapFactory.decodeStream(inputStream);
                uploadktp.setImageBitmap(ktpbitmap);
                uploadktp.setVisibility(View.VISIBLE);

                ViewGroup.LayoutParams paramktp = uploadktp.getLayoutParams();

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
                uploadktp.setLayoutParams(paramktp);


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