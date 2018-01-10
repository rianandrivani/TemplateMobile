package com.kalbe.project.templatemobile.Repo;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.kalbe.project.templatemobile.Common.clsPhotoProfile;
import com.kalbe.project.templatemobile.Data.DatabaseHelper;
import com.kalbe.project.templatemobile.Data.DatabaseManager;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */

public class clsPhotoProfilRepo implements crud {
    private DatabaseHelper helper;
    public clsPhotoProfilRepo(Context context){
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }
    @Override
    public int create(Object item) {
        int index = -1;
        clsPhotoProfile object = (clsPhotoProfile) item;
        try {
            index = helper.getProfileDao().create(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int createOrUpdate(Object item) {
        int index = -1;
        clsPhotoProfile object = (clsPhotoProfile) item;
        try {
            Dao.CreateOrUpdateStatus status = helper.getProfileDao().createOrUpdate(object);
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
        clsPhotoProfile object = (clsPhotoProfile) item;
        try {
            index = helper.getProfileDao().update(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        clsPhotoProfile object = (clsPhotoProfile) item;
        try {
            index = helper.getProfileDao().delete(object);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) throws SQLException {
        clsPhotoProfile item = null;
        try{
            item = helper.getProfileDao().queryForId(id);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public List<?> findAll() throws SQLException {
        List<clsPhotoProfile> items = null;
        try{
            items = helper.getProfileDao().queryForAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }
}
