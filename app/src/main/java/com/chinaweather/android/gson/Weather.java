package com.chinaweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 * 创建一个总体的类， 来引用刚刚创建的各个实体类。
 */
public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

//    @Override
//    public String toString() {
//        return "status:"+ status+" basic:"+basic+" aqi:"+aqi+ " now:"+now;
//    }
}
