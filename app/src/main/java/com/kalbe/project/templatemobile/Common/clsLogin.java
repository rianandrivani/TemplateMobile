package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 11/21/2017.
 */
@DatabaseTable
public class clsLogin implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(String txtUsername) {
        this.txtUsername = txtUsername;
    }

    public String getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(String txtPassword) {
        this.txtPassword = txtPassword;
    }

    public String getDtLogin() {
        return dtLogin;
    }

    public void setDtLogin(String dtLogin) {
        this.dtLogin = dtLogin;
    }

    public String getTxtImei() {
        return txtImei;
    }

    public void setTxtImei(String txtImei) {
        this.txtImei = txtImei;
    }

    public String getTxtDeviceName() {
        return txtDeviceName;
    }

    public void setTxtDeviceName(String txtDeviceName) {
        this.txtDeviceName = txtDeviceName;
    }

    @DatabaseField(id = true,columnName = "txtGuiID")
    public String txtGuiId;
    @DatabaseField(columnName = "txtUsername")
    public String txtUsername;
    @DatabaseField(columnName = "txtPassword")
    public String txtPassword;
    @DatabaseField(columnName = "dtLogin")
    public String dtLogin;
    @DatabaseField(columnName = "txtImei")
    public String txtImei;
    @DatabaseField(columnName = "txtDeviceName")
    public String txtDeviceName;
}
