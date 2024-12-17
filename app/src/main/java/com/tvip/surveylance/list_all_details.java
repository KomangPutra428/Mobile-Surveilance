package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.tvip.surveylance.pojo.draftkompetitor_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class list_all_details extends AppCompatActivity {
    TextView tanggal_date;
    ListView list_date;
    List<draftkompetitor_pojo> draftkompetitorPojoList = new ArrayList<>();
    ListViewAdapterDraftKompetitor adapterDraftKompetitor;
    SweetAlertDialog pDialog;
    SharedPreferences sharedPreferences;
    RelativeLayout peringatan;
    Chip keterangan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_details);
        tanggal_date = findViewById(R.id.tanggal_date);
        list_date = findViewById(R.id.list_date);
        peringatan = findViewById(R.id.peringatan);
        keterangan = findViewById(R.id.keterangan);

        tanggal_date.setText(dateTime(getIntent().getStringExtra("submit_date")));
    }

    private void getDetail() {
        pDialog = new SweetAlertDialog(list_all_details.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();
        draftkompetitorPojoList.clear();
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_kompetitor_top?kode_pelanggan="+szId+"&tanggal="+convertFormat(getIntent().getStringExtra("submit_date")),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        peringatan.setVisibility(View.GONE);
                        list_date.setVisibility(View.VISIBLE);

                        int colorInt = getResources().getColor(R.color.background_green);
                        ColorStateList csl = ColorStateList.valueOf(colorInt);
                        int colorInt2 = getResources().getColor(R.color.border_green);
                        ColorStateList csl2 = ColorStateList.valueOf(colorInt2);
                        keterangan.setChipBackgroundColor(csl);
                        keterangan.setChipStrokeColor(csl2);
                        keterangan.setTextColor(Color.parseColor("#2ECC71"));

                        keterangan.setText("SELESAI");
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final draftkompetitor_pojo movieItem = new draftkompetitor_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("submit_date"),
                                        movieObject.getString("nik_survey"),
                                        movieObject.getString("kode_pelanggan"),
                                        movieObject.getString("nama_jenis"),
                                        movieObject.getString("nama_kiriman"),
                                        movieObject.getString("produk_lain"),
                                        movieObject.getString("kompetitor"),
                                        movieObject.getString("uom"),
                                        movieObject.getString("volume"),
                                        movieObject.getString("average_penjualan_before"),
                                        movieObject.getString("average_penjualan_now"),
                                        movieObject.getString("harga_beli"),
                                        movieObject.getString("harga_jual"),
                                        movieObject.getString("nama_program"),
                                        movieObject.getString("periode_program_awal"),
                                        movieObject.getString("periode_program_akhir"),
                                        movieObject.getString("mekanisme_program"),
                                        movieObject.getString("target_program"),
                                        movieObject.getString("keterangan"));
                                draftkompetitorPojoList.add(movieItem);

                            }
                            adapterDraftKompetitor = new ListViewAdapterDraftKompetitor(draftkompetitorPojoList, getApplicationContext());
                            list_date.setAdapter(adapterDraftKompetitor);
                            adapterDraftKompetitor.notifyDataSetChanged();
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
                        finish();
                        int colorInt = getResources().getColor(R.color.background_red);
                        ColorStateList csl = ColorStateList.valueOf(colorInt);

                        int colorInt2 = getResources().getColor(R.color.border_red);
                        ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                        keterangan.setChipBackgroundColor(csl);
                        keterangan.setChipStrokeColor(csl2);
                        keterangan.setTextColor(Color.parseColor("#FB4141"));
                        peringatan.setVisibility(View.VISIBLE);
                        list_date.setVisibility(View.GONE);
                        keterangan.setText("BELUM SELESAI");

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

    public class ListViewAdapterDraftKompetitor extends ArrayAdapter<draftkompetitor_pojo> {

        private class ViewHolder {
            TextView nama_produk;
            ListView sub_list;
            List<draftkompetitor_pojo> sub;
            ListViewAdapterSubDraft adapterSubDraft;

        }
        List<draftkompetitor_pojo> draftkompetitorPojoList;
        private Context context;

        public ListViewAdapterDraftKompetitor(List<draftkompetitor_pojo> draftkompetitorPojoList, Context context) {
            super(context, R.layout.list_item_saved_kompetitor, draftkompetitorPojoList);
            this.draftkompetitorPojoList = draftkompetitorPojoList;
            this.context = context;

        }

        public int getCount() {
            return draftkompetitorPojoList.size();
        }

        public draftkompetitor_pojo getItem(int position) {
            return draftkompetitorPojoList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            draftkompetitor_pojo draftkompetitor_pojo = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_saved_kompetitor, parent, false);
                viewHolder.nama_produk = (TextView) convertView.findViewById(R.id.nama_produk);
                viewHolder.sub_list = (ListView) convertView.findViewById(R.id.sub_list);

                viewHolder.sub = new ArrayList<>();
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String szId = sharedPreferences.getString("szId", null);
            viewHolder.sub.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://assessment.tvip.co.id/rest_server/kompetitor/aktivitas_kompetitor/index_kompetitor_sub?kode_pelanggan="+szId+"&tanggal=" + convertFormat(getIntent().getStringExtra("submit_date")) + "&nama_product=" + draftkompetitor_pojo.getKompetitor(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                final JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {
                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    final draftkompetitor_pojo movieItem = new draftkompetitor_pojo(
                                            movieObject.getString("id"),
                                            movieObject.getString("submit_date"),
                                            movieObject.getString("nik_survey"),
                                            movieObject.getString("kode_pelanggan"),
                                            movieObject.getString("nama_jenis"),
                                            movieObject.getString("nama_kiriman"),
                                            movieObject.getString("produk_lain"),
                                            movieObject.getString("kompetitor"),
                                            movieObject.getString("uom"),
                                            movieObject.getString("volume"),
                                            movieObject.getString("average_penjualan_before"),
                                            movieObject.getString("average_penjualan_now"),
                                            movieObject.getString("harga_beli"),
                                            movieObject.getString("harga_jual"),
                                            movieObject.getString("nama_program"),
                                            movieObject.getString("periode_program_awal"),
                                            movieObject.getString("periode_program_akhir"),
                                            movieObject.getString("mekanisme_program"),
                                            movieObject.getString("target_program"),
                                            movieObject.getString("keterangan"));
                                    viewHolder.sub.add(movieItem);

                                }

                                viewHolder.adapterSubDraft = new ListViewAdapterSubDraft(viewHolder.sub, getApplicationContext());
                                viewHolder.sub_list.setAdapter(viewHolder.adapterSubDraft);
                                viewHolder.adapterSubDraft.notifyDataSetChanged();

                                listadd.setListViewHeightBasedOnChildren(viewHolder.sub_list);
                            } catch(JSONException e){
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

            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
            RequestQueue requestQueue = Volley.newRequestQueue(list_all_details.this);
            requestQueue.add(stringRequest);

            viewHolder.nama_produk.setText(draftkompetitor_pojo.getKompetitor());



            return convertView;

        }
    }

    public class ListViewAdapterSubDraft extends ArrayAdapter<draftkompetitor_pojo> {

        private class ViewHolder {
            TextView volume, uom;
            TextView qty, harga;
            MaterialCardView carddetail;
            ImageView crate;
        }

        List<draftkompetitor_pojo> draftkompetitorPojoList;
        private Context context;

        public ListViewAdapterSubDraft(List<draftkompetitor_pojo> draftkompetitorPojoList, Context context) {
            super(context, R.layout.list_item_sub_saved, draftkompetitorPojoList);
            this.draftkompetitorPojoList = draftkompetitorPojoList;
            this.context = context;

        }

        public int getCount() {
            return draftkompetitorPojoList.size();
        }

        public draftkompetitor_pojo getItem(int position) {
            return draftkompetitorPojoList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            draftkompetitor_pojo draftkompetitor_pojo = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_item_sub_saved, parent, false);

                viewHolder.volume = (TextView) convertView.findViewById(R.id.volume);
                viewHolder.uom = (TextView) convertView.findViewById(R.id.uom);

                viewHolder.qty = (TextView) convertView.findViewById(R.id.qty);
                viewHolder.harga = (TextView) convertView.findViewById(R.id.harga);
                viewHolder.carddetail = (MaterialCardView) convertView.findViewById(R.id.carddetail);
                viewHolder.crate = (ImageView) convertView.findViewById(R.id.crate);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.carddetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = draftkompetitor_pojo.getId();
                    String submit_date = draftkompetitor_pojo.getSubmit_date();

                    String volume = draftkompetitor_pojo.getVolume();
                    String uom = draftkompetitor_pojo.getUom();

                    String namaproduk = draftkompetitor_pojo.getKompetitor();

                    String hargabeli = draftkompetitor_pojo.getHarga_beli();
                    String hargajual = draftkompetitor_pojo.getHarga_jual();

                    String bulanlalu = draftkompetitor_pojo.getAverage_penjualan_before();
                    String bulanini = draftkompetitor_pojo.getAverage_penjualan_now();

                    String namaprogram = draftkompetitor_pojo.getNama_program();
                    String periodeprogram = tanggal(draftkompetitor_pojo.getPeriode_program_awal()) + " s/d " + tanggal(draftkompetitor_pojo.getPeriode_program_akhir());

                    String mekanismeprogram = draftkompetitor_pojo.getMekanisme_program();
                    String targetprogram = draftkompetitor_pojo.getTarget_program();
                    String keterangan = draftkompetitor_pojo.getKeterangan();

                    String bulanawal = draftkompetitor_pojo.getPeriode_program_awal();
                    String bulan_akhir =  draftkompetitor_pojo.getPeriode_program_akhir();

                    String produklain =  draftkompetitor_pojo.getProduk_lain();

                    String pengirim =  draftkompetitor_pojo.getNama_jenis();
                    String namapengirim =  draftkompetitor_pojo.getNama_kiriman();



                    Intent intent = new Intent(getApplicationContext(), detail_kompetitor.class);
                    intent.putExtra("produklain", produklain);
                    intent.putExtra("pengirim", pengirim);
                    intent.putExtra("namapengirim", namapengirim);

                    intent.putExtra("bulanawal", bulanawal);
                    intent.putExtra("bulan_akhir", bulan_akhir);

                    intent.putExtra("id", id);
                    intent.putExtra("submit_date", submit_date);

                    intent.putExtra("volume", volume);
                    intent.putExtra("uom", uom);
                    intent.putExtra("hargabeli", hargabeli);
                    intent.putExtra("hargajual", hargajual);
                    intent.putExtra("bulanlalu", bulanlalu);
                    intent.putExtra("bulanini", bulanini);
                    intent.putExtra("namaprogram", namaprogram);
                    intent.putExtra("periodeprogram", periodeprogram);
                    intent.putExtra("mekanismeprogram", mekanismeprogram);
                    intent.putExtra("targetprogram", targetprogram);
                    intent.putExtra("keterangan", keterangan);
                    intent.putExtra("namaproduk", namaproduk);

                    startActivity(intent);
                }
            });
            int harga = Integer.parseInt(draftkompetitor_pojo.getHarga_beli());
            DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
            formatRp.setCurrencySymbol("Rp. ");
            formatRp.setMonetaryDecimalSeparator(',');
            formatRp.setGroupingSeparator('.');
            kursIndonesia.setDecimalFormatSymbols(formatRp);

            if(draftkompetitor_pojo.getUom().contains("Gallon")){
                viewHolder.volume.setText(draftkompetitor_pojo.getVolume() + "/Gallon");
                viewHolder.crate.setImageResource(R.drawable.ic_galon);
            } else {
                viewHolder.volume.setText(draftkompetitor_pojo.getVolume() + "/Box");
            }

            if(draftkompetitor_pojo.getProduk_lain().equals("") || draftkompetitor_pojo.getProduk_lain().equals("null")){
                viewHolder.uom.setText(draftkompetitor_pojo.getUom());
            } else {
                viewHolder.uom.setText(draftkompetitor_pojo.getProduk_lain() + " " + draftkompetitor_pojo.getUom());
            }

            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String average_penjualan = formatter.format(Integer.parseInt(draftkompetitor_pojo.getAverage_penjualan_now()));
            String penjualan = average_penjualan.replace(",", ".");

            viewHolder.qty.setText(penjualan + " Item");
            viewHolder.harga.setText(String.valueOf(kursIndonesia.format(harga)));

            return convertView;

        }
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public static String dateTime(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    public static String tanggal(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return convetDateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();
    }

}