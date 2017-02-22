package com.chinaweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/2/20.
 * 由于JSON中的一些字段可能不太适合直接作为Java字段来命名， 因此这里使用@SerializedName()注解的方式
 * 来让JSON和Java之间建立映射关系。
 */

public class Basic {
    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;
    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
