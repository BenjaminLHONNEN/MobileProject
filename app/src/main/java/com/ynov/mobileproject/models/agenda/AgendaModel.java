package com.ynov.mobileproject.models.agenda;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class AgendaModel implements Serializable {

    public String uid;
    public String Image;
    public String Location;
    public double Latitude;
    public double Longitude;
    public Date date;

}
