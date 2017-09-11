package com.supermap.egispservice.base.entity;

import java.io.Serializable;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description 反向结果信息类，包含地址，省市县 坐标
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/9/6
 */
public class AddressInfo implements Serializable{
    private static final long serialVersionUID = 0;
    /**
     * 唯一代码，可以是ID，编号
     */
    private String code;
    private String country;
    private String province;
    private String city;
    private String district;
    private String street;
    private String address;
    private PointXY point;
    
    
    @Override
    public String toString() {
    	
    	return this.code+","+this.country+","+this.province+","+this.city+","+this.district+","+this.district+","+this.address+","+this.point;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PointXY getPoint() {
        return point;
    }

    public void setPoint(PointXY point) {
        this.point = point;
    }
}
