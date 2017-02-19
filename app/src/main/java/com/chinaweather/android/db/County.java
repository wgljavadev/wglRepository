package com.chinaweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/2/19.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;  //记录县的名字
    private String weatherId; //请求和风天气服务器接口的天气， 即记录县所对应的天气id。
    private int cityId; //当前城市所属的id值。
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCountyName() {
        return countyName;
    }
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
    public String getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
