package org.mobile.db;
import cm.hardwarereport.example.Constants;
import  android.database.sqlite.SQLiteDatabase;
import cn.bingoogolapple.refreshlayout.salemobile.App;
/**
 * Created by tchl on 3/14/16.
 */
public class DBLoader {

    private static DaoMaster daoMaster = null;
    private static DaoSession daoSession = null;
    private static SQLiteDatabase db = null;

    public static void init(){
        MyDevOpenHelper helper = new MyDevOpenHelper(App.getInstance(),Constants.DB_NAME,null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        if (daoSession==null){
            init();
        }
        return daoSession;
    }
}
