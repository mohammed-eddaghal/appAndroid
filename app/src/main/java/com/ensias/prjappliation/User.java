package com.ensias.prjappliation;


public class User {
    String id;
    double lang,lat;
    String name;
    public User(){
    }
    public User(double lang, double lat, String name) {
        this.lang = lang;
        this.lat = lat;
        this.name = name;
    }

    public User(String id, double lang, double lat, String name) {
        this.id = id;
        this.lang = lang;
        this.lat = lat;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLang() {
        return lang;
    }

    public double getLat() {
        return lat;
    }

    public String getName() {
        return name;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setName(String name) {
        this.name = name;
    }
}
