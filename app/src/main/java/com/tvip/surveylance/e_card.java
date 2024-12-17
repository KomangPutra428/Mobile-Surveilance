package com.tvip.surveylance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.zxing.WriterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class e_card extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView idpelanggan, namatoko, retail, owner, alamat, pt;
    ImageView QRId;
    QRGEncoder qrgEncoder;
    Bitmap bitmap;
    ImageView logo;
    Button download;
    MaterialCardView ecard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_card);
        HttpsTrustManager.allowAllSSL();
        idpelanggan = findViewById(R.id.idpelanggan);
        namatoko = findViewById(R.id.namatoko);
        retail = findViewById(R.id.retail);
        owner = findViewById(R.id.owner);
        QRId = findViewById(R.id.QRId);
        alamat = findViewById(R.id.alamat);
        pt = findViewById(R.id.pt);
        logo = findViewById(R.id.logo);
        download = findViewById(R.id.download);
        ecard = findViewById(R.id.ecard);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ecard.setDrawingCacheEnabled(true);
                Bitmap bitmap = ecard.getDrawingCache();

                String root = Environment.getExternalStorageDirectory().toString();
                File newDir = new File(root + "/saved_picture");
                newDir.mkdirs();
                Random gen = new Random();
                int n = 10000;
                n = gen.nextInt(n);
                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                String szId = sharedPreferences.getString("szId", null);
                String fotoname = "kld" + ".jpg";
                File file = new File(newDir, fotoname);
                String s = file.getAbsolutePath();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                System.err.print("Path of saved image." + s);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {

                }
            }
        });

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String szId = sharedPreferences.getString("szId", null);
        String szName = sharedPreferences.getString("szName", null);
        String szHierarchyId = sharedPreferences.getString("szHierarchyId", null);
        String szContactPerson1 = sharedPreferences.getString("szContactPerson1", null);

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = manager.getDefaultDisplay();



        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String nik_baru = sharedPreferences.getString("szId", null);
        String rest = nik_baru;
        String[] parts = rest.split("-");
        String restnomor = parts[0];
        String restnomorbaru = restnomor.replace(" ", "");

        if(restnomorbaru.equals("321") || restnomorbaru.equals("336") || restnomorbaru.equals("324")){
            pt.setText("PT. Adi Sukses Abadi");
            logo.setImageResource(R.drawable.asa);
        } else {
            logo.setImageResource(R.drawable.tvip);
            pt.setText("PT. Tirta Varia Intipratama");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/master/lokasi/index_kodenikdepo?kode_dms=" + restnomorbaru,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    alamat.setText(movieObject.getString("alamat"));

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
        RequestQueue requestQueue = Volley.newRequestQueue(e_card.this);
        requestQueue.add(stringRequest);

        Point point = new Point();
        display.getSize(point);


        int width = point.x;
        int height = point.y;

        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        qrgEncoder = new QRGEncoder(szId, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            QRId.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

        idpelanggan.setText(szId);
        namatoko.setText(szName);
        retail.setText(szHierarchyId);
        owner.setText(szContactPerson1);
    }
}