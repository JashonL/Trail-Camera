package com.shuoxd.camera.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryDataUtils {

    /**
     * 获取1国家
     */
    public static List<String> getCountry() {
        String [] country={"United States","Canada","Mexico"};
        return Arrays.asList(country);
    }



    /**
     * 国家对应的州
     */
    public static List<List<String>> getState() {
        List<List<String>>datas=new ArrayList<>();
        datas.add(getUnitedStates());
        datas.add(getCanadas());
        datas.add(getMoxico());
        return datas;
    }


    /**
     * 美国
     * @return
     */
    public static List<String> getUnitedStates() {
        List<String>citys=new ArrayList<>();
        citys.add("Alaska");
        citys.add("Alabama");
        citys.add("Arkansas");
        citys.add("Arizona");
        citys.add("California");
        citys.add("Colorado");
        citys.add("Connecticut");
        citys.add("District of Columbia");
        citys.add("Delaware");
        citys.add("Florida");
        citys.add("Georgia");
        citys.add("Hawaii");
        citys.add("Lowa");
        citys.add("Idaho");
        citys.add("Illinois");
        citys.add("Indiana");
        citys.add("Kansas");
        citys.add("Kentucky");
        citys.add("Louisiana");
        citys.add("Massachusetts");
        citys.add("Maryland");
        citys.add("Maine");
        citys.add("Michigan");
        citys.add("Minnesota");
        citys.add("Missouri");
        citys.add("Mississippi");
        citys.add("Montana");
        citys.add("North Carolina");
        citys.add("North Dakota");
        citys.add("Nebraska");
        citys.add("New Hampshire");
        citys.add("New Jersey");
        citys.add("Nevada");
        citys.add("New York");
        citys.add("Ohio");
        citys.add("Oklahoma");
        citys.add("Oregon");
        citys.add("Pennsylvania");
        citys.add("Rhode Island");
        citys.add("South Carolina");
        citys.add("South Dakota");
        citys.add("Tennessee");
        citys.add("Texas");
        citys.add("Utah");
        citys.add("Virginia");
        citys.add("Vermont");
        citys.add("Washington");
        citys.add("Wisconisn");
        citys.add("West Virginia");
        citys.add("Wyoming");
        return citys;
    }







    /**
     * 加拿大
     * * @return
     */
    public static List<String> getCanadas() {
        List<String>citys=new ArrayList<>();
        citys.add("Alberta");
        citys.add("British Columbia");
        citys.add("Manitoba");
        citys.add("Newbrunswick");
        citys.add("Newfoundland");
        citys.add("Nova Soctia");
        citys.add("Ontario");
        citys.add("Prince Edward Island");
        citys.add("Quebec");
        citys.add("Saskatchewan");
        return citys;
    }





    /**
     * 加拿大
     * * @return
     */
    public static List<String> getMoxico() {
        List<String>citys=new ArrayList<>();
        citys.add("Guerrero");
        citys.add("Hidalgo");
        citys.add("Jalisco");
        citys.add("Michoacan de Ocampo");
        citys.add("Morelos");
        citys.add("Mexico");
        citys.add("Nayarit");
        citys.add("Nuevo leon");
        citys.add("Oaxaca");
        citys.add("Puebla");
        citys.add("Queretaro de Arteaga");
        citys.add("Sinaloa");
        citys.add("San Luis Potosi");
        citys.add("Sonora");
        citys.add("Tabasco");
        citys.add("Tamaulipas");
        citys.add("Tlaxcala");
        citys.add("Veracruz");
        citys.add("Yucatan");
        citys.add("Zac");
        citys.add("Chiapas");
        citys.add("Baja California");
        citys.add("Colima");
        citys.add("Durango");
        citys.add("Coahuila de Zaragoza");
        citys.add("Ciudad de Mexico");
        citys.add("Aguascalientes");
        citys.add("Guanajuato");
        citys.add("Campeche");
        citys.add("Chihuahua");

        return citys;
    }


}
