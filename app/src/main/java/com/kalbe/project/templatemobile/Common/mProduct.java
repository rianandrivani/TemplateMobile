package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by Rian Andrivani on 11/23/2017.
 */
@DatabaseTable
public class mProduct implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtProductCode() {
        return txtProductCode;
    }

    public void setTxtProductCode(String txtProductCode) {
        this.txtProductCode = txtProductCode;
    }

    public String getTxtProductName() {
        return txtProductName;
    }

    public void setTxtProductName(String txtProductName) {
        this.txtProductName = txtProductName;
    }

    public Double getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(Double txtPrice) {
        this.txtPrice = txtPrice;
    }


    @DatabaseField(id = true,columnName = "txtGuiID")
    public String txtGuiId;
    @DatabaseField(columnName = "txtProductCode")
    public String txtProductCode;
    @DatabaseField(columnName = "txtProductName")
    public String txtProductName;
    @DatabaseField(columnName = "Price")
    public Double txtPrice;
}
