package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 11/23/2017.
 */
@DatabaseTable
public class tOrderHeader implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtNoTransaksi() {
        return txtNoTransaksi;
    }

    public void setTxtNoTransaksi(String txtNoTransaksi) {
        this.txtNoTransaksi = txtNoTransaksi;
    }

    @DatabaseField(id = true,columnName = "txtGuiID")
    public String txtGuiId;
    @DatabaseField(columnName = "No Transaksi")
    public String txtNoTransaksi;
}
