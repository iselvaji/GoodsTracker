package com.easyvan.goodstracker.model.rest.product.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sm5 on 6/12/2019.
 */

public class Location implements Parcelable {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("address")
    @Expose
    private String address;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public final static Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {

        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }
        public Location[] newArray(int size) {
            return (new Location[size]);
        }
    };

    private Location(Parcel in) {
        this.lat = in.readDouble();
        this.lng = in.readDouble();
        this.address = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(address);
    }

    public int describeContents() {
        return  0;
    }

    public Location(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
