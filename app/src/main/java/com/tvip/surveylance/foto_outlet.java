package com.tvip.surveylance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class foto_outlet extends AppCompatActivity {
    Button selanjutnya, batal;
    RelativeLayout depan, dalam, parkir, masuk, kanan, kiri;
    ImageView uploaddepan, uploaddalam, uploadparkir, uploadmasuk, uploadkanan, uploadkiri;
    TextView textdepan, textdalam, textparkir, textmasuk, textkanan, textkiri;
    static Bitmap bitmapdepan, bitmapdalam, bitmapparkir, bitmapmasuk, bitmapkanan, bitmapkiri;

    ImageView gambardepan, gambargudang, gambarareaparkir, gambarmasuk, gambarkanan, gambarkiri;

    LinearLayout lineardepan, lineardalam, linearparkir, linearmasuk, linearkanan, linearkiri;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_outlet);
        HttpsTrustManager.allowAllSSL();
        lineardepan = findViewById(R.id.lineardepan);
        lineardalam = findViewById(R.id.lineardalam);
        linearparkir = findViewById(R.id.linearparkir);
        linearmasuk = findViewById(R.id.linearmasuk);
        linearkanan = findViewById(R.id.linearkanan);
        linearkiri = findViewById(R.id.linearkiri);

        gambardepan = findViewById(R.id.gambardepan);
        gambargudang = findViewById(R.id.gambargudang);
        gambarareaparkir = findViewById(R.id.gambarareaparkir);
        gambarmasuk = findViewById(R.id.gambarmasuk);
        gambarkanan = findViewById(R.id.gambarkanan);
        gambarkiri = findViewById(R.id.gambarkiri);

        depan = findViewById(R.id.depan);
        uploaddepan = findViewById(R.id.uploaddepan);
        textdepan = findViewById(R.id.textdepan);

        dalam = findViewById(R.id.dalam);
        uploaddalam = findViewById(R.id.uploaddalam);
        textdalam = findViewById(R.id.textdalam);

        parkir = findViewById(R.id.parkir);
        uploadparkir = findViewById(R.id.uploadparkir);
        textparkir = findViewById(R.id.textparkir);

        masuk = findViewById(R.id.masuk);
        uploadmasuk = findViewById(R.id.uploadmasuk);
        textmasuk = findViewById(R.id.textmasuk);

        kanan = findViewById(R.id.kanan);
        uploadkanan = findViewById(R.id.uploadkanan);
        textkanan = findViewById(R.id.textkanan);

        kiri = findViewById(R.id.kiri);
        uploadkiri = findViewById(R.id.uploadkiri);
        textkiri = findViewById(R.id.textkiri);

        selanjutnya = findViewById(R.id.selanjutnya);
        batal = findViewById(R.id.batal);

        depan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }

            }
        });

        dalam.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2);
                }
            }
        });

        parkir.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                }
            }
        });

        masuk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 4);
                }
            }
        });

        kanan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 5);
                }
            }
        });

        kiri.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 6);
                }
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
                if(textdepan.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(foto_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Foto Depan Masih Kosong")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(textdalam.getVisibility() == View.VISIBLE){
                        new SweetAlertDialog(foto_outlet.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Foto Dalam Gudang Masih Kosong")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                } else if(textparkir.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(foto_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Foto Area Parkir Masih Kosong")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(textmasuk.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(foto_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Foto Akses Jalan Masuk Masih Kosong")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(textkanan.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(foto_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Foto Arah Ke Kanan Masih Kosong")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(textkiri.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(foto_outlet.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Foto Arah Ke Kiri Masih Kosong")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    Intent intent = new Intent(getBaseContext(), kondisi_outlet.class);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (1 == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
        } else if (2 == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2);
            }
        } else if (3 == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            }
        } else if (4 == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 4);
            }
        } else if (5 == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 5);
            }
        } else if (6 == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 6);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            bitmapdepan = (Bitmap) data.getExtras().get("data");
            lineardepan.setVisibility(View.VISIBLE);
            int maxHeight = 500;
            int maxWidth = 500;
            float scale = Math.min(((float)maxHeight / bitmapdepan.getWidth()), ((float)maxWidth / bitmapdepan.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmapdepan = Bitmap.createBitmap(bitmapdepan, 0, 0, bitmapdepan.getWidth(), bitmapdepan.getHeight(), matrix, true);

            gambardepan.setImageBitmap(bitmapdepan);
            uploaddepan.setImageDrawable(gambardepan.getDrawable());

            textdepan.setVisibility(View.GONE);

            ViewGroup.LayoutParams paramnpwp = uploaddepan.getLayoutParams();

            double sizeInDP = 500;
            int marginInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP, getResources()
                            .getDisplayMetrics());

            double sizeInDP2 = 500;
            int marginInDp2 = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, (float) sizeInDP2, getResources()
                            .getDisplayMetrics());

            paramnpwp.width = marginInDp;
            paramnpwp.height = marginInDp2;
            uploaddepan.setLayoutParams(paramnpwp);

            super.onActivityResult(1, resultCode, data);
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            lineardalam.setVisibility(View.VISIBLE);
            bitmapdalam = (Bitmap) data.getExtras().get("data");
            int maxHeight = 500;
            int maxWidth = 500;
            float scale = Math.min(((float)maxHeight / bitmapdalam.getWidth()), ((float)maxWidth / bitmapdalam.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmapdalam = Bitmap.createBitmap(bitmapdalam, 0, 0, bitmapdalam.getWidth(), bitmapdalam.getHeight(), matrix, true);

            gambargudang.setImageBitmap(bitmapdalam);
            uploaddalam.setImageDrawable(gambargudang.getDrawable());
            textdalam.setVisibility(View.GONE);
            ViewGroup.LayoutParams paramnpwp = uploaddalam.getLayoutParams();

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
            uploaddalam.setLayoutParams(paramnpwp);
            super.onActivityResult(2, resultCode, data);
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            linearparkir.setVisibility(View.VISIBLE);
            bitmapparkir = (Bitmap) data.getExtras().get("data");

            int maxHeight = 500;
            int maxWidth = 500;
            float scale = Math.min(((float)maxHeight / bitmapparkir.getWidth()), ((float)maxWidth / bitmapparkir.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmapparkir = Bitmap.createBitmap(bitmapparkir, 0, 0, bitmapparkir.getWidth(), bitmapparkir.getHeight(), matrix, true);

            gambarareaparkir.setImageBitmap(bitmapparkir);
            uploadparkir.setImageDrawable(gambarareaparkir.getDrawable());
            textparkir.setVisibility(View.GONE);
            ViewGroup.LayoutParams paramnpwp = uploadparkir.getLayoutParams();

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
            uploadparkir.setLayoutParams(paramnpwp);
            super.onActivityResult(3, resultCode, data);
        } else if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            linearmasuk.setVisibility(View.VISIBLE);
            bitmapmasuk = (Bitmap) data.getExtras().get("data");

            int maxHeight = 500;
            int maxWidth = 500;
            float scale = Math.min(((float)maxHeight / bitmapmasuk.getWidth()), ((float)maxWidth / bitmapmasuk.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmapmasuk = Bitmap.createBitmap(bitmapmasuk, 0, 0, bitmapmasuk.getWidth(), bitmapmasuk.getHeight(), matrix, true);

            gambarmasuk.setImageBitmap(bitmapmasuk);
            uploadmasuk.setImageDrawable(gambarmasuk.getDrawable());
            textmasuk.setVisibility(View.GONE);
            ViewGroup.LayoutParams paramnpwp = uploadmasuk.getLayoutParams();

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
            uploadmasuk.setLayoutParams(paramnpwp);
            super.onActivityResult(4, resultCode, data);
        } else if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            linearkanan.setVisibility(View.VISIBLE);
            bitmapkanan = (Bitmap) data.getExtras().get("data");

            int maxHeight = 500;
            int maxWidth = 500;
            float scale = Math.min(((float)maxHeight / bitmapkanan.getWidth()), ((float)maxWidth / bitmapkanan.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmapkanan = Bitmap.createBitmap(bitmapkanan, 0, 0, bitmapkanan.getWidth(), bitmapkanan.getHeight(), matrix, true);

            gambarkanan.setImageBitmap(bitmapkanan);
            uploadkanan.setImageDrawable(gambarkanan.getDrawable());
            textkanan.setVisibility(View.GONE);
            ViewGroup.LayoutParams paramnpwp = uploadkanan.getLayoutParams();

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
            uploadkanan.setLayoutParams(paramnpwp);
            super.onActivityResult(5, resultCode, data);
        } else if (requestCode == 6 && resultCode == Activity.RESULT_OK) {
            linearkiri.setVisibility(View.VISIBLE);
            bitmapkiri = (Bitmap) data.getExtras().get("data");

            int maxHeight = 500;
            int maxWidth = 500;
            float scale = Math.min(((float)maxHeight / bitmapkiri.getWidth()), ((float)maxWidth / bitmapkiri.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmapkiri = Bitmap.createBitmap(bitmapkiri, 0, 0, bitmapkiri.getWidth(), bitmapkiri.getHeight(), matrix, true);

            gambarkiri.setImageBitmap(bitmapkiri);
            uploadkiri.setImageDrawable(gambarkiri.getDrawable());
            textkiri.setVisibility(View.GONE);
            ViewGroup.LayoutParams paramnpwp = uploadkiri.getLayoutParams();

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
            uploadkiri.setLayoutParams(paramnpwp);
            super.onActivityResult(5, resultCode, data);
        }
    }
}