package cn.bingoogolapple.refreshlayout.salemobile.model;


public class StorageModel {

    private String objectId; //objectId
    private String brandNum; //brand
    private String model; //model
    private String barCode;
    private String modelStorage;
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return  "brandNum:"+brandNum+"model:"+model+"  barCode:"+barCode+" modelStorage:"+modelStorage;
    }

    public StorageModel() {
        // TODO Auto-generated constructor stub
    }
    public StorageModel(String ObjectId, String brandNum, String model,String barCode,String modelStorage) {
        //   public HardwareModel(String Bu,String Ro) {
        super();
        this.objectId = ObjectId;
        this.brandNum = brandNum;
        this.model = model ;
        this.barCode = barCode ;
        this.modelStorage = modelStorage;

    }

    public void setObjectId(String objectId) {   this.objectId = objectId; }
    public String getObjectId() { return objectId; }

    public void setModel(String model) { this.model = model; }
    public String getModel() {  return model;  }

    public void setBrandNum(String brandNum) { this.brandNum = brandNum; }
    public String getBrandNum() {  return brandNum;  }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public String getBarCode() {
        return barCode;
    }

    public void setModelStorage(String modelStorage) {
        this.modelStorage = modelStorage;
    }
    public String getModelStorage() {
        return modelStorage;
    }
}