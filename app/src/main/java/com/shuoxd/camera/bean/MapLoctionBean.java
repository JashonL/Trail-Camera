package com.shuoxd.camera.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created：2018/8/3 on 17:46
 * Author:gaideng on dg
 * Description:
 */

public class MapLoctionBean implements Parcelable{
    private String city;
    private double latitude;
    private double longitude;
    private String country;
    private String detail;

    public MapLoctionBean() {
    }

    protected MapLoctionBean(Parcel in) {
        city = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        country=in.readString();
        detail=in.readString();
    }

    public static final Creator<MapLoctionBean> CREATOR = new Creator<MapLoctionBean>() {
        @Override
        public MapLoctionBean createFromParcel(Parcel in) {
            return new MapLoctionBean(in);
        }

        @Override
        public MapLoctionBean[] newArray(int size) {
            return new MapLoctionBean[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(country);
        dest.writeString(detail);
    }
}
