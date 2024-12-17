package com.tvip.surveylance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.tvip.surveylance.detail_pelanggan.editKodePosoutlet;
import static com.tvip.surveylance.detail_pelanggan.editkecamatanoutlet;
import static com.tvip.surveylance.detail_pelanggan.editkelurahanoutlet;
import static com.tvip.surveylance.detail_pelanggan.editkotaoutlet;
import static com.tvip.surveylance.detail_pelanggan.editteleponoutlet;
import static com.tvip.surveylance.detail_pelanggan.szAddress;
import static com.tvip.surveylance.update_pelanggan.alamat;
import static com.tvip.surveylance.update_pelanggan.latitudepost;
import static com.tvip.surveylance.update_pelanggan.longitudepost;
import static com.tvip.surveylance.update_pelanggan.telepon;

public class upload_ktp extends AppCompatActivity {
    RelativeLayout ktp;
    Bitmap ktpbitmap;
    ImageView uploadktp;
    TextView textktp;
    CheckBox iya, tidak;
    static EditText nomorktp, editalasan, edittanggallahir;
    SharedPreferences sharedPreferences;
    SweetAlertDialog pDialog;
    Button batal, selanjutnya;
    TextInputLayout alasan;

    CheckBox bersedia, tidakbersedia, bersedianppkp, tidakbersedianppkp;

    LinearLayout uploadktplinear, uploadnppkplayout;

