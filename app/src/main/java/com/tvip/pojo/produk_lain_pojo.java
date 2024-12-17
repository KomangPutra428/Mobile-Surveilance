package com.tvip.surveylance.pojo;

public class produk_lain_pojo {
    private String produk;
    private String omset;

    public produk_lain_pojo(String produk, String omset) {
        this.produk = produk;
        this.omset = omset;

    }

    public String getProduk() {
        return produk;
    }

    public String getOmset() {
        return omset;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    public void setOmset(String omset) {
        this.omset = omset;
    }
}
