package cn.lfz.windynote.constant;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePal;


/**
 * Created by Administrator on 2017/9/26.
 * 全局Application类
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(this);
        LitePal.getDatabase();  //访问数据库，更新表版本
    }

    //获取Context
    public static Context getContext(){
        return context;
    }
}
