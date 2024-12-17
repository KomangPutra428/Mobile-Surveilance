package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.tvip.surveylance.pojo.produk_lain_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class penjualan_produk_lain extends AppCompatActivity {
    EditText editcoverage, editrumahan, editkantoran, editoutlet;
    ImageButton minusrumahan, addrumahan, minuskantoran, addkantoran, minusoutlet, addoutlet;

    EditText editelpiji, editsembako, editamdk;
    CheckBox elpijicek, sembakocek, amdkcek;
    SweetAlertDialog pDialog;

    SharedPreferences sharedPreferences;

    ListView produklain;
    int count = 0;
    int count1 = 0;
    int count2 = 0;

    Button kembali, simpan;

    List<produk_lain_pojo> produk_lain_pojos = new ArrayList<>();
    ListViewAdapterProdukLain adapter;

    TextView tambah;

    RadioGroup buttonpersen;
    CheckBox produk1, produk2, produk3, produk4, produk5, produk6, produk7, produk8, produk9;
    CheckBox amdk1, amdk2, amdk3, amdk4, amdk5, amdk6, amdk7, amdk8;

    RadioButton persen1, persen2, persen3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_produk_lain);
        HttpsTrustManager.allowAllSSL();
        editcoverage = findViewById(R.id.editcoverage);
        editrumahan = findViewById(R.id.editrumahan);
        editkantoran = findViewById(R.id.editkantoran);
        editoutlet = findViewById(R.id.editoutlet);
        buttonpersen = findViewById(R.id.buttonpersen);

        persen1 = findViewById(R.id.persen1);
        persen2 = findViewById(R.id.persen2);
        persen3 = findViewById(R.id.persen3);

        produklain = findViewById(R.id.produklain);

        kembali = findViewById(R.id.kembali);
        simpan = findViewById(R.id.simpan);

        editelpiji = findViewById(R.id.editelpiji);
        editsembako = findViewById(R.id.editsembako);
        editamdk = findViewById(R.id.editamdk);

        editelpiji.addTextChangedListener(new NumberTextWatcher(editelpiji));
        editsembako.addTextChangedListener(new NumberTextWatcher(editsembako));
        editamdk.addTextChangedListener(new NumberTextWatcher(editamdk));

        elpijicek = findViewById(R.id.elpijicek);
        sembakocek = findViewById(R.id.sembakocek);
        amdkcek = findViewById(R.id.amdkcek);

        minusrumahan = findViewById(R.id.minusrumahan);
        addrumahan = findViewById(R.id.addrumahan);

        minuskantoran = findViewById(R.id.minuskantoran);
        addkantoran = findViewById(R.id.addkantoran);

        minusoutlet = findViewById(R.id.minusoutlet);
        addoutlet = findViewById(R.id.addoutlet);

        produk1 = findViewById(R.id.produk1);
        produk2 = findViewById(R.id.produk2);
        produk3 = findViewById(R.id.produk3);

        produk4 = findViewById(R.id.produk4);
        produk5 = findViewById(R.id.produk5);
        produk6 = findViewById(R.id.produk6);

        produk7 = findViewById(R.id.produk7);
        produk8 = findViewById(R.id.produk8);
        produk9 = findViewById(R.id.produk9);

        amdk1 = findViewById(R.id.amdk1);
        amdk2 = findViewById(R.id.amdk2);
        amdk3 = findViewById(R.id.amdk3);
        amdk4 = findViewById(R.id.amdk4);

        amdk5 = findViewById(R.id.amdk5);
        amdk6 = findViewById(R.id.amdk6);
        amdk7 = findViewById(R.id.amdk7);
        amdk8 = findViewById(R.id.amdk8);

        tambah = findViewById(R.id.tambah);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(penjualan_produk_lain.this);
                dialog.setContentView(R.layout.input_produk_lain);

                Button batal = dialog.findViewById(R.id.batal);
                Button tambahkan = dialog.findViewById(R.id.tambahkan);

                final EditText editnama = dialog.findViewById(R.id.editnama);
                final EditText editomset = dialog.findViewById(R.id.editomset);
                editomset.addTextChangedListener(new NumberTextWatcher(editomset));

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tambahkan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editnama.getText().toString().length() == 0){
                            editnama.setError("Isi Nama Produk");
                        } else if (editomset.getText().toString().length() == 0){
                            editomset.setError("Isi Omset/Bulan");
                        } else {
                            produk_lain_pojos.add(new produk_lain_pojo(editnama.getText().toString(), editomset.getText().toString()));
                            adapter = new ListViewAdapterProdukLain(produk_lain_pojos, penjualan_produk_lain.this);
                            produklain.setAdapter(adapter);
                            listchanger.setListViewHeightBasedOnChildren(produklain);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    }
                });

                dialog.show();
            }
        });

        editrumahan.setText("0");
        addrumahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                editrumahan.setText(String.valueOf(count));
            }

        });
        minusrumahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editrumahan.setText(String.valueOf(count));
                if (count == 0) {
                    return;
                }
                count--;
            }
        });

        editkantoran.setText("0");
        addkantoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count1++;
                editkantoran.setText(String.valueOf(count1));
            }

        });
        minuskantoran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editkantoran.setText(String.valueOf(count1));
                if (count1 == 0) {
                    return;
                }
                count1--;
            }
        });

        editoutlet.setText("0");
        addoutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count2++;
                editoutlet.setText(String.valueOf(count2));
            }

        });
        minusoutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editoutlet.setText(String.valueOf(count2));
                if (count2 == 0) {
                    return;
                }
                count2--;
            }
        });

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editcoverage.getText().toString().length() ==0){
                    editcoverage.setError("Isi Area Coverage");
                } else if (buttonpersen.getCheckedRadioButtonId() == -1){
                    new SweetAlertDialog(penjualan_produk_lain.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Kontribusi Penjualan AMDK")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                } else if (!produk1.isChecked() && !produk2.isChecked() && !produk3.isChecked() &&
                        !produk4.isChecked() && !produk5.isChecked() && !produk6.isChecked() &&
                        !produk7.isChecked() && !produk8.isChecked() && !produk9.isChecked()) {
                    new SweetAlertDialog(penjualan_produk_lain.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Pilih Produk")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (!amdk1.isChecked() && !amdk2.isChecked() && !amdk3.isChecked() &&
                        !amdk4.isChecked() && !amdk5.isChecked() && !amdk6.isChecked() &&
                        !amdk7.isChecked() && !amdk8.isChecked()) {
                    new SweetAlertDialog(penjualan_produk_lain.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Merek AMDK")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    coverage();
                }
            }
        });
    }

    private void coverage() {
        pDialog = new SweetAlertDialog(penjualan_produk_lain.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_coverage",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        rumahan();
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
                params.put("area", editcoverage.getText().toString());
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
        requestQueue2.add(stringRequest2);
    }

    private void rumahan() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_jenispelanggan",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        kantoran();
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
                params.put("jenis_pelanggan", "1");
                params.put("total", editrumahan.getText().toString());
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
        requestQueue2.add(stringRequest2);
    }

    private void kantoran() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_jenispelanggan",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        outlet();
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
                params.put("jenis_pelanggan", "2");
                params.put("total", editkantoran.getText().toString());
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
        requestQueue2.add(stringRequest2);
    }

    private void outlet() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_jenispelanggan",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        gaselpiji();
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
                params.put("jenis_pelanggan", "3");
                params.put("total", editoutlet.getText().toString());
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
        requestQueue2.add(stringRequest2);
    }

    private void gaselpiji() {
        if(elpijicek.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produklain",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            sembako();
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
                    String restnomorbaru = editelpiji.getText().toString().replace(" ", "");
                    String removetitik = restnomorbaru.replace(".", "");

                    params.put("szId", szId);
                    params.put("produk", elpijicek.getText().toString());
                    params.put("omset", removetitik);
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        } else {
            sembako();
        }
    }

    private void sembako() {
        if(sembakocek.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produklain",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdk();
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
                    String restnomorbaru = editsembako.getText().toString().replace(" ", "");
                    String removetitik = restnomorbaru.replace(".", "");

                    params.put("szId", szId);
                    params.put("produk", sembakocek.getText().toString());
                    params.put("omset", removetitik);
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        } else {
            amdk();
        }

    }

    private void amdk() {
        if(amdkcek.isChecked()){
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produklain",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            postlist();
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
                    String restnomorbaru = editamdk.getText().toString().replace(" ", "");
                    String removetitik = restnomorbaru.replace(".", "");

                    params.put("szId", szId);
                    params.put("produk", amdkcek.getText().toString());
                    params.put("omset", removetitik);
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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        } else {
            postlist();
        }
    }

    private void postlist() {
        if(produk_lain_pojos.size() == 0){
            kontribusi();
        } else {
            for (int i = 0; i <= produk_lain_pojos.size() - 1; i++) {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int finalI = i;
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_produklain",
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                if (finalI == produk_lain_pojos.size() - 1) {
                                    kontribusi();
                                }
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
                        String restnomorbaru = adapter.getItem(finalI).getOmset().replace(" ", "");
                        String removetitik = restnomorbaru.replace(".", "");
                        params.put("szId", szId);
                        params.put("produk", adapter.getItem(finalI).getProduk());
                        params.put("omset", removetitik);
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
                RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
                requestQueue2.add(stringRequest2);
            }
        }

    }

    private void kontribusi() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Kontribusi",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        produktoko1();
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

                if(persen1.isChecked()){
                    params.put("persentase", "1");
                } else if(persen2.isChecked()){
                    params.put("persentase", "2");
                } else if(persen3.isChecked()){
                    params.put("persentase", "3");
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
        RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
        requestQueue2.add(stringRequest2);
    }

    private void produktoko1() {
        if(!produk1.isChecked()){
            produktoko2();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko2();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "1");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko2() {
        if(!produk2.isChecked()){
            produktoko3();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko3();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "2");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko3() {
        if(!produk3.isChecked()){
            produktoko4();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko4();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "3");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko4() {
        if(!produk4.isChecked()){
            produktoko5();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko5();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "4");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko5() {
        if(!produk5.isChecked()){
            produktoko6();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko6();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "5");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko6() {
        if(!produk6.isChecked()){
            produktoko7();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko7();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "6");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko7() {
        if(!produk7.isChecked()){
            produktoko8();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko8();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "7");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko8() {
        if(!produk8.isChecked()){
            produktoko9();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            produktoko9();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "8");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void produktoko9() {
        if(!produk9.isChecked()){
            amdkpost1();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_PelangganProduk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost1();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("produk", "9");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost1() {
        if(!amdk1.isChecked()){
            amdkpost2();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost2();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "1");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost2() {
        if(!amdk2.isChecked()){
            amdkpost3();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost3();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "2");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost3() {
        if(!amdk3.isChecked()){
            amdkpost4();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost4();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "3");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost4() {
        if(!amdk4.isChecked()){
            amdkpost5();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost5();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "4");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost5() {
        if(!amdk5.isChecked()){
            amdkpost6();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost6();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "5");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost6() {
        if(!amdk6.isChecked()){
            amdkpost7();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost7();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "6");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost7() {
        if(!amdk7.isChecked()){
            amdkpost8();
        } else {
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            amdkpost8();
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "7");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void amdkpost8() {
        if(!amdk8.isChecked()){
            pDialog.cancel();
            SweetAlertDialog success = new SweetAlertDialog(penjualan_produk_lain.this, SweetAlertDialog.SUCCESS_TYPE);
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
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://hrd.tvip.co.id/rest_server_survey/utilitas/Customer/index_Amdk",
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            pDialog.cancel();
                            SweetAlertDialog success = new SweetAlertDialog(penjualan_produk_lain.this, SweetAlertDialog.SUCCESS_TYPE);
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
                    int a = buttonpersen.getCheckedRadioButtonId();

                    params.put("szId", szId);
                    params.put("amdk", "8");

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
            RequestQueue requestQueue2 = Volley.newRequestQueue(penjualan_produk_lain.this);
            requestQueue2.add(stringRequest2);
        }
    }


    public static class ListViewAdapterProdukLain extends ArrayAdapter<produk_lain_pojo> {

        private class ViewHolder {
            EditText editlainnya, editrp;

        }
        List<produk_lain_pojo> produk_lain_pojos;
        private Context context;

        public ListViewAdapterProdukLain(List<produk_lain_pojo> produk_lain_pojos, Context context) {
            super(context, R.layout.list_item_produk_lain, produk_lain_pojos);
            this.produk_lain_pojos = produk_lain_pojos;
            this.context = context;

        }

        public int getCount() {
            return produk_lain_pojos.size();
        }

        public produk_lain_pojo getItem(int position) {
            return produk_lain_pojos.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final produk_lain_pojo produk_lain_pojo = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_produk_lain, parent, false);

                viewHolder.editlainnya = (EditText) convertView.findViewById(R.id.editlainnya);
                viewHolder.editrp = (EditText) convertView.findViewById(R.id.editrp);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.editlainnya.setText(produk_lain_pojo.getProduk());
            viewHolder.editrp.setText(produk_lain_pojo.getOmset());

            viewHolder.editrp.addTextChangedListener(new NumberTextWatcher(viewHolder.editrp));

            viewHolder.editlainnya.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    getItem(position).setProduk(viewHolder.editlainnya.getText().toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getItem(position).setProduk(viewHolder.editlainnya.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    getItem(position).setProduk(viewHolder.editlainnya.getText().toString());
                }
            });

            viewHolder.editrp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    getItem(position).setOmset(viewHolder.editrp.getText().toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    getItem(position).setOmset(viewHolder.editrp.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    getItem(position).setOmset(viewHolder.editrp.getText().toString());
                }
            });


            return convertView;

        }
    }
}