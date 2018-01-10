package com.kalbe.project.templatemobile.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kalbe.project.templatemobile.Common.clsLogin;
import com.kalbe.project.templatemobile.Data.DatabaseHelper;
import com.kalbe.project.templatemobile.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */

public class clsLoginRepo implements crud {
    private DatabaseHelper helper;
    public clsLoginRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsLogin object = (clsLogin) item;
        try {
            index = helper.getLoginDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsLogin object = (clsLogin) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getLoginDao().createOrUpdate(object);
            index = status.getNumLinesChanged();
//            index = 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        clsLogin object = (clsLogin) item;
        try {
            index = helper.getLoginDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsLogin object = (clsLogin) item;
        try {
            index = helper.getLoginDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsLogin item = null;
        try{
            item = helper.getLoginDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsLogin> items = null;
        try{
            items = helper.getLoginDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
