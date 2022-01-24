package com.shuoxd.camera.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Created by Administrator on 2019/6/23.
 */

public class ProvinceCityBean implements IPickerViewData {
    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }


    public static class CityBean {

        private String name;
        private String lng;
        private String lat;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }
}
