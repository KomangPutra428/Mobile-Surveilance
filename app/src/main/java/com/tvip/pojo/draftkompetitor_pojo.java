package com.tvip.surveylance.pojo;

public class draftkompetitor_pojo {
    private String id;
    private String submit_date;
    private String nik_survey;
    private String kode_pelanggan;

    private String nama_jenis;
    private String nama_kiriman;
    private String produk_lain;

    private String kompetitor;

    private String uom;
    private String volume;

    private String average_penjualan_before;
    private String average_penjualan_now;
    private String harga_beli;

    private String harga_jual;
    private String nama_program;
    private String periode_program_awal;

    private String periode_program_akhir;
    private String mekanisme_program;
    private String target_program;

    private String keterangan;

    public draftkompetitor_pojo(String id, String submit_date, String nik_survey, String kode_pelanggan,
                                String nama_jenis, String nama_kiriman, String produk_lain, String kompetitor, String uom, String volume,
                                String average_penjualan_before, String average_penjualan_now, String harga_beli,
                                String harga_jual, String nama_program, String periode_program_awal,
                                String periode_program_akhir, String mekanisme_program, String target_program,
                                String keterangan) {
        this.id = id;
        this.submit_date = submit_date;
        this.nik_survey = nik_survey;
        this.kode_pelanggan = kode_pelanggan;

        this.nama_jenis = nama_jenis;
        this.nama_kiriman = nama_kiriman;
        this.produk_lain = produk_lain;
        this.kompetitor = kompetitor;

        this.uom = uom;
        this.volume = volume;

        this.average_penjualan_before = average_penjualan_before;
        this.average_penjualan_now = average_penjualan_now;
        this.harga_beli = harga_beli;

        this.harga_jual = harga_jual;
        this.nama_program = nama_program;
        this.periode_program_awal = periode_program_awal;

        this.periode_program_akhir = periode_program_akhir;
        this.mekanisme_program = mekanisme_program;
        this.target_program = target_program;

        this.keterangan = keterangan;


    }

    public String getId() {return id;}
    public String getSubmit_date() {return submit_date;}
    public String getNik_survey() {return nik_survey;}
    public String getKode_pelanggan() {return kode_pelanggan;}

    public String getKompetitor() {return kompetitor;}

    public String getNama_jenis() {
        return nama_jenis;
    }

    public String getNama_kiriman() {
        return nama_kiriman;
    }

    public String getProduk_lain() {
        return produk_lain;
    }

    public String getUom() {return uom;}
    public String getVolume() {return volume;}

    public String getAverage_penjualan_before() {return average_penjualan_before;}
    public String getAverage_penjualan_now() {return average_penjualan_now;}
    public String getHarga_beli() {return harga_beli;}

    public String getHarga_jual() {return harga_jual;}
    public String getNama_program() {return nama_program;}
    public String getPeriode_program_awal() {return periode_program_awal;}

    public String getPeriode_program_akhir() {return periode_program_akhir;}
    public String getMekanisme_program() {return mekanisme_program;}
    public String getTarget_program() {return target_program;}

    public String getKeterangan() {return keterangan;}
}
