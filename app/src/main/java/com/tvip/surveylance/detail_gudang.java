package com.tvip.surveylance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detail_gudang extends AppCompatActivity {
    TextView idpelanggan, namaoutlet, segment, pemilik, rtrw, disarankan, parkir, alamatdiganti;
    SharedPreferences sharedPreferences;
    TextView kota;
    TextView kecamatan;
    TextView kelurahan;
    TextView kodepos;
    static TextView telepon;
    static TextView alamatlengkap;
    static TextView idktp, idnpwp;
    ListView chillerlist, lokasilist, orderlist;
    ArrayList<String> chillerarray = new ArrayList<>();
    ArrayList<String> lokasioutlet = new ArrayList<>();
    ArrayList<String> orderarray = new ArrayList<>();

    TextView kepemilikan, luas, panjang, lebar, galon, sps, botol, kondisi, karyawan;

    static String longitude, latitude;
    SweetAlertDialog pDialog;
    MaterialCardView ktp, npwp, detail;
    LinearLayout update;

    TextView langganan, jarak, catatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gudang);
        HttpsTrustManager.allowAllSSL();

        chillerlist = findViewById(R.id.chillerlist);
        alamatdiganti = findViewById(R.id.alamatdiganti);

        langganan = findViewById(R.id.langganan);
        jarak = findViewById(R.id.jarak);
        catatan = findViewById(R.id.catatan);
        parkir = findViewById(R.id.parkir);
        lokasilist = findViewById(R.id.lokasilist);
        orderlist = findViewById(R.id.orderlist);

        getParkir();
        getChiller();
        getLokasiOutlet();
        getOrder();

        karyawan = findViewById(R.id.karyawan);

        disarankan = findViewById(R.id.disarankan);

        kepemilikan = findViewById(R.id.kepemilikan);
        luas = findViewById(R.id.luas);
        panjang = findViewById(R.id.panjang);

        lebar = findViewById(R.id.lebar);
        galon = findViewById(R.id.galon);
        sps = findViewById(R.id.sps);

        botol = findViewById(R.id.botol);
        kondisi =  findViewById(R.id.kondisi);

        idktp = findViewById(R.id.idktp);
        idnpwp = findViewById(R.id.idnpwp);

        ktp = findViewById(R.id.ktp);
        npwp = findViewById(R.id.npwp);
        update = findViewById(R.id.update);

        update.setVisibility(View.GONE);

        detail = findViewById(R.id.detail);

        idpelanggan = findViewById(R.id.idpelanggan);
        namaoutlet = findViewById(R.id.namaoutlet);
        segment = findViewById(R.id.segment);
        pemilik = findViewById(R.id.pemilik);

        kota = findViewById(R.id.kota);
        kecamatan = findViewById(R.id.kecamatan);
        kelurahan = findViewById(R.id.kelurahan);
        kodepos = findViewById(R.id.kodepos);
        telepon = findViewById(R.id.telepon);
        alamatlengkap = findViewById(R.id.alamatlengkap);
        rtrw = findViewById(R.id.rtrw);

        rtrw.setText("-");
        getNote();
        kepemilikan.setText(getIntent().getStringExtra("kepemilikan"));
        luas.setText(getIntent().getStringExtra("luas") + " M");
        panjang.setText(getIntent().getStringExtra("panjang") + " M");
        lebar.setText(getIntent().getStringExtra("lebar") + " M");
        galon.setText(getIntent().getStringExtra("galon") + " Galon");
        sps.setText(getIntent().getStringExtra("sps")+ " Box");
        botol.setText(getIntent().getStringExtra("botol") + " Galon");

        if(getIntent().getStringExtra("condition").equals("1")){
            kondisi.setText("Layak / ada pest control");
        } else if(getIntent().getStringExtra("condition").equals("2")){
            kondisi.setText("Layak/ kurang bersih, tidak ada pest control");
        } else {
            kondisi.setText("Tidak Layak/ kotor, tdk ada pest control");
        }

        getBerlangganan();
        getKaryawan();
        getDisarankan();

        ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idktp.getText().toString().equals("Tidak Ada Dokumen")){
                    new SweetAlertDialog(detail_gudang.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Dokumen Tidak Ada")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    Intent intent = new Intent(getBaseContext(), tampilandokumen.class);
                    intent.putExtra("Jenis", "KTP");
                    intent.putExtra("id", idktp.getText().toString());

                    startActivity(intent);
                }

            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), e_card.class);
                startActivity(intent);
            }
        });

        npwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idnpwp.getText().toString().equals("Tidak Ada Dokumen")){
                    Intent intent = new Intent(getBaseContext(), update_npwp.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getBaseContext(), tampilandokumen.class);
                    intent.putExtra("Jenis", "NPWP");
                    intent.putExtra("id", idnpwp.getText().toString());

                    startActivity(intent);
                }

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), update_pelanggan.class);
                startActivity(intent);


            }
        });

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szId", null);
        String rest = nik_baru;
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        String restnomorbaru = restnomor.replace(" ", "");

        pDialog = new SweetAlertDialog(detail_gudang.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_login?szId=" + nik_baru + "&kode_dms=" + restnomorbaru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    idpelanggan.setText(movieObject.getString("szId"));
                                    namaoutlet.setText(movieObject.getString("szName"));
                                    if(movieObject.getString("szHierarchyId").equals("")){
                                        segment.setText("-");
                                    } else {
                                        segment.setText(movieObject.getString("szHierarchyId"));
                                    }

                                    if(movieObject.getString("szContactPerson1").equals("")){
                                        pemilik.setText("-");
                                    } else {
                                        pemilik.setText(movieObject.getString("szContactPerson1"));
                                    }
                                    kota.setText(movieObject.getString("szCity"));
                                    kecamatan.setText(movieObject.getString("szDistrict"));
                                    kelurahan.setText(movieObject.getString("szSubDistrict"));
                                    kodepos.setText(movieObject.getString("szZipCode"));
                                    telepon.setText(movieObject.getString("szPhone1"));

                                    longitude = movieObject.getString("szLongitude");
                                    latitude = movieObject.getString("szLatitude");



                                    pDialog.dismissWithAnimation();

                                    alamatlengkap.setText(movieObject.getString("szAddress"));

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
        RequestQueue requestQueue = Volley.newRequestQueue(detail_gudang.this);
        requestQueue.add(stringRequest);

    }

    private void getOrder() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_MendapatkanOrder?szId=" + szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String city = jsonObject1.getString("jenis_order");
                                    orderarray.add(city);

                                }
                            }
                            orderlist.setAdapter(new ArrayAdapter<String>(detail_gudang.this, R.layout.simple_list_item, R.id.text, orderarray));
                            listchanger.setListViewHeightBasedOnChildren(orderlist);
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
        kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(detail_gudang.this);
        requestkota.add(kecamatan);
    }

    private void getLokasiOutlet() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_LokasiOutlet?szId=" + szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String city = jsonObject1.getString("jenis_lokasi");
                                    lokasioutlet.add(city);

                                }
                            }
                            lokasilist.setAdapter(new ArrayAdapter<String>(detail_gudang.this, R.layout.simple_list_item, R.id.text, lokasioutlet));
                            listchanger.setListViewHeightBasedOnChildren(lokasilist);
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
        kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(detail_gudang.this);
        requestkota.add(kecamatan);
    }

    private void getChiller() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_chiller?szId=" + szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String city = jsonObject1.getString("jenis_alat_pendingin");
                                    chillerarray.add(city);

                                }
                            }
                            chillerlist.setAdapter(new ArrayAdapter<String>(detail_gudang.this, R.layout.simple_list_item, R.id.text, chillerarray));
                            listchanger.setListViewHeightBasedOnChildren(chillerlist);
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
        kecamatan.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestkota = Volley.newRequestQueue(detail_gudang.this);
        requestkota.add(kecamatan);
    }

    private void getParkir() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_parkir?szId=" + szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    parkir.setText(movieObject.getString("jenis_area"));


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
        RequestQueue requestQueue = Volley.newRequestQueue(detail_gudang.this);
        requestQueue.add(stringRequest);
    }

    private void getDisarankan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_segment?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                if(movieObject.getString("jenis_outlet").equals("1")) {
                                    disarankan.setText("STAR OUTLET");
                                } else if(movieObject.getString("jenis_outlet").equals("2")) {
                                    disarankan.setText("WHOLESALER");
                                } else if(movieObject.getString("jenis_outlet").equals("3")){
                                    disarankan.setText("MANAGE OUTLET");
                                } else if(movieObject.getString("jenis_outlet").equals("4")){
                                    disarankan.setText("AHS");
                                }



                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        disarankan.setText("-");
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
        RequestQueue requestQueue = Volley.newRequestQueue(detail_gudang.this);
        requestQueue.add(stringRequest);
    }


    private void getKaryawan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_jumlah_pelanggan?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                pDialog.dismissWithAnimation();

                                karyawan.setText(movieObject.getString("jml_karyawan"));


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
        RequestQueue requestQueue = Volley.newRequestQueue(detail_gudang.this);
        requestQueue.add(stringRequest);
    }

    private void getBerlangganan() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_berlangganan?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                pDialog.dismissWithAnimation();

                                langganan.setText(convertFormat(movieObject.getString("join_date")));
                                jarak.setText(movieObject.getString("jarak") + " KM");

                                if(movieObject.getString("bongkar_muat").equals("ya")){
                                    catatan.setText("Bersedia bongkar muat malam hari");
                                } else {
                                    catatan.setText("Belum Bersedia bongkar muat malam hari");
                                }

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
        RequestQueue requestQueue = Volley.newRequestQueue(detail_gudang.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szId", null);
        StringRequest kota = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/mobile_eis_2/file_perid.php?dokumen_npwp="+nik_baru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (String.valueOf(jsonArray).equals("[]")){
                                idktp.setText("Tidak Ada Dokumen");
                                idnpwp.setText("Tidak Ada Dokumen");
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    idktp.setText(jsonObject1.getString("nik_ktp"));
                                    idnpwp.setText(jsonObject1.getString("npwp"));

                                    if(jsonObject1.getString("nik_ktp").equals("null")){
                                        idktp.setText("Tidak Ada Dokumen");
                                    } else {
                                        idktp.setText(jsonObject1.getString("nik_ktp"));
                                    }

                                    if(jsonObject1.getString("npwp").equals("null")){
                                        idnpwp.setText("Tidak Ada Dokumen");
                                    } else {
                                        idnpwp.setText(jsonObject1.getString("npwp"));
                                    }

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
        RequestQueue requestkota = Volley.newRequestQueue(this);
        requestkota.add(kota);
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    private void getNote() {
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_history?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                alamatdiganti.setText(movieObject.getString("szAddress"));

                            }


                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        disarankan.setText("-");
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
        RequestQueue requestQueue = Volley.newRequestQueue(detail_gudang.this);
        requestQueue.add(stringRequest);
    }
}