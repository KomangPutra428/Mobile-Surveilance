package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.tvip.surveylance.pojo.memento_pojo;
import com.tvip.surveylance.pojo.ratiokosongpojo;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class list_memento extends AppCompatActivity {
    ListView list_sfa;
    RelativeLayout peringatan;
    List<memento_pojo> memento_pojoList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ListViewAdapterMemento adapter;
    SweetAlertDialog pDialog;
    TextView tambah;
    String Tanggal1, Tanggal2;
    ImageView filtering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_memento);
        list_sfa = findViewById(R.id.list_sfa);
        peringatan = findViewById(R.id.peringatan);
        tambah = findViewById(R.id.tambah);
        filtering = findViewById(R.id.filtering);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        Tanggal1 = currentDateandTime;
        Tanggal2 = currentDateandTime;


        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(list_memento.this, tambah_memento.class);
                startActivity(intent);
            }
        });

        list_sfa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ids = ((memento_pojo) parent.getItemAtPosition(position)).getId_Auto();

                String tanggal_waktu = ((memento_pojo) parent.getItemAtPosition(position)).getDate_created();
                String sales_force = ((memento_pojo) parent.getItemAtPosition(position)).getNama_survey();
                String kode = ((memento_pojo) parent.getItemAtPosition(position)).getKode_pelanggan();
                String nama_pelanggan = ((memento_pojo) parent.getItemAtPosition(position)).getNama_sf();
                String nama_driver = ((memento_pojo) parent.getItemAtPosition(position)).getNama_driver();

                String qtyvsdo = ((memento_pojo) parent.getItemAtPosition(position)).getQty_fisik_vs_do_vs_so();
                String rutin = ((memento_pojo) parent.getItemAtPosition(position)).getRutin_kunjungan_sales();
                String penyampaian = ((memento_pojo) parent.getItemAtPosition(position)).getPenyampaian_program();
                String attitude = ((memento_pojo) parent.getItemAtPosition(position)).getPelayanan_attitude_dan_keluhan();
                String kategori = ((memento_pojo) parent.getItemAtPosition(position)).getKategori_temuan();
                String keterangan = ((memento_pojo) parent.getItemAtPosition(position)).getKeterangan_temuan();
                String info = ((memento_pojo) parent.getItemAtPosition(position)).getInfo_pelanggan();

                String latitude = ((memento_pojo) parent.getItemAtPosition(position)).getLatitude();
                String longitude = ((memento_pojo) parent.getItemAtPosition(position)).getLongitude();
                String alamat = ((memento_pojo) parent.getItemAtPosition(position)).getVerifikasi_alamat();

                String outlet = ((memento_pojo) parent.getItemAtPosition(position)).getDisplay_outlet();
                String ttd = ((memento_pojo) parent.getItemAtPosition(position)).getTanda_tangan();

                Intent intent = new Intent(getApplicationContext(), detail_memento.class);

                intent.putExtra("ids", ids);

                intent.putExtra("tanggal_waktu", tanggal_waktu);
                intent.putExtra("sales_force", sales_force);
                intent.putExtra("kode", kode);
                intent.putExtra("nama_pelanggan", nama_pelanggan);
                intent.putExtra("nama_driver", nama_driver);

                intent.putExtra("qtyvsdo", qtyvsdo);
                intent.putExtra("rutin", rutin);
                intent.putExtra("penyampaian", penyampaian);
                intent.putExtra("attitude", attitude);
                intent.putExtra("kategori", kategori);
                intent.putExtra("keterangan", keterangan);
                intent.putExtra("info", info);

                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("alamat", alamat);

                intent.putExtra("outlet", outlet);
                intent.putExtra("ttd", ttd);
                startActivity(intent);


//                String rata = ((ratiokosongpojo) parent.getItemAtPosition(position)).getRata_rata();
//                String standar = ((ratiokosongpojo) parent.getItemAtPosition(position)).getStandar();
//                String luas = ((ratiokosongpojo) parent.getItemAtPosition(position)).getLuas_gudang();
//                String stock_kosong = ((ratiokosongpojo) parent.getItemAtPosition(position)).getJumlah_stok();
//                String dtmSurvey = ((ratiokosongpojo) parent.getItemAtPosition(position)).getDtmSurvey();
//                String bulan = ((ratiokosongpojo) parent.getItemAtPosition(position)).getBulan();
//                String tahun = ((ratiokosongpojo) parent.getItemAtPosition(position)).getTahun();

            }
        });

        filtering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(list_memento.this);
                dialog.setContentView(R.layout.pilih_range_tanggal);
                dialog.show();

                Button batal = dialog.findViewById(R.id.batal);
                Button ok = dialog.findViewById(R.id.ok);

                final EditText edittanggal = dialog.findViewById(R.id.edittanggal);
                final EditText editsampaitanggal = dialog.findViewById(R.id.editsampaitanggal);

                MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().build();

                edittanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "Tag_picker");
                        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                Pair selectedDates = (Pair) materialDatePicker.getSelection();
                                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                                Date startDate = rangeDate.first;
                                Date endDate = rangeDate.second;
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd MMMM yyyy");
                                edittanggal.setText(simpleFormat.format(startDate));
                                editsampaitanggal.setText(simpleFormat.format(endDate));
                                Tanggal1 = convertFormat2(simpleFormat.format(startDate));
                                Tanggal2 = convertFormat2(simpleFormat.format(endDate));
                            }
                        });
                    }
                });

                editsampaitanggal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "Tag_picker");
                        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                Pair selectedDates = (Pair) materialDatePicker.getSelection();
                                final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));
                                Date startDate = rangeDate.first;
                                Date endDate = rangeDate.second;
                                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd MMMM yyyy");
                                edittanggal.setText(simpleFormat.format(startDate));
                                editsampaitanggal.setText(simpleFormat.format(endDate));

                                Tanggal1 = convertFormat2(simpleFormat.format(startDate));
                                Tanggal2 = convertFormat2(simpleFormat.format(endDate));
                            }
                        });
                    }
                });


                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edittanggal.getText().toString().length() == 0){
                            edittanggal.setError("Isi Tanggal");
                        } else {
                            dialog.dismiss();
                            getDataMemento();
                        }
                    }
                });
            }
        });


    }

    private void getDataMemento() {
        memento_pojoList.clear();
        pDialog = new SweetAlertDialog(list_memento.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.show();

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.4.168/api_memento/Kunjungan/index?kode_pelanggan="+ szId +"&tanggal=" +Tanggal1+ "&tanggal2=" + Tanggal2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        list_sfa.setVisibility(View.VISIBLE);
                        peringatan.setVisibility(View.GONE);
                        pDialog.dismissWithAnimation();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {
                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final memento_pojo movieItem = new memento_pojo(
                                        movieObject.getString("id_Auto"),
                                        movieObject.getString("nik_sf"),
                                        movieObject.getString("nama_sf"),
                                        movieObject.getString("kode_pelanggan"),
                                        movieObject.getString("verifikasi_alamat"),
                                        movieObject.getString("nama_survey"),
                                        movieObject.getString("nama_driver"),
                                        movieObject.getString("qty_fisik_vs_do_vs_so"),
                                        movieObject.getString("rutin_kunjungan_sales"),
                                        movieObject.getString("penyampaian_program"),
                                        movieObject.getString("pelayanan_attitude_dan_keluhan"),
                                        movieObject.getString("info_pelanggan"),
                                        movieObject.getString("kategori_temuan"),
                                        movieObject.getString("keterangan_temuan"),
                                        movieObject.getString("longitude"),
                                        movieObject.getString("latitude"),
                                        movieObject.getString("display_outlet"),
                                        movieObject.getString("tanda_tangan"),
                                        movieObject.getString("date_created"));
                                memento_pojoList.add(movieItem);

                                adapter = new ListViewAdapterMemento(memento_pojoList, getApplicationContext());
                                list_sfa.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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
                        list_sfa.setVisibility(View.GONE);
                        peringatan.setVisibility(View.VISIBLE);
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

    public class ListViewAdapterMemento extends ArrayAdapter<memento_pojo> {

        List<memento_pojo> memento_pojos;

        private Context context;

        public ListViewAdapterMemento(List<memento_pojo> memento_pojos, Context context) {
            super(context, R.layout.list_item_memento, memento_pojos);
            this.memento_pojos = memento_pojos;
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);

            View listViewItem = inflater.inflate(R.layout.list_item_memento, null, true);


            TextView tanggal = listViewItem.findViewById(R.id.tanggal);
            TextView ketnama = listViewItem.findViewById(R.id.ketnama);
            TextView salesforce = listViewItem.findViewById(R.id.salesforce);
            Chip keterangan_temuan = listViewItem.findViewById(R.id.keterangan_temuan);

            memento_pojo memento_pojo = getItem(position);

            tanggal.setText(convertFormat(memento_pojo.getDate_created()));
            ketnama.setText(memento_pojo.getNama_sf());
            salesforce.setText("Sales Force : " + memento_pojo.getNama_survey());
            keterangan_temuan.setText(memento_pojo.getKategori_temuan());
            if(memento_pojo.getKeterangan_temuan().equals("Tidak Ada Temuan")){
                int colorInt = getResources().getColor(R.color.background_green);
                ColorStateList csl = ColorStateList.valueOf(colorInt);
                int colorInt2 = getResources().getColor(R.color.border_green);
                ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                keterangan_temuan.setChipBackgroundColor(csl);
                keterangan_temuan.setChipStrokeColor(csl2);
                keterangan_temuan.setTextColor(Color.parseColor("#2ECC71"));
            } else {
                int colorInt = getResources().getColor(R.color.background_red);
                ColorStateList csl = ColorStateList.valueOf(colorInt);

                int colorInt2 = getResources().getColor(R.color.border_red);
                ColorStateList csl2 = ColorStateList.valueOf(colorInt2);

                keterangan_temuan.setChipBackgroundColor(csl);
                keterangan_temuan.setChipStrokeColor(csl2);
                keterangan_temuan.setTextColor(Color.parseColor("#FB4141"));
            }

            return listViewItem;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataMemento();
    }

    public static String convertFormat2(String inputDate) {
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }
}