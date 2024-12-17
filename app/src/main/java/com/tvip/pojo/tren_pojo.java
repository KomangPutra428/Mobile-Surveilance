package com.tvip.surveylance.pojo;

public class tren_pojo {
    private String sku;
    private String qty;
    private String bulan;
    private String tahun;

    public tren_pojo(String sku, String qty, String bulan, String tahun) {
        this.sku = sku;
        this.qty = qty;
        this.bulan = bulan;
        this.tahun = tahun;

    }

    public String getSku() {
        return sku;
    }

    public String getQty() {
        return qty;
    }

    public String getBulan() {
        return bulan;
    }

    public String getTahun() {
        return tahun;
    }
}
