package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 11/23/2017.
 */
@DatabaseTable
public class tOrderDetail implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtHeaderID() {
        return txtHeaderID;
    }

    public void setTxtHeaderID(String txtHeaderID) {
        this.txtHeaderID = txtHeaderID;
    }

    public String getTxtQuantity() {
        return txtQuantity;
    }

    public void setTxtQuantity(String txtQuantity) {
        this.txtQuantity = txtQuantity;
    }

    public Double getTxtTotalPrice() {
        return txtTotalPrice;
    }

    public void setTxtTotalPrice(Double txtTotalPrice) {
        this.txtTotalPrice = txtTotalPrice;
    }

    @DatabaseField(id = true,columnName = "txtGuiID")
    public String txtGuiId;
    @DatabaseField(columnName = "txtHeaderID")
    public String txtHeaderID;
    @DatabaseField(columnName = "txtQuantity")
    public String txtQuantity;
    @DatabaseField(columnName = "txtTotalPrice")
    public Double txtTotalPrice;

    public mProduct getProduct() {
        return product;
    }

    public void setProduct(mProduct product) {
        this.product = product;
    }

    @DatabaseField(foreign = true, columnName = "txtProductCode", foreignAutoRefresh = true)
    private mProduct product;
}
