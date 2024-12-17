package com.tvip.surveylance.pojo;

public class pelanggan_pojo {
    private String nama_pelanggan;
    private String alamat_pelanggan;
    private String jenis;

    public pelanggan_pojo(String nama_pelanggan, String alamat_pelanggan, String jenis) {
        this.nama_pelanggan = nama_pelanggan;
        this.alamat_pelanggan = alamat_pelanggan;
        this.jenis = jenis;

    }

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
