package com.kalbe.project.templatemobile.Data;

import android.content.Context;
import android.os.Environment;

import com.kalbe.project.templatemobile.Repo.mConfigRepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Rian Andrivani on 11/7/2017.
 */

public class clsHardCode {
    Context context;
    public String txtPathApp= Environment.getExternalStorageDirectory()+ File.separator+"data"+File.separator+"data"+File.separator+"Template"+File.separator+"app_database"+File.separator;
    public String dbName = "Template.db";
    public String txtFolderData = Environment.getExternalStorageDirectory()+ File.separator+"Android"+File.separator+"data"+File.separator+"KalbeFamily"+File.separator+"image_Person"+File.separator;

    public String linkMaster = new mConfigRepo(context).API + "mProduct";
    public String linkLogin = new mConfigRepo(context).API + "Login";
    public String linkToken = new mConfigRepo(context).APIToken + "token";

    public String copydb(Context context) throws IOException {
        String CURRENT_DATABASE_PATH = "data/data/" + context.getPackageName() + "/databases/"+ new clsHardCode().dbName;

        try {
            File dbFile = new File(CURRENT_DATABASE_PATH);
            FileInputStream fis = new FileInputStream(dbFile);
            String txtPathUserData= Environment.getExternalStorageDirectory()+File.separator+"backupDbTemplate";
            File yourFile = new File(txtPathUserData);
            yourFile.createNewFile();
            OutputStream output = new FileOutputStream(yourFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            fis.close();
        } catch (Exception e) {
            String s= "hahaha";
        }

        return "hehe";
    }
}
