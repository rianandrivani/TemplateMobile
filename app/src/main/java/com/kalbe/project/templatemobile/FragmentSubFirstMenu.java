package com.kalbe.project.templatemobile;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kalbe.project.templatemobile.BL.clsActivity;
import com.kalbe.project.templatemobile.Common.AppAdapter;
import com.kalbe.project.templatemobile.Common.mProduct;
import com.kalbe.project.templatemobile.Common.tOrderDetail;
import com.kalbe.project.templatemobile.Common.tOrderHeader;
import com.kalbe.project.templatemobile.Data.clsHardCode;
import com.kalbe.project.templatemobile.Data.clsSwipeList;
import com.kalbe.project.templatemobile.Repo.mProductRepo;
import com.kalbe.project.templatemobile.Repo.tOrderDetailRepo;
import com.kalbe.project.templatemobile.Repo.tOrderHeaderRepo;

import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import edu.swu.pulltorefreshswipemenulistview.library.PullToRefreshSwipeMenuListView;
import edu.swu.pulltorefreshswipemenulistview.library.pulltorefresh.interfaces.IXListViewListener;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.bean.SwipeMenu;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.SwipeMenuCreator;


public class FragmentSubFirstMenu extends Fragment implements IXListViewListener{
    View v;
    Context context;
    private TextView tv_date, tv_noTransaksi;
    private Button btnAddProduct, btnSave;
    PullToRefreshSwipeMenuListView mListView;

