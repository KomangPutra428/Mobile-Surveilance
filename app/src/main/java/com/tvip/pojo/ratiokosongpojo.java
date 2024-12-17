package com.tvip.surveylance.pojo;

public class ratiokosongpojo {
    private String id_ratio;
    private String depo;
    private String segment;
    private String bulan;
    private String tahun;

    private String total;
    private String rata_rata;
    private String standar;
    private String jumlah_stok;

    private String luas_gudang;
    private String user_input;
    private String nik_survey;
    private String dtmSurvey;

    private String dtmLastUpdate;
    private String dtmCreate;
    private String submit_date;

    public ratiokosongpojo(String id_ratio, String depo, String segment, String bulan, String tahun,
                           String total, String rata_rata, String standar, String jumlah_stok,
                           String luas_gudang, String user_input, String nik_survey, String dtmSurvey,
                           String dtmLastUpdate, String dtmCreate, String submit_date) {
        this.id_ratio = id_ratio;
        this.depo = depo;
        this.segment = segment;
        this.bulan = bulan;
        this.tahun = tahun;

        this.total = total;
        this.rata_rata = rata_rata;
        this.standar = standar;
        this.jumlah_stok = jumlah_stok;

        this.luas_gudang = luas_gudang;
        this.user_input = user_input;
        this.nik_survey = nik_survey;
        this.dtmSurvey = dtmSurvey;

        this.dtmLastUpdate = dtmLastUpdate;
        this.dtmCreate = dtmCreate;
        this.submit_date = submit_date;

    }

    public String getId_ratio() {return id_ratio;}

    public String getDepo() {return depo;}

    public String getSegment() {return segment;}
    public String getBulan() {return bulan;}
    public String getTahun() {return tahun;}

    public String getTotal() {return total;}
    public String getRata_rata() {return rata_rata;}
    public String getStandar() {return standar;}
    public String getJumlah_stok() {return jumlah_stok;}

    public String getLuas_gudang() {return luas_gudang;}
    public String getUser_input() {return user_input;}
    public String getNik_survey() {return nik_survey;}
    public String getDtmSurvey() {return dtmSurvey;}

    public String getDtmLastUpdate() {return dtmLastUpdate;}
    public String getDtmCreate() {return dtmCreate;}
    public String getSubmit_date() {return submit_date;}
}
