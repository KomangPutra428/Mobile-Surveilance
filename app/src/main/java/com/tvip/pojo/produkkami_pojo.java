package com.tvip.surveylance.pojo;

public class produkkami_pojo {
    private String id;
    private String sku;
    private String qty;

    public produkkami_pojo(String id, String sku, String qty) {
        this.id = id;
        this.sku = sku;
        this.qty = qty;

    }

    public String getId() { return id; }

    public String getSku() {
        return sku;
    }

    public String getQty() {
        return qty;
    }
}
