package com.ynov.mobileproject.models.velov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Parameters {

    @SerializedName("dataset")
    @Expose
    private String dataset;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("rows")
    @Expose
    private Integer rows;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("geofilter.distance")
    @Expose
    private List<String> geofilterDistance = null;
    @SerializedName("facet")
    @Expose
    private List<String> facet = null;

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getGeofilterDistance() {
        return geofilterDistance;
    }

    public void setGeofilterDistance(List<String> geofilterDistance) {
        this.geofilterDistance = geofilterDistance;
    }

    public List<String> getFacet() {
        return facet;
    }

    public void setFacet(List<String> facet) {
        this.facet = facet;
    }

}
