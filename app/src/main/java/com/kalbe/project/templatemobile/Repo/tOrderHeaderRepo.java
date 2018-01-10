package com.kalbe.project.templatemobile.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kalbe.project.templatemobile.Common.tOrderHeader;
import com.kalbe.project.templatemobile.Data.DatabaseHelper;
import com.kalbe.project.templatemobile.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Rian Andrivani on 11/23/2017.
 */

public class tOrderHeaderRepo implements crud {
    private DatabaseHelper helper;
    public tOrderHeaderRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        tOrderHeader object = (tOrderHeader) item;
        try {
            index = helper.getOrderHeaderDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        tOrderHeader object = (tOrderHeader) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getOrderHeaderDao().createOrUpdate(object);
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
        tOrderHeader object = (tOrderHeader) item;
        try {
            index = helper.getOrderHeaderDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        tOrderHeader object = (tOrderHeader) item;
        try {
            index = helper.getOrderHeaderDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        tOrderHeader item = null;
        try{
            item = helper.getOrderHeaderDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<tOrderHeader> items = null;
        try{
            items = helper.getOrderHeaderDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
