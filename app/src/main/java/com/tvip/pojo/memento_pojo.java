package com.tvip.surveylance.pojo;

public class memento_pojo {
    private String id_Auto;
    private String nik_sf;
    private String nama_sf;
    private String kode_pelanggan;

    private String verifikasi_alamat;
    private String nama_survey;
    private String nama_driver;
    private String qty_fisik_vs_do_vs_so;
    private String rutin_kunjungan_sales;

    private String penyampaian_program;
    private String pelayanan_attitude_dan_keluhan;
    private String info_pelanggan;
    private String kategori_temuan;
    private String keterangan_temuan;

    private String longitude;
    private String latitude;
    private String display_outlet;
    private String tanda_tangan;

    private String date_created;

    public memento_pojo(String id_Auto, String nik_sf, String nama_sf, String kode_pelanggan,
                        String verifikasi_alamat, String nama_survey, String nama_driver, String qty_fisik_vs_do_vs_so, String rutin_kunjungan_sales,
                        String penyampaian_program, String pelayanan_attitude_dan_keluhan, String info_pelanggan, String kategori_temuan,
                        String keterangan_temuan, String longitude, String latitude, String display_outlet, String tanda_tangan,
                        String date_created) {

        this.id_Auto = id_Auto;
        this.nik_sf = nik_sf;
        this.nama_sf = nama_sf;
        this.kode_pelanggan = kode_pelanggan;

        this.verifikasi_alamat = verifikasi_alamat;
        this.nama_survey = nama_survey;
        this.nama_driver = nama_driver;
        this.qty_fisik_vs_do_vs_so = qty_fisik_vs_do_vs_so;
        this.rutin_kunjungan_sales = rutin_kunjungan_sales;

        this.penyampaian_program = penyampaian_program;
        this.pelayanan_attitude_dan_keluhan = pelayanan_attitude_dan_keluhan;
        this.info_pelanggan = info_pelanggan;
        this.kategori_temuan = kategori_temuan;
        this.keterangan_temuan = keterangan_temuan;

        this.longitude = longitude;
        this.latitude = latitude;
        this.display_outlet = display_outlet;
        this.tanda_tangan = tanda_tangan;

        this.date_created = date_created;
    }

    public String getId_Auto() {return id_Auto;}
    public String getNik_sf() {return nik_sf;}
    public String getNama_sf() {return nama_sf;}
    public String getKode_pelanggan() {return kode_pelanggan;}

    public String getVerifikasi_alamat() {return verifikasi_alamat;}

    public String getNama_survey() {return nama_survey;}
    public String getNama_driver() {return nama_driver;}
    public String getQty_fisik_vs_do_vs_so() {return qty_fisik_vs_do_vs_so;}
    public String getRutin_kunjungan_sales() {return rutin_kunjungan_sales;}

    public String getPenyampaian_program() {return penyampaian_program;}
    public String getPelayanan_attitude_dan_keluhan() {return pelayanan_attitude_dan_keluhan;}
    public String getInfo_pelanggan() {return info_pelanggan;}
    public String getKategori_temuan() {return kategori_temuan;}
    public String getKeterangan_temuan() {return keterangan_temuan;}

    public String getLongitude() {return longitude;}
    public String getLatitude() {return latitude;}
    public String getDisplay_outlet() {return display_outlet;}
    public String getTanda_tangan() {return tanda_tangan;}

    public String getDate_created() {return date_created;}
}
