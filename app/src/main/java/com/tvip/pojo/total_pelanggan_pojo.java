package com.tvip.surveylance.pojo;

public class total_pelanggan_pojo {
    private String jenis_pelanggan;
    private String total;

    public total_pelanggan_pojo(String jenis_pelanggan, String total) {
        this.jenis_pelanggan = jenis_pelanggan;
        this.total = total;

    }

    public String getJenis_pelanggan() { return jenis_pelanggan; }
    public String getTotal() { return total; }
}
