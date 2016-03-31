package cn.bingoogolapple.refreshlayout.salemobile.model;

/**
 * Created by tchl on 2016/3/16.
 */
public class DetailMobileSaleInfo {
    private String objectId; //objectId
    private String brand; //brand
    private String model; //model
    private String barCode;
    private int price;

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return  "brand:"+brand+"model:"+model+"  barCode:"+barCode+" price:"+price;
    }

    public DetailMobileSaleInfo() {
        // TODO Auto-generated constructor stub
    }
    public DetailMobileSaleInfo(String ObjectId, String brand, String model,String barCode,int price ) {
        super();
        this.objectId = ObjectId;
        this.brand = brand;
        this.model = model ;
        this.barCode = barCode ;
        this.price = price;

    }

    public void setObjectId(String objectId) {   this.objectId = objectId; }
    public String getObjectId() { return objectId; }

    public void setModel(String model) { this.model = model; }
    public String getModel() {  return model;  }

    public void setBrand(String brand) { this.brand = brand; }
    public String getBrand() {  return brand;  }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getBarCode() {
        return barCode;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getPrice() {
        return price;
    }

}
