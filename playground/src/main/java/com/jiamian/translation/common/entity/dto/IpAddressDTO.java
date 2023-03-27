package com.jiamian.translation.common.entity.dto;

/**
 * Created by oliver.huang on 2018/2/7.
 * {"ip":"222.36.15.7","country":"中国","area":"","region":"天津","city":"天津","county":"XX","isp":"铁通","country_id":"CN","area_id":"","region_id":"120000","city_id":"120100","county_id":"xx","isp_id":"100020"}
 */
public class IpAddressDTO {

    private String ip;

    private String country;

    private String area;

    private String region;

    private String city;

    private String county;

    private String isp;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }
}
