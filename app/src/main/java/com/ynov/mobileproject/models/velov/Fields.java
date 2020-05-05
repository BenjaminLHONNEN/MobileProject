package com.ynov.mobileproject.models.velov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Fields {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dist")
    @Expose
    private String dist;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("availabili")
    @Expose
    private String availabili;
    @SerializedName("availabi_1")
    @Expose
    private String availabi1;
    @SerializedName("geo_shape")
    @Expose
    private GeoShape geoShape;
    @SerializedName("pole")
    @Expose
    private String pole;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("last_updat")
    @Expose
    private String lastUpdat;
    @SerializedName("availabl_1")
    @Expose
    private String availabl1;
    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("available")
    @Expose
    private String available;
    @SerializedName("commune")
    @Expose
    private String commune;
    @SerializedName("bonus")
    @Expose
    private String bonus;
    @SerializedName("address2")
    @Expose
    private String address2;
    @SerializedName("bike_stand")
    @Expose
    private String bikeStand;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("last_upd_1")
    @Expose
    private String lastUpd1;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("geo_point_2d")
    @Expose
    private List<Double> geoPoint2d = null;
    @SerializedName("banking")
    @Expose
    private String banking;
    @SerializedName("nmarrond")
    @Expose
    private String nmarrond;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAvailabili() {
        return availabili;
    }

    public void setAvailabili(String availabili) {
        this.availabili = availabili;
    }

    public String getAvailabi1() {
        return availabi1;
    }

    public void setAvailabi1(String availabi1) {
        this.availabi1 = availabi1;
    }

    public GeoShape getGeoShape() {
        return geoShape;
    }

    public void setGeoShape(GeoShape geoShape) {
        this.geoShape = geoShape;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLastUpdat() {
        return lastUpdat;
    }

    public void setLastUpdat(String lastUpdat) {
        this.lastUpdat = lastUpdat;
    }

    public String getAvailabl1() {
        return availabl1;
    }

    public void setAvailabl1(String availabl1) {
        this.availabl1 = availabl1;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getBikeStand() {
        return bikeStand;
    }

    public void setBikeStand(String bikeStand) {
        this.bikeStand = bikeStand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLastUpd1() {
        return lastUpd1;
    }

    public void setLastUpd1(String lastUpd1) {
        this.lastUpd1 = lastUpd1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getGeoPoint2d() {
        return geoPoint2d;
    }

    public void setGeoPoint2d(List<Double> geoPoint2d) {
        this.geoPoint2d = geoPoint2d;
    }

    public String getBanking() {
        return banking;
    }

    public void setBanking(String banking) {
        this.banking = banking;
    }

    public String getNmarrond() {
        return nmarrond;
    }

    public void setNmarrond(String nmarrond) {
        this.nmarrond = nmarrond;
    }

}
