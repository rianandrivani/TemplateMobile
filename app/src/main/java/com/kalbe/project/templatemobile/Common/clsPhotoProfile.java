package com.kalbe.project.templatemobile.Common;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */
@DatabaseTable
public class clsPhotoProfile implements Serializable {
    public String getTxtGuiId() {
        return txtGuiId;
    }

    public void setTxtGuiId(String txtGuiId) {
        this.txtGuiId = txtGuiId;
    }

    public String getTxtDescription() {
        return txtDescription;
    }

    public void setTxtDescription(String txtDescription) {
        this.txtDescription = txtDescription;
    }

    public byte[] getTxtImg() {
        return txtImg;
    }

    public void setTxtImg(byte[] txtImg) {
        this.txtImg = txtImg;
    }

    @DatabaseField(id = true,columnName = "txtGuiId")
    public String txtGuiId;
    @DatabaseField(columnName = "txtDescription")
    public String txtDescription;
    @DatabaseField(columnName = "txtImg", dataType = DataType.BYTE_ARRAY)
    private byte[] txtImg;
}
