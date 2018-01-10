package com.kalbe.project.templatemobile.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kalbe.project.templatemobile.Common.mProduct;
import com.kalbe.project.templatemobile.Common.tOrderDetail;
import com.kalbe.project.templatemobile.Data.DatabaseHelper;
import com.kalbe.project.templatemobile.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Rian Andrivani on 11/23/2017.
 */

public class tOrderDetailRepo implements crud {
    private DatabaseHelper helper;
    public tOrderDetailRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        tOrderDetail object = (tOrderDetail) item;
        try {
            index = helper.getOrderDetailDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        tOrderDetail object = (tOrderDetail) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getOrderDetailDao().createOrUpdate(object);
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
        tOrderDetail object = (tOrderDetail) item;
        try {
            index = helper.getOrderDetailDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        tOrderDetail object = (tOrderDetail) item;
        try {
            index = helper.getOrderDetailDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        tOrderDetail item = null;
        try{
            item = helper.getOrderDetailDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<tOrderDetail> items = null;
        try{
            items = helper.getOrderDetailDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<tOrderDetail> findByIdString(String id) throws SQLException {
        List<tOrderDetail> item = null;
        try {
            item = helper.getOrderDetailDao().queryBuilder().where().eq("txtHeaderID", id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

}
