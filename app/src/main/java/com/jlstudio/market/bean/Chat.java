package com.jlstudio.market.bean;

/**
 * Created by gzw on 2016/3/24.
 */
public class Chat {
    private int id;
    private String userfrom;
    private String fromname;
    private String  userto;
    private String  toname;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserfrom() {
        return userfrom;
    }

    public void setUserfrom(String userfrom) {
        this.userfrom = userfrom;
    }

    public String getUserto() {
        return userto;
    }

    public void setUserto(String userto) {
        this.userto = userto;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    private long datetime;
}
