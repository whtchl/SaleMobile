package com.example;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * 此java工程用于生成
 * greenDao操作数据库需要的
 * DaoMaster  && DaoSession && xxDao && xxEntity
 */
public class MyDaoGenerator {

    public static void main(String[] args) throws Exception{

        //参数1：数据库版本
        //参数2：自动生成代码默认包
        Schema schema = new Schema(2,"org.mobile.db");

        addUserInfo(schema);

        //获取自动输出代码路径
        //避免团队合作时各人项目路径不同
        String url = MyDaoGenerator.class.getResource("/").getFile().toString();
        String[] str = url.split("daogenerator");
        String outDir = str[0] + "salemobile/src/main/java";
        System.out.println(outDir);
        new DaoGenerator().generateAll(schema,outDir);
    }

    /**
     * 信息表
     * @param schema
     */
    private static void addUserInfo(Schema schema){
        Entity brandInfo = schema.addEntity("BrandInfo");
        brandInfo.addIdProperty();
        brandInfo.addStringProperty("brandNum");
        brandInfo.addStringProperty("brand");
        brandInfo.addDateProperty("date");
        brandInfo.addStringProperty("objectId");

        Entity mobileSaleInfo = schema.addEntity("MobileSaleInfo");
        mobileSaleInfo.addIdProperty();
        mobileSaleInfo.addStringProperty("brandNum");
        mobileSaleInfo.addStringProperty("model");
        mobileSaleInfo.addStringProperty("barCode");
        mobileSaleInfo.addStringProperty("modelStorage");
        mobileSaleInfo.addStringProperty("objectId");
        mobileSaleInfo.addDateProperty("date");
    }
}