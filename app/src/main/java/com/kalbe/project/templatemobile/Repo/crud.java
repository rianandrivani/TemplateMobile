package com.kalbe.project.templatemobile.Repo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Robert on 19/06/2017.
 */

interface crud {
    public int create(Object item) throws SQLException;
    public int createOrUpdate(Object item) throws SQLException;
    public int update(Object item)throws SQLException;
    public int delete(Object item)throws SQLException;
    public Object findById(int id)throws SQLException;
    public List<?> findAll()throws SQLException;
}
