package com.tvip.surveylance.pojo;

public class pelanggan_pojo_2 {
    private String id;
    private String nama_pelanggan;
    private String alamat_pelanggan;
    private String jenis;

    public pelanggan_pojo_2(String id, String nama_pelanggan, String alamat_pelanggan, String jenis) {
        this.id = id;
        this.nama_pelanggan = nama_pelanggan;
        this.alamat_pelanggan = alamat_pelanggan;
        this.jenis = jenis;

    }

    public String getId() { return id; }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public String getAlamat_pelanggan() {
        return alamat_pelanggan;
    }

    public String getJenis() {
        return jenis;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public void setAlamat_pelanggan(String alamat_pelanggan) {
        this.alamat_pelanggan = alamat_pelanggan;
    }
}
