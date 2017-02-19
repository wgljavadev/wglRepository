package com.chinaweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/2/19.
 * 实体类继承LitePal中的DataSupport ， 由LitePal来进行操作。
 * LitePal中的每一个实体类都是必须要继承自DataSupport
 */

public class Province extends DataSupport {
    private int id;     //每个实体中都应该有的
    private String provinceName;
    private int provinceCode;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id ;
    }
    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public int getProvinceCode() {
        return provinceCode;
    }
    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