    List<mProduct> dtProduct = null;
    List<tOrderHeader> dataHeader = null;
    List<tOrderDetail> dtProductDetail = null;
    mProductRepo productRepo;
    tOrderHeaderRepo orderHeaderRepo;
    tOrderDetailRepo orderDetailRepo;
    private HashMap<String, String> hashMapSpinnerPrice = new HashMap<>();
    private List<clsSwipeList> swipeList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragment_sub_first_menu, container, false);
        context = getActivity().getApplicationContext();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tv_noTransaksi = (TextView) v.findViewById(R.id.txtNoTransaksi);
        tv_date = (TextView) v.findViewById(R.id.tvDate);
        btnAddProduct = (Button) v.findViewById(R.id.btnAdd);
        btnSave = (Button) v.findViewById(R.id.btnSave);

        try {
            orderHeaderRepo = new tOrderHeaderRepo(context);
            dataHeader = (List<tOrderHeader>) orderHeaderRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tv_noTransaksi.setText("A-331" + String.valueOf(dataHeader.size()+1));
        // add date in txtviewDateQuantity
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault()).format(new Date());
        tv_date.setText(timeStamp);

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    productRepo = new mProductRepo(context);
                    dtProduct = (List<mProduct>) productRepo.findAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (dtProduct.size() > 0) {
                    popupAddProduct(new tOrderDetail());
                } else {
                    Toast.makeText(context, "Please download list product", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

                builder.setTitle("Save Transaction");
                builder.setMessage("Are you sure to save?");
                builder.setCancelable(false);

                builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (dtProductDetail.size() == 0) {
                            Toast.makeText(context, "Please add 1 least product...", Toast.LENGTH_SHORT).show();
                        } else {
                            save();
//                            try {
//                                new clsHardCode().copydb(context);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            dialog.dismiss();
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });

        tableProduct();
        return v;
    }

    private void popupAddProduct(final tOrderDetail dataDetail) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View promptView = layoutInflater.inflate(R.layout.popup_add_product, null);
        final EditText etQuantity;
        final Spinner spinnerProduct;

        etQuantity = (EditText) promptView.findViewById(R.id.etQuantity);
        spinnerProduct = (Spinner) promptView.findViewById(R.id.spnProduct);

        orderHeaderRepo = new tOrderHeaderRepo(context);

        try {
            dataHeader = (List<tOrderHeader>) orderHeaderRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            productRepo = new mProductRepo(context);
            dtProduct = (List<mProduct>) productRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Spinner Drop down elements
        List<String> product = new ArrayList<String>();
//        product.add("Select One");
        if (dtProduct.size() > 0) {
            for (mProduct mProduct : dtProduct) {
                product.add(mProduct.txtProductName);
                hashMapSpinnerPrice.put(mProduct.txtProductName, String.valueOf(mProduct.txtPrice));
            }
        }

        // Initializing an ArrayAdapter with initial text like select one
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, product) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Select One");

        // attaching data adapter to spinner
        spinnerProduct.setAdapter(adapter);
        spinnerProduct.setSelection(adapter.getCount());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                            }
                        })
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

        // for edit data in popupAddQuantity
        int index = 0;
        if (dataDetail.getTxtGuiId() != null) {
            List<mProduct> dtJoin = null;
            try {
                productRepo = new mProductRepo(context);
                dtJoin = (List<mProduct>) productRepo.findJoin(dataDetail.getProduct().txtGuiId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String item = dtJoin.get(index).getTxtProductName();
            for (int i = 0; i < spinnerProduct.getAdapter().getCount() - 1; i++) {
                if (spinnerProduct.getItemAtPosition(i).equals(item)) {
                    index = i;
                }
            }
            spinnerProduct.setSelection(index);
            etQuantity.setText(dataDetail.getTxtQuantity());
        }

        final List<tOrderHeader> finalDataHeader = dataHeader;
        alertD.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String product = spinnerProduct.getSelectedItem().toString();
                final String quantiry = etQuantity.getText().toString();
                double qtySum=0;
                double qtyNum;
                List<mProduct> dtProductID = null;

                if (product.equals("Select One")){
                    Toast.makeText(context, "Have to choose product", Toast.LENGTH_SHORT).show();
                } else if (quantiry.equals("")){
                    Toast.makeText(context, "Please Fill Quantity", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Your product : " + product, Toast.LENGTH_SHORT).show();

                    try {
                        productRepo = new mProductRepo(context);
                        dtProductID = (List<mProduct>) productRepo.findByIdString(product);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    mProduct mProduct = new mProduct();
                    mProduct.setTxtGuiId(dtProductID.get(0).txtGuiId);
                    mProduct.setTxtProductCode(dtProductID.get(0).txtProductCode);
                    mProduct.setTxtProductName(dtProductID.get(0).txtProductName);
                    mProduct.setTxtPrice(dtProductID.get(0).txtPrice);

                    tOrderDetail data = new tOrderDetail();
                    if (dataDetail.getTxtGuiId() != null) {
                        data.setTxtGuiId(dataDetail.getTxtGuiId());
                    } else {
                        data.setTxtGuiId(new clsActivity().GenerateGuid());
                    }
                    data.setTxtHeaderID(String.valueOf(finalDataHeader.size()+1));
                    data.setProduct(mProduct);
                    data.setTxtQuantity(quantiry);

                    double prc = Double.valueOf(hashMapSpinnerPrice.get(product).toString());
                    double itm = Double.valueOf(quantiry);
                    qtyNum = prc * itm;
                    qtySum += qtyNum;
                    data.setTxtTotalPrice(qtyNum);

                    tOrderDetailRepo orderDetailRepo = new tOrderDetailRepo(context);
                    orderDetailRepo.createOrUpdate(data);

                    alertD.dismiss();
                    tableProduct();
                }
            }
        });
    }

    private void tableProduct() {
        try {
            orderHeaderRepo = new tOrderHeaderRepo(context);
            dataHeader = (List<tOrderHeader>) orderHeaderRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            orderDetailRepo = new tOrderDetailRepo(context);
            dtProductDetail = (List<tOrderDetail>) orderDetailRepo.findByIdString(String.valueOf(dataHeader.size()+1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clsSwipeList swplist;

        swipeList.clear();

        int i = 0;
        List<mProduct> dtJoin = null;
        for (tOrderDetail data : dtProductDetail) {
            swplist = new clsSwipeList();
            try {
                productRepo = new mProductRepo(context);
                dtJoin = (List<mProduct>) productRepo.findJoin(dtProductDetail.get(i).getProduct().txtGuiId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            swplist.set_txtTitle("Product : " + dtJoin.get(0).txtProductName);
            swplist.set_txtDescription("Total Product : " + data.getTxtQuantity());
            swipeList.add(swplist);

            i++;
        }

        mListView = (PullToRefreshSwipeMenuListView) v.findViewById(R.id.listViewProduct);
        AppAdapter mAdapter = new clsActivity().setList(getActivity().getApplicationContext(), swipeList);
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(false);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);

        HashMap<String, String> mapEdit = new HashMap<String, String>();
        HashMap<String, String> mapDelete = new HashMap<String, String>();

        mapEdit.put("name", "Edit");
        mapEdit.put("bgColor", "#3498db");

        mapDelete.put("name", "Delete");
        mapDelete.put("bgColor", "#FF4500");

        Map<String, HashMap> mapMenu = new HashMap<String, HashMap>();
        mapMenu.put("0", mapEdit);
        mapMenu.put("1", mapDelete);

        SwipeMenuCreator creator = new clsActivity().setCreator(context, mapMenu);
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                clsSwipeList item = swipeList.get(position);
                switch (index) {
                    case 0:
                        editList(context, position);
                        break;
                    case 1:
                        deleteList(context, position);
                        break;
                }
            }
        });

        setListViewHeightBasedOnItems(mListView);
    }

    private void editList(Context ctx, int position) {
        popupAddProduct(dtProductDetail.get(position));
    }

    private void deleteList(Context ctx, int position) {
        tOrderDetail dtDetail = dtProductDetail.get(position);
        orderDetailRepo.delete(dtDetail);

        tableProduct();
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    private void save() {
        try {
            orderHeaderRepo = new tOrderHeaderRepo(context);
            dataHeader = (List<tOrderHeader>) orderHeaderRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tOrderHeader data = new tOrderHeader();
        data.setTxtGuiId(String.valueOf(dataHeader.size()+1));
        data.setTxtNoTransaksi(tv_noTransaksi.getText().toString());

        orderHeaderRepo.createOrUpdate(data);

        FragmentFirstMenu HeaderMenu = new FragmentFirstMenu();
        FragmentTransaction fragmentTransactionHome = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransactionHome.replace(R.id.frame, HeaderMenu);
        fragmentTransactionHome.commit();

        Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
