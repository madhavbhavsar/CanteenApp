package com.madapps.canteen.worker.entryactivity;

public class EntryModel {

    String fid,date,itemid,itemquantity,ispaid;

    public EntryModel() {
    }

    public EntryModel(String fid, String date, String itemid, String itemquantity,String ispaid) {
        this.fid = fid;
        this.date = date;
        this.itemid = itemid;
        this.itemquantity = itemquantity;
        this.ispaid = ispaid;
    }

    public String getIspaid() {
        return ispaid;
    }

    public void setIspaid(String ispaid) {
        this.ispaid = ispaid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(String itemquantity) {
        this.itemquantity = itemquantity;
    }
}
