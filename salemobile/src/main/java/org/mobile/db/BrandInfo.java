package org.mobile.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "BRAND_INFO".
 */
public class BrandInfo {

    private Long id;
    private String brandNum;
    private String brand;
    private java.util.Date date;
    private String objectId;

    public BrandInfo() {
    }

    public BrandInfo(Long id) {
        this.id = id;
    }

    public BrandInfo(Long id, String brandNum, String brand, java.util.Date date, String objectId) {
        this.id = id;
        this.brandNum = brandNum;
        this.brand = brand;
        this.date = date;
        this.objectId = objectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandNum() {
        return brandNum;
    }

    public void setBrandNum(String brandNum) {
        this.brandNum = brandNum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
