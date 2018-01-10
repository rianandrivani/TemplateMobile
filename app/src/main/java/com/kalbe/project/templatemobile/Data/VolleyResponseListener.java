package com.kalbe.project.templatemobile.Data;

/**
 * Created by arick.anjasmara on 22/06/2017.
 */

public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(String response, Boolean status, String strErrorMsg);
}
