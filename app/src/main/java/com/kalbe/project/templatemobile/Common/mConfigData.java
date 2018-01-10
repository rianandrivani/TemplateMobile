package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Rian Andrivani on 11/21/2017.
 */
@DatabaseTable
public class mConfigData {
    @DatabaseField(id = true,columnName = "intId")
    public Integer intId;
    @DatabaseField(columnName = "txtName")
    public String txtName;
    @DatabaseField(columnName = "txtValue")
    public String txtValue;
    @DatabaseField(columnName = "txtDefaultValue")
    public String txtDefaultValue;
    @DatabaseField(columnName = "intEditAdmin")
    public String intEditAdmin;

    public synchronized Integer getIntId() {
        return intId;
    }

    public synchronized void setIntId(Integer intId) {
        this.intId = intId;
    }

    public synchronized String getTxtName() {
        return txtName;
    }

    public synchronized void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public synchronized String getTxtValue() {
        return txtValue;
    }

    public synchronized void setTxtValue(String txtValue) {
        this.txtValue = txtValue;
    }

    public String getTxtDefaultValue() {
        return txtDefaultValue;
    }

    public synchronized void setTxtDefaultValue(String txtDefaultValue) {
        this.txtDefaultValue = txtDefaultValue;
    }

    public synchronized String getIntEditAdmin() {
        return intEditAdmin;
    }

    public synchronized void setIntEditAdmin(String intEditAdmin) {
        this.intEditAdmin = intEditAdmin;
    }

    public mConfigData(){

    }
}