    Calendar tanggalfullday;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public void tanggallahir() {
        final Calendar currentDate = Calendar.getInstance();
        tanggalfullday = currentDate.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                tanggalfullday.set(year, monthOfYear, dayOfMonth);

                edittanggallahir.setText(dateFormatter.format(tanggalfullday.getTime()));
                edittanggallahir.setError(null);
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(upload_ktp.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dokumen);
        HttpsTrustManager.allowAllSSL();
        editalasan = findViewById(R.id.editalasan);
        alasan = findViewById(R.id.alasan);

        edittanggallahir = findViewById(R.id.edittanggallahir);

        bersedianppkp = findViewById(R.id.bersedianppkp);
        tidakbersedianppkp = findViewById(R.id.tidakbersedianppkp);


        bersedia = findViewById(R.id.bersedia);
        tidakbersedia = findViewById(R.id.tidakbersedia);
        uploadktplinear = findViewById(R.id.uploadktplinear);
        ktp = findViewById(R.id.ktp);
        textktp = findViewById(R.id.textktp);
        uploadktp = findViewById(R.id.uploadktp);
        nomorktp = findViewById(R.id.nomorktp);
        batal = findViewById(R.id.batal);
        selanjutnya = findViewById(R.id.selanjutnya);

        iya = findViewById(R.id.iya);
        tidak = findViewById(R.id.tidak);

        bersedianppkp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    uploadnppkplayout.setVisibility(View.VISIBLE);
                    tidakbersedianppkp.setChecked(false);
                }
            }
        });

        tidakbersedianppkp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    uploadnppkplayout.setVisibility(View.GONE);
                    bersedianppkp.setChecked(false);
                }
            }
        });

        edittanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tanggallahir();
            }
        });

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
                ActivityCompat.requestPermissions(upload_ktp.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bersedia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    uploadktplinear.setVisibility(View.VISIBLE);
                    alasan.setVisibility(View.GONE);
                    tidakbersedia.setChecked(false);
                } else {
                    uploadktplinear.setVisibility(View.GONE);
                }
            }
        });

        tidakbersedia.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    uploadktplinear.setVisibility(View.GONE);
                    alasan.setVisibility(View.VISIBLE);
                    bersedia.setChecked(false);
                } else {
                    alasan.setVisibility(View.GONE);
                }
            }
        });


        selanjutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bersedia.isChecked() && !tidakbersedia.isChecked()){
                    new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Centang salah satu pilihan")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(bersedia.isChecked()){
                    if(nomorktp.getText().toString().length() == 0){
                        nomorktp.setError("Isi NIK KTP");
                    } else if(nomorktp.getText().toString().length() < 16){
                        nomorktp.setError("Nik ktp minimal 16");
                    } else if(nomorktp.getText().toString().length() > 16){
                        nomorktp.setError("Nik ktp maksimal 16");
                    } else if(textktp.getVisibility() == View.VISIBLE){
                        new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.WARNING_TYPE)
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
                        new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.WARNING_TYPE)
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
                        tanggal();
                        pDialog = new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setCancelable(false);
                        pDialog.setTitleText("Harap Menunggu");
                        pDialog.show();

                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_dokumen_with_status_2.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        dms_update_ktp();

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.cancel();
                                new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.ERROR_TYPE)
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

                                String rest = nik_baru;
                                String[] parts = rest.split("-");
                                String restnomor = parts[0];
                                String restnomorbaru = restnomor.replace(" ", "");

                                params.put("szName", detail_pelanggan.namaoutlet.getText().toString());
                                params.put("szHierarchyId", detail_pelanggan.szHierarchyId);
                                params.put("szHierarchyFull", detail_pelanggan.szHierarchyFull);
                                params.put("szLocation", detail_pelanggan.depo);

                                if(restnomorbaru.equals("321") || restnomorbaru.equals("336") ||
                                        restnomorbaru.equals("324") || restnomorbaru.equals("317") ||
                                        restnomorbaru.equals("036")){
                                    params.put("szPt", "ASA");
                                } else {
                                    params.put("szPt", "TVIP");
                                }


                                params.put("nik_ktp", nomorktp.getText().toString());
                                params.put("dokumen_ktp",  nomorktp.getText().toString() + "_" + nik_baru + ".jpeg");

                                params.put("status_ktp", "1");
                                params.put("ket_ktp",  "Sudah Dilakukan upload KTP");


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(upload_ktp.this);
                        requestQueue2.add(stringRequest2);

                    }
                } else if(tidakbersedia.isChecked()){
                    if(editalasan.getText().toString().length() == 0){
                        editalasan.setError("Isi Alasan");
                    } else {
                        tanggal();
                        update_history2();
                        pDialog = new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setCancelable(false);
                        pDialog.setTitleText("Harap Menunggu");
                        pDialog.show();

                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/upload_dokumen_with_status_2.php",
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.cancel();
                                new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.ERROR_TYPE)
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

                                String rest = nik_baru;
                                String[] parts = rest.split("-");
                                String restnomor = parts[0];
                                String restnomorbaru = restnomor.replace(" ", "");

                                params.put("szName", detail_pelanggan.namaoutlet.getText().toString());
                                params.put("szHierarchyId", detail_pelanggan.szHierarchyId);
                                params.put("szHierarchyFull", detail_pelanggan.szHierarchyFull);
                                params.put("szLocation", detail_pelanggan.depo);

                                if(restnomorbaru.equals("321") || restnomorbaru.equals("336") ||
                                        restnomorbaru.equals("324") || restnomorbaru.equals("317") ||
                                        restnomorbaru.equals("036")){
                                    params.put("szPt", "ASA");
                                } else {
                                    params.put("szPt", "TVIP");
                                }

                                params.put("nik_ktp", "null");
                                params.put("dokumen_ktp",  "null");

                                params.put("status_ktp", "2");
                                params.put("ket_ktp",  editalasan.getText().toString());


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
                        RequestQueue requestQueue2 = Volley.newRequestQueue(upload_ktp.this);
                        requestQueue2.add(stringRequest2);
                    }
                }



            }
        });

    }

    private void update_history2() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/history_update.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(getBaseContext(), status_complete.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.ERROR_TYPE)
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
                String szId = sharedPreferences.getString("nik_baru", null);

                params.put("no_pelanggan", szId);
                params.put("alamat_pelanggan", detail_pelanggan.editalamatoutlet.getText().toString() + " RT" + detail_pelanggan.editRToutlet.getText().toString() + " /RW" + detail_pelanggan.editRWoutlet.getText().toString() + ", " + editkotaoutlet.getText().toString() + ", " + editkecamatanoutlet.getText().toString() + ", " + editkelurahanoutlet.getText().toString() + ", " + editKodePosoutlet.getText().toString());
                params.put("no_telepon", detail_pelanggan.editteleponoutlet.getText().toString());

                params.put("nik_ktp", "Tidak Ada KTP");
                params.put("npwp", "Tidak Ada Dokumen");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(upload_ktp.this);
        requestQueue2.add(stringRequest2);
    }

    private void tanggal() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_createjoinpelanggan",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

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
                String szId = sharedPreferences.getString("nik_baru", null);

                params.put("szId", szId);
                params.put("join_date", "");
                if(edittanggallahir.getText().toString().length() == 0){
                    params.put("tanggal_lahir", "");
                } else {
                    params.put("tanggal_lahir", convertFormat(edittanggallahir.getText().toString()));
                }

                params.put("jarak", "");
                params.put("bongkar_muat", "");


                params.put("nik", "");
                params.put("longitude", "");
                params.put("latitude", "");

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
        RequestQueue requestQueue2 = Volley.newRequestQueue(upload_ktp.this);
        requestQueue2.add(stringRequest2);
    }

    private void dms_update_ktp() {
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_UpdateKTP",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        upload_image_ktp();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        upload_image_ktp();
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
                                    Intent npwp = new Intent(getBaseContext(), upload_npwp.class);
                                    startActivity(npwp);

                                } else {
                                    update_history();
                                }
                            } else if (status.contains("Image not uploaded")){
                                pDialog.cancel();
                                new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.ERROR_TYPE)
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
                        new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.ERROR_TYPE)
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

        RequestQueue requestQueue2 = Volley.newRequestQueue(upload_ktp.this);
        requestQueue2.add(stringRequest2);
    }

    private void update_history() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/mobile_eis_2/history_update.php",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(getBaseContext(), status_complete.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                new SweetAlertDialog(upload_ktp.this, SweetAlertDialog.ERROR_TYPE)
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
                String szId = sharedPreferences.getString("nik_baru", null);

                params.put("no_pelanggan", szId);
                params.put("alamat_pelanggan", detail_pelanggan.editalamatoutlet.getText().toString() + " RT" + detail_pelanggan.editRToutlet.getText().toString() + " /RW" + detail_pelanggan.editRWoutlet.getText().toString() + ", " + editkotaoutlet.getText().toString() + ", " + editkecamatanoutlet.getText().toString() + ", " + editkelurahanoutlet.getText().toString() + ", " + editKodePosoutlet.getText().toString());
                params.put("no_telepon", detail_pelanggan.editteleponoutlet.getText().toString());

                params.put("nik_ktp", nomorktp.getText().toString());
                params.put("npwp", "Tidak Ada Dokumen");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(upload_ktp.this);
        requestQueue2.add(stringRequest2);
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