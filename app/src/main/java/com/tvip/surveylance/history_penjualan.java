package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.android.material.tabs.TabLayout;
import com.tvip.surveylance.pojo.kompetitor_pojo;
import com.tvip.surveylance.pojo.produkkami_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class history_penjualan extends AppCompatActivity {
    TabLayout tablayout;
    TextView tambah;
    ListView produkkami, kompetitor;
    String a;
    final ArrayList<String> produk = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private List<produkkami_pojo> produkkami_pojos = new ArrayList<>();
    private List<kompetitor_pojo> kompetitor_pojos = new ArrayList<>();
    ListViewAdapterprodukkami adapter;
    ListViewAdapterkompetitor adapter2;
    SweetAlertDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_penjualan);
        HttpsTrustManager.allowAllSSL();
        tablayout = findViewById(R.id.tablayout);
        tambah = findViewById(R.id.tambah);
        produkkami = findViewById(R.id.produkkami);
        kompetitor = findViewById(R.id.kompetitor);
        a = "0";

        produkkami.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialog = new Dialog(history_penjualan.this);
                dialog.setContentView(R.layout.produkkami);
                final AutoCompleteTextView editprodukkami = dialog.findViewById(R.id.editprodukkami);

                ImageButton minusprodukkami = dialog.findViewById(R.id.minusprodukkami);
                final EditText editproduk = dialog.findViewById(R.id.editproduk);
                ImageButton addprodukkami = dialog.findViewById(R.id.addprodukkami);

                editprodukkami.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            editprodukkami.showDropDown();
                        }
                    }
                });
                editprodukkami.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        editprodukkami.showDropDown();
                        return false;
                    }
                });

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                tambahkan.setText("Simpan");

                StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_product",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("status").equals("true")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            String szId = jsonObject1.getString("szName");
                                            String szName = jsonObject1.getString("szId");

                                            produk.add( szId +  " (" + szName + ")" );

                                        }
                                    }
                                    editprodukkami.setAdapter(new ArrayAdapter<String>(history_penjualan.this, android.R.layout.simple_expandable_list_item_1, produk));
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
                RequestQueue requestkota = Volley.newRequestQueue(history_penjualan.this);
                requestkota.add(kecamatan);

                final int[] count = new int[1];

                editproduk.setText("0");
                addprodukkami.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count[0]++;
                        editproduk.setText(String.valueOf(count[0]));
                    }

                });
                minusprodukkami.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editproduk.setText(String.valueOf(count[0]));
                        if (count[0] == 0) {
                            return;
                        }
                        count[0]--;
                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editproduk.getText().toString().length() == 0){
                            editproduk.setError("Isi Produk");
                        } else if(editproduk.getText().toString().length() == 0){
                            editproduk.setError("Isi Satuan");
                        } else {
                            pDialog = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setCancelable(false);
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.show();
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produkini",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.cancel();
                                            SweetAlertDialog success = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            success.setContentText("Berhasil disimpan");
                                            success.setCancelable(false);
                                            success.setConfirmText("OK");
                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    dialog.dismiss();
                                                    produkpabrik();
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
                                    String id = editprodukkami.getText().toString();
                                    String[] splited_text = id.split(" \\(");
                                    id = splited_text[0];

                                    String produk = editprodukkami.getText().toString();
                                    String[] splited_text2 = produk.split(" \\(");
                                    produk = splited_text2[1];
                                    String restnomorbaru = produk.replace(")", "");

                                    params.put("id", adapter.getItem(position).getId());
                                    params.put("sku", restnomorbaru);
                                    params.put("nama_produk", id);
                                    params.put("qty", editproduk.getText().toString());

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(history_penjualan.this);
                            requestQueue2.add(stringRequest2);
                        }
                    }
                });

                dialog.show();


            }
        });

        kompetitor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final Dialog dialog = new Dialog(history_penjualan.this);
                dialog.setContentView(R.layout.produklain);
                final EditText editproduk = dialog.findViewById(R.id.editproduk);
                final EditText editukuran = dialog.findViewById(R.id.editukuran);

                ImageButton minus = dialog.findViewById(R.id.minus);
                final EditText edit = dialog.findViewById(R.id.edit);
                ImageButton add = dialog.findViewById(R.id.add);

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);
                tambahkan.setText("Simpan");
                final int[] count2 = new int[1];

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editproduk.getText().toString().length() == 0){
                            editproduk.setError("Isi Produk");
                        } else if(editukuran.getText().toString().length() == 0){
                            editukuran.setError("Isi Satuan");
                        } else {
                            pDialog = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setCancelable(false);
                            pDialog.setTitleText("Harap Menunggu");
                            pDialog.show();
                            StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produkkompetitor",
                                    new Response.Listener<String>() {

                                        @Override
                                        public void onResponse(String response) {
                                            pDialog.cancel();
                                            SweetAlertDialog success = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.SUCCESS_TYPE);
                                            success.setContentText("Berhasil disimpan");
                                            success.setCancelable(false);
                                            success.setConfirmText("OK");
                                            success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    dialog.dismiss();
                                                    produkkompetitor();
                                                    pDialog = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                    pDialog.setCancelable(false);
                                                    pDialog.setTitleText("Harap Menunggu");
                                                    pDialog.show();
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

                                    params.put("id", adapter2.getItem(position).getId());
                                    params.put("product", editproduk.getText().toString());
                                    params.put("keterangan", editukuran.getText().toString());

                                    params.put("qty", edit.getText().toString());

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
                            RequestQueue requestQueue2 = Volley.newRequestQueue(history_penjualan.this);
                            requestQueue2.add(stringRequest2);
                        }
                    }
                });

                edit.setText("0");
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count2[0]++;
                        edit.setText(String.valueOf(count2[0]));
                    }

                });
                minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edit.setText(String.valueOf(count2[0]));
                        if (count2[0] == 0) {
                            return;
                        }
                        count2[0]--;
                    }
                });
                dialog.show();

            }
        });
        
        produkpabrik();
        produkkompetitor();


        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a.equals("0")){
                    final Dialog dialog = new Dialog(history_penjualan.this);
                    dialog.setContentView(R.layout.produkkami);
                    final AutoCompleteTextView editprodukkami = dialog.findViewById(R.id.editprodukkami);

                    ImageButton minusprodukkami = dialog.findViewById(R.id.minusprodukkami);
                    final EditText editproduk = dialog.findViewById(R.id.editproduk);
                    ImageButton addprodukkami = dialog.findViewById(R.id.addprodukkami);

                    editprodukkami.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){
                                editprodukkami.showDropDown();
                            }
                        }
                    });
                    editprodukkami.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            editprodukkami.showDropDown();
                            return false;
                        }
                    });

                    Button batal = dialog.findViewById(R.id.batal);
                    Button tambahkan = dialog.findViewById(R.id.tambahkan);

                    StringRequest kecamatan = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_product",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.getString("status").equals("true")) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                                String szId = jsonObject1.getString("szName");
                                                String szName = jsonObject1.getString("szId");

                                                produk.add( szId +  " (" + szName + ")" );

                                            }
                                        }
                                        editprodukkami.setAdapter(new ArrayAdapter<String>(history_penjualan.this, android.R.layout.simple_expandable_list_item_1, produk));
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
                    RequestQueue requestkota = Volley.newRequestQueue(history_penjualan.this);
                    requestkota.add(kecamatan);

                    final int[] count = new int[1];

                    editproduk.setText("0");
                    addprodukkami.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count[0]++;
                            editproduk.setText(String.valueOf(count[0]));
                        }

                    });
                    minusprodukkami.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editproduk.setText(String.valueOf(count[0]));
                            if (count[0] == 0) {
                                return;
                            }
                            count[0]--;
                        }
                    });

                    batal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    tambahkan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(editprodukkami.getText().toString().length() == 0){
                                editprodukkami.setError("Isi Produk");
                            } else {
                                final SweetAlertDialog pDialog = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setCancelable(false);
                                pDialog.setTitleText("Harap Menunggu");
                                pDialog.show();
                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produk",
                                        new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                pDialog.cancel();
                                                SweetAlertDialog success = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                success.setContentText("Berhasil disimpan");
                                                success.setCancelable(false);
                                                success.setConfirmText("OK");
                                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        dialog.dismiss();
                                                        produkpabrik();
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

                                        String id = editprodukkami.getText().toString();
                                        String[] splited_text = id.split(" \\(");
                                        id = splited_text[0];

                                        String produk = editprodukkami.getText().toString();
                                        String[] splited_text2 = produk.split(" \\(");
                                        produk = splited_text2[1];
                                        String restnomorbaru = produk.replace(")", "");

                                        params.put("szId", szId);
                                        params.put("sku", restnomorbaru);
                                        params.put("nama_produk", id);

                                        params.put("qty", editproduk.getText().toString());
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
                                RequestQueue requestQueue2 = Volley.newRequestQueue(history_penjualan.this);
                                requestQueue2.add(stringRequest2);
                            }
                        }
                    });
                    dialog.show();
                } else {
                    final Dialog dialog = new Dialog(history_penjualan.this);
                    dialog.setContentView(R.layout.produklain);
                    final EditText editproduk = dialog.findViewById(R.id.editproduk);
                    final EditText editukuran = dialog.findViewById(R.id.editukuran);

                    ImageButton minus = dialog.findViewById(R.id.minus);
                    final EditText edit = dialog.findViewById(R.id.edit);
                    ImageButton add = dialog.findViewById(R.id.add);

                    Button batal = dialog.findViewById(R.id.batal);
                    Button tambahkan = dialog.findViewById(R.id.tambahkan);

                    final int[] count2 = new int[1];

                    batal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    tambahkan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(editproduk.getText().toString().length() == 0){
                                editproduk.setError("Isi Produk");
                            } else if(editukuran.getText().toString().length() == 0){
                                editukuran.setError("Isi Satuan");
                            } else {
                                pDialog = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setCancelable(false);
                                pDialog.setTitleText("Harap Menunggu");
                                pDialog.show();
                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produkkompetitor",
                                        new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                pDialog.cancel();
                                                SweetAlertDialog success = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.SUCCESS_TYPE);
                                                success.setContentText("Berhasil disimpan");
                                                success.setCancelable(false);
                                                success.setConfirmText("OK");
                                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                        dialog.dismiss();
                                                        produkkompetitor();
                                                        pDialog = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
                                                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                                        pDialog.setCancelable(false);
                                                        pDialog.setTitleText("Harap Menunggu");
                                                        pDialog.show();
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
                                        params.put("product", editproduk.getText().toString());
                                        params.put("keterangan", editukuran.getText().toString());

                                        params.put("qty", edit.getText().toString());

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
                                RequestQueue requestQueue2 = Volley.newRequestQueue(history_penjualan.this);
                                requestQueue2.add(stringRequest2);
                            }
                        }
                    });

                    edit.setText("0");
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            count2[0]++;
                            edit.setText(String.valueOf(count2[0]));
                        }

                    });
                    minus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            edit.setText(String.valueOf(count2[0]));
                            if (count2[0] == 0) {
                                return;
                            }
                            count2[0]--;
                        }
                    });
                    dialog.show();
                }
            }
        });
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                a = String.valueOf(position);
                if(position == 1){
                    kompetitor.setVisibility(View.VISIBLE);
                    produkkami.setVisibility(View.GONE);
                } else {
                    kompetitor.setVisibility(View.GONE);
                    produkkami.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                a = String.valueOf(position);
                if(position == 1){
                    kompetitor.setVisibility(View.VISIBLE);
                    produkkami.setVisibility(View.GONE);
                } else {
                    kompetitor.setVisibility(View.GONE);
                    produkkami.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                a = String.valueOf(position);
                if(position == 1){
                    kompetitor.setVisibility(View.VISIBLE);
                    produkkami.setVisibility(View.GONE);
                } else {
                    kompetitor.setVisibility(View.GONE);
                    produkkami.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void produkkompetitor() {
        kompetitor_pojos.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_kompetitor?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final kompetitor_pojo movieItem = new kompetitor_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("product"),
                                        movieObject.getString("keterangan"),
                                        movieObject.getString("qty"));
                                kompetitor_pojos.add(movieItem);

                            }
                            adapter2 = new ListViewAdapterkompetitor(kompetitor_pojos, getApplicationContext());
                            kompetitor.setAdapter(adapter2);
                            adapter2.notifyDataSetChanged();
                            pDialog.dismissWithAnimation();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void produkpabrik() {
        produkkami_pojos.clear();
        pDialog = new SweetAlertDialog(history_penjualan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produk?szId="+ szId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final produkkami_pojo movieItem = new produkkami_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("nama_produk"),
                                        movieObject.getString("qty"));
                                produkkami_pojos.add(movieItem);

                            }
                            adapter = new ListViewAdapterprodukkami(produkkami_pojos, getApplicationContext());
                            produkkami.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            pDialog.dismissWithAnimation();

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static class ListViewAdapterprodukkami extends ArrayAdapter<produkkami_pojo> {

        List<produkkami_pojo> produkkami_pojos;

        private Context context;

        public ListViewAdapterprodukkami(List<produkkami_pojo> produkkami_pojos, Context context) {
            super(context, R.layout.list_item_pelanggan, produkkami_pojos);
            this.produkkami_pojos = produkkami_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_pelanggan, null, true);


            TextView namaproduk = listViewItem.findViewById(R.id.namaproduk);
            TextView qty = listViewItem.findViewById(R.id.qty);
            TextView ubah = listViewItem.findViewById(R.id.ubah);

            ubah.setVisibility(View.VISIBLE);
            produkkami_pojo produkkami_pojo = getItem(position);

            namaproduk.setText(produkkami_pojo.getSku());
            qty.setText(produkkami_pojo.getQty() + " Qty");


            return listViewItem;
        }
    }

    public static class ListViewAdapterkompetitor extends ArrayAdapter<kompetitor_pojo> {

        List<kompetitor_pojo> kompetitor_pojos;

        private Context context;

        public ListViewAdapterkompetitor(List<kompetitor_pojo> kompetitor_pojos, Context context) {
            super(context, R.layout.list_item_produk, kompetitor_pojos);
            this.kompetitor_pojos = kompetitor_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_produk, null, true);


            TextView namaproduk = listViewItem.findViewById(R.id.namaproduk);
            TextView qty = listViewItem.findViewById(R.id.qty);
            RelativeLayout textproduk = listViewItem.findViewById(R.id.textproduk);
            TextView ukuran = listViewItem.findViewById(R.id.ukuran);
            TextView ubah = listViewItem.findViewById(R.id.ubah);

            ubah.setVisibility(View.VISIBLE);
            kompetitor_pojo kompetitor_pojo = getItem(position);

            namaproduk.setText(kompetitor_pojo.getProduct().toUpperCase());
            qty.setText(kompetitor_pojo.getQty() + " Qty");
            ukuran.setText(kompetitor_pojo.getKeterangan().toUpperCase());
            textproduk.setBackgroundColor(Color.parseColor("#FB4141"));

            return listViewItem;
        }
    }
}