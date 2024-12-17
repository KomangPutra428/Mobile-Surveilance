package com.tvip.surveylance.pojo;

public class armada_pojo {
    private String id;
    private String armada;
    private String unit;

    private String satuan;

    private String kapasitas;


    public armada_pojo(String id, String armada, String unit, String satuan, String kapasitas) {
        this.id = id;
        this.armada = armada;
        this.unit = unit;
        this.satuan = satuan;
        this.kapasitas = kapasitas;

    }

    public String getId() {
        return id;
    }

    public String getArmada() {
        return armada;
    }

    public String getUnit() {
        return unit;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public String getSatuan() {
        return satuan;
    }
}
