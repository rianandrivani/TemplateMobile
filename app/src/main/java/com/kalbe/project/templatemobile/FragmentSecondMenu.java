package com.kalbe.project.templatemobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kalbe.project.templatemobile.Common.clsLogin;
import com.kalbe.project.templatemobile.Common.clsToken;
import com.kalbe.project.templatemobile.Common.mProduct;
import com.kalbe.project.templatemobile.Data.VolleyResponseListener;
import com.kalbe.project.templatemobile.Data.VolleyUtils;
import com.kalbe.project.templatemobile.Data.clsHardCode;
import com.kalbe.project.templatemobile.Repo.clsLoginRepo;
import com.kalbe.project.templatemobile.Repo.clsPhotoProfilRepo;
import com.kalbe.project.templatemobile.Repo.clsTokenRepo;
import com.kalbe.project.templatemobile.Repo.mProductRepo;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentSecondMenu extends Fragment {
    View v;
    Context context;

    private Spinner spnMasterProduct;
    private Button btnDownload;
    public String accessToken, refreshToken;

    clsLoginRepo loginRepo = null;
    clsTokenRepo tokenRepo = null;
    mProductRepo productRepo = null;
    List<clsLogin> dataLogin = null;
    List<clsToken> dataToken = null;
    List<mProduct> dtProduct = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragment_second_menu, container, false);
        context = getActivity().getApplicationContext();

        spnMasterProduct = (Spinner) v.findViewById(R.id.spnMasterProduct);
        btnDownload = (Button) v.findViewById(R.id.btnDownload);

        try {
            loginRepo = new clsLoginRepo(context);
            tokenRepo = new clsTokenRepo(context);
            productRepo = new mProductRepo(context);
            dtProduct = (List<mProduct>) productRepo.findAll();
            dataLogin = (List<clsLogin>) loginRepo.findAll();
            dataToken = (List<clsToken>) tokenRepo.findAll();

            if (dtProduct.size() > 0) {
                showProduct();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        accessToken = dataToken.get(0).txtUserToken;

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dtProduct.size() > 0)
                    Toast.makeText(context, "Product Available", Toast.LENGTH_SHORT).show();
                else
                    downloadMaster();
            }
        });

        return v;
    }

    public void downloadMaster() {
        String strLinkAPI = new clsHardCode().linkMaster;
        final String mRequestBody = ""; // parameter

        volleyDownloadMaster(strLinkAPI, mRequestBody, "Please Wait...", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsn = jsonObject.getJSONObject("result");
                        String warn = jsn.getString("txtMessage");
                        String result = jsn.getString("intResult");

                        if (result.equals("1")) {
                            JSONArray jsonMaster = jsonObject.getJSONArray("listProduct");
                            for (int i = 0; i < jsonMaster.length(); i++) {
                                JSONObject jsonobject = jsonMaster.getJSONObject(i);
                                String txtGuiID = jsonobject.getString("txtGuiID");
                                String txtPrice = jsonobject.getString("txtPrice");
                                String txtProductCode = jsonobject.getString("txtProductCode");
                                String txtProductName = jsonobject.getString("txtProductName");

                                mProduct data = new mProduct();
                                data.setTxtGuiId(txtGuiID);
                                data.setTxtProductCode(txtProductCode);
                                data.setTxtProductName(txtProductName);
                                data.setTxtPrice(Double.valueOf(txtPrice));

                                mProductRepo repo = new mProductRepo(context);
                                repo.createOrUpdate(data);

                                insertProduct();
                            }
                        } else {
                            Toast.makeText(context, "Something Failed...", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(context, warn, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void volleyDownloadMaster(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        final String[] body = new String[1];
        final String[] message = new String[1];
        RequestQueue queue = Volley.newRequestQueue(context);
        final ProgressDialog Dialog = new ProgressDialog(getActivity());
        Dialog.setMessage(progressBarType);
        Dialog.setCancelable(false);
        Dialog.show();

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        StringRequest request = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response, status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String strLinkAPI = new clsHardCode().linkToken;
                String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    try {
                        // body for value error response
                        body[0] = new String(error.networkResponse.data,"UTF-8");
                        JSONObject jsonObject = new JSONObject(body[0]);
                        message[0] = jsonObject.getString("Message");
                        //Toast.makeText(context, "Error 401, " + message[0], Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new VolleyUtils().requestTokenWithRefresh(getActivity(), strLinkAPI, refresh_token, "z/iQZAGiEmA+ygHJ+UvmcA3Ij/xrAGQPYzwyp1FI9IE=", new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, Boolean status, String strErrorMsg) {
                            if (response != null) {
                                try {
                                    String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                                    JSONObject jsonObject = new JSONObject(response);
                                    accessToken = jsonObject.getString("access_token");
                                    refreshToken = jsonObject.getString("refresh_token");
                                    String dtIssued = jsonObject.getString(".issued");

                                    clsToken data = new clsToken();
                                    data.setIntId("1");
                                    data.setDtIssuedToken(dtIssued);
                                    data.setTxtUserToken(accessToken);
                                    data.setTxtRefreshToken(refreshToken);

                                    tokenRepo.createOrUpdate(data);
                                    Toast.makeText(context, "Success get new Access Token", Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    if (refreshToken != null) {
                        downloadMaster();
                        finalDialog1.dismiss();
                    }
                    finalDialog1.dismiss();

                } else {
                    Toast.makeText(context, "Error 500, Server Error", Toast.LENGTH_SHORT).show();
                    finalDialog1.dismiss();
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);

                return headers;
            }
        };
        request.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }

    private void insertProduct() {
        mProduct data = new mProduct();
        data.setTxtGuiId("1");
        data.setTxtProductCode("ENT");
        data.setTxtProductName("Entrasol");
        data.setTxtPrice((double) 18000);

        mProduct data2 = new mProduct();
        data2.setTxtGuiId("2");
        data2.setTxtProductCode("PREN");
        data2.setTxtProductName("Prenagen");
        data2.setTxtPrice((double) 25000);

        mProduct data3 = new mProduct();
        data3.setTxtGuiId("3");
        data3.setTxtProductCode("MIL");
        data3.setTxtProductName("Milna Biskuit");
        data3.setTxtPrice((double) 5000);

        mProduct data4 = new mProduct();
        data4.setTxtGuiId("4");
        data4.setTxtProductCode("FTBR");
        data4.setTxtProductName("Fitbar");
        data4.setTxtPrice((double) 3000);

        mProduct data5 = new mProduct();
        data5.setTxtGuiId("5");
        data5.setTxtProductCode("DTBSL");
        data5.setTxtProductName("Diabetasol");
        data5.setTxtPrice((double) 12000);

        mProduct data6 = new mProduct();
        data6.setTxtGuiId("6");
        data6.setTxtProductCode("CHLG");
        data6.setTxtProductName("Chilgo");
        data6.setTxtPrice((double) 4500);

        mProduct data7 = new mProduct();
        data7.setTxtGuiId("10");
        data7.setTxtProductCode("BNCL");
        data7.setTxtProductName("Benecol");
        data7.setTxtPrice((double) 3000);

        mProduct data8 = new mProduct();
        data8.setTxtGuiId("11");
        data8.setTxtProductCode("ZE");
        data8.setTxtProductName("Zee");
        data8.setTxtPrice((double) 15000);

        mProductRepo productRepo = new mProductRepo(context);
        productRepo.createOrUpdate(data);
        productRepo.createOrUpdate(data2);
        productRepo.createOrUpdate(data3);
        productRepo.createOrUpdate(data4);
        productRepo.createOrUpdate(data5);
        productRepo.createOrUpdate(data6);
        productRepo.createOrUpdate(data7);
        productRepo.createOrUpdate(data8);

        showProduct();
    }

    private void showProduct() {
        try {
            productRepo = new mProductRepo(context);
            dtProduct = (List<mProduct>) productRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Spinner Drop down elements
        List<String> product = new ArrayList<String>();
        product.add("Product Name");
        if (dtProduct.size() > 0) {
            for (mProduct mProduct : dtProduct) {
                product.add(mProduct.txtProductName);
            }
        }

        // Creating adapter for spinnerTelp
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, product);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Initializing an ArrayAdapter with initial text like select one
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_dropdown_item, product){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // attaching data adapter to spinner
        spnMasterProduct.setAdapter(spinnerArrayAdapter);
    }
}
