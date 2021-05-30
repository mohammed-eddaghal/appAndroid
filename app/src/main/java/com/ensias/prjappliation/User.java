package com.ensias.prjappliation;


public class User {
    static int cont=0;
    private String id;
    private double lang,lat;
    private String name;

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

    public User(String id, double lang, double lat) {
        this.id = id;
        this.lang = lang;
        this.lat = lat;
        this.name = "med"+(++cont);
    }

    @Override
    public String toString() {
        return super.toString()+"/|\'"+
                "User{" +
                "id='" + id + '\'' +
                ", lang=" + lang +
                ", lat=" + lat +
                ", name='" + name + '\'' +
                '}';
    }

    public User(User user){
        id=user.id;
        name=user.name;
        lang=user.lang;
        lat=user.lat;
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

    public void cloneUser(User user){
        setId(user.getId());
        setLat(user.getLat());
        setLang(user.getLang());
        setName(user.getName());
    }
}
