package cn.bingoogolapple.refreshlayout.salemobile.model;

//not use RefreshMode,use HardwareModel
// Template
// {"Bu": "6-215","Ro": "护士站","Em":"yes","Ap":"676668","Eq":"Printer","Ac":"李三","Ph":"676768","St":"Accepter","Other":"nothing"}
public class RefreshModel  {

    private String objectId; //objectId
    private String brand; //brand

    private String totalsale; //Building
    private String brandNum; //brandNum

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return  "brand:"+brand+"  totalsale:"+totalsale+"  brandNum:"+brandNum+" objectId:"+objectId;
    }

    public RefreshModel() {
        // TODO Auto-generated constructor stub
    }
    public RefreshModel(String ObjectId,String brand,String totalsale,String brandNum) {
        //   public HardwareModel(String Bu,String Ro) {
        super();
        this.objectId = ObjectId;
        this.brand = brand;
        this.totalsale = totalsale ;
        this.brandNum=brandNum;

    }

    public void setObjectId(String objectId) {   this.objectId = objectId; }
    public String getObjectId() { return objectId; }

    public void setBrand(String brand) { this.brand = brand; }
    public String getBrand() {  return brand;  }


    public void setTotalsale(String totalsale) {
        this.totalsale = totalsale;
    }
    public String getTotalsale() {
        return totalsale;
    }

    public void setBrandNum(String brandNum) { this.brandNum = brandNum; }
    public String getBrandNum() {  return brandNum;  }
}