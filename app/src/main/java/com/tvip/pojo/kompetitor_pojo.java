package com.tvip.surveylance.pojo;

public class kompetitor_pojo {
    private String id;
    private String product;
    private String keterangan;
    private String qty;

    public kompetitor_pojo(String id, String product, String keterangan, String qty) {
        this.id = id;
        this.product = product;
        this.keterangan = keterangan;
        this.qty = qty;

    }

    public String getId() { return id; }

    public String getProduct() {
        return product;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public String getQty() {
        return qty;
    }
}
