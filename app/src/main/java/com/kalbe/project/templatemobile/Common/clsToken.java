package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by User on 1/29/2018.
 */
@DatabaseTable
public class clsToken implements Serializable {
    public String getIntId() {
        return intId;
    }

    public void setIntId(String intId) {
        this.intId = intId;
    }

    public String getDtIssuedToken() {
        return dtIssuedToken;
    }

    public void setDtIssuedToken(String dtIssuedToken) {
        this.dtIssuedToken = dtIssuedToken;
    }

    public String getTxtUserToken() {
        return txtUserToken;
    }

    public void setTxtUserToken(String txtUserToken) {
        this.txtUserToken = txtUserToken;
    }

    public String getTxtRefreshToken() {
        return txtRefreshToken;
    }

    public void setTxtRefreshToken(String txtRefreshToken) {
        this.txtRefreshToken = txtRefreshToken;
    }

    @DatabaseField(id = true,columnName = "intId")
    public String intId;
    @DatabaseField(columnName = "dtIssuedToken")
    public String dtIssuedToken;
    @DatabaseField(columnName = "txtUserToken")
    public String txtUserToken;
    @DatabaseField(columnName = "txtRefreshToken")
    public String txtRefreshToken;
}
