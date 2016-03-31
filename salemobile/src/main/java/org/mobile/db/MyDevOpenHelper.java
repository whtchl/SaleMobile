package org.mobile.db;
import android.content.Context;
import  android.database.sqlite.SQLiteDatabase;
/**
 * Created by tchl on 3/14/16.
 */
public class MyDevOpenHelper extends DaoMaster.OpenHelper {

    public MyDevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory){
        super(context,name,factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                //TODO
                //数据库升级时在此处理
                //否则升级后数据库会清空

                //创建新表
                //UserInfoDao.createTable(db,true);

                //加入新字段
                //db.execSQL("ALTER TABLE USER_INFO ADD RONG_TOKEN VARCHAR(50)");
                break;
            default:
                break;
        }
    }
}