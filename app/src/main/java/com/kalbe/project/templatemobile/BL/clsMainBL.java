package com.kalbe.project.templatemobile.BL;

import android.content.Context;

import com.kalbe.project.templatemobile.Common.clsLogin;
import com.kalbe.project.templatemobile.Common.clsStatusMenuStart;
import com.kalbe.project.templatemobile.Repo.clsLoginRepo;
import com.kalbe.project.templatemobile.Repo.enumStatusMenuStart;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */

public class clsMainBL {
    public clsStatusMenuStart checkUserActive(Context context) throws ParseException {
        clsLoginRepo login = new clsLoginRepo(context);
        clsStatusMenuStart _clsStatusMenuStart =new clsStatusMenuStart();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String now = dateFormat.format(cal.getTime()).toString();
//        if(repo.CheckLoginNow()){
        List<clsLogin> listDataLogin = null;
        try {
            listDataLogin = (List<clsLogin>) login.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (clsLogin data : listDataLogin){
            if (!data.getTxtUsername().equals(null)){
                _clsStatusMenuStart.set_intStatus(enumStatusMenuStart.UserActiveLogin);
            }
        }

        return _clsStatusMenuStart;
    }
}
