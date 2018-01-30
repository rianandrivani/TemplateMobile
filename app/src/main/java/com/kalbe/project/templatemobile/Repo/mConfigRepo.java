package com.kalbe.project.templatemobile.Repo;

import android.content.Context;

import com.kalbe.project.templatemobile.Common.mConfigData;
import com.kalbe.project.templatemobile.Data.DatabaseHelper;
import com.kalbe.project.templatemobile.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Rian Andrivani on 11/21/2017.
 */

public class mConfigRepo {
    DatabaseHelper helper;
    public String API_menu = "http://template.kalbe.com/abc";
    public String API = "http://10.171.11.101/WebAPITemplate/API/";
    public String APIToken = "http://10.171.11.101/WebAPITemplate/";

    public mConfigRepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    public Object findById(int value) throws SQLException {
        mConfigData item = null;
        try{
            item = helper.getmConfigDao().queryForId(value);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    public List<?> findAll() throws SQLException {
        List<mConfigData> items = null;
        try{
            items = helper.getmConfigDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public void InsertDefaultmConfig() throws SQLException {
        mConfigData data1 = new mConfigData();
        data1.setIntId(1);
        data1.setTxtName("android:versionCode");
        data1.setTxtValue("5");
        data1.setTxtDefaultValue("5");
        data1.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data1);

        mConfigData data2 = new mConfigData();
        data2.setIntId(2);
        data2.setTxtName("API_menu");
        data2.setTxtValue(API_menu);
        data2.setTxtDefaultValue(API_menu);
        data2.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data2);

        mConfigData data3 = new mConfigData();
        data3.setIntId(3);
        data3.setTxtName("Domain Kalbe");
        data3.setTxtValue("ONEKALBE.LOCAL");
        data3.setTxtDefaultValue("ONEKALBE.LOCAL");
        data3.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data3);

        mConfigData data4 = new mConfigData();
        data4.setIntId(4);
        data4.setTxtName("Application Name");
        data4.setTxtValue("Kalbe Template");
        data4.setTxtDefaultValue("3VyizZ7haX2KCvR0wl64YwulEteHqsq5FLncJSL+pBM=");
        data4.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data4);

        mConfigData data5 = new mConfigData();
        data5.setIntId(5);
        data5.setTxtName("UserId");
        data5.setTxtValue("rian.andrivani");
        data5.setTxtDefaultValue("rian.andrivani");
        data5.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data5);

        mConfigData data6 = new mConfigData();
        data6.setIntId(6);
        data6.setTxtName("Text Footer");
        data6.setTxtValue("Copyright &copy; KN IT 2018");
        data6.setTxtDefaultValue("Copyright &copy; KN IT 2018");
        data6.setIntEditAdmin("1");
        helper.getmConfigDao().createOrUpdate(data6);
    }
}
