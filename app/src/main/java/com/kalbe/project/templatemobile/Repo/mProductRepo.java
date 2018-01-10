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

public class mProductRepo implements crud {
    private DatabaseHelper helper;
    public mProductRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        mProduct object = (mProduct) item;
        try {
            index = helper.getProductDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        mProduct object = (mProduct) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getProductDao().createOrUpdate(object);
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
        mProduct object = (mProduct) item;
        try {
            index = helper.getProductDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        mProduct object = (mProduct) item;
        try {
            index = helper.getProductDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        mProduct item = null;
        try{
            item = helper.getProductDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<mProduct> items = null;
        try{
            items = helper.getProductDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }

    public List<mProduct> findByIdString(String txtProductName) throws SQLException {
        List<mProduct> item = null;
        try {
            item = helper.getProductDao().queryBuilder().where().eq("txtProductName", txtProductName).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public List<?> findJoin(String txtProductID) throws SQLException {
        QueryBuilder<tOrderDetail, Integer> orderDetailQb = helper.getOrderDetailDao().queryBuilder();
        QueryBuilder<mProduct, Integer> productQb = helper.getProductDao().queryBuilder();
//        orderDetailQb.join(productQb);
        orderDetailQb.where().like("txtProductCode", txtProductID); // add where clause
//        productQb.join(orderDetailQb);
        List<mProduct> results = productQb.join(orderDetailQb).query();

        return results;
    }

    public List<?> findLeftJoin() throws SQLException {
        QueryBuilder<tOrderDetail, Integer> orderDetailQb = helper.getOrderDetailDao().queryBuilder();
        QueryBuilder<mProduct, Integer> productQb = helper.getProductDao().queryBuilder();
//        orderDetailQb.where().like("txtProductCode", "1");
        List<mProduct> results = productQb.leftJoin(orderDetailQb).query();

        return results;
    }
}
