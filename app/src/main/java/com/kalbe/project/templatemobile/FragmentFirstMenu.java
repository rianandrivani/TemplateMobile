package com.kalbe.project.templatemobile;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.kalbe.project.templatemobile.BL.clsActivity;
import com.kalbe.project.templatemobile.Common.mProduct;
import com.kalbe.project.templatemobile.Common.tOrderDetail;
import com.kalbe.project.templatemobile.Common.tOrderHeader;
import com.kalbe.project.templatemobile.Data.clsSwipeList;
import com.kalbe.project.templatemobile.Repo.mProductRepo;
import com.kalbe.project.templatemobile.Repo.tOrderDetailRepo;
import com.kalbe.project.templatemobile.Repo.tOrderHeaderRepo;
import com.kalbe.project.templatemobile.adapter.AppAdapterViewCusBase;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.swu.pulltorefreshswipemenulistview.library.PullToRefreshSwipeMenuListView;
import edu.swu.pulltorefreshswipemenulistview.library.pulltorefresh.interfaces.IXListViewListener;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.bean.SwipeMenu;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.SwipeMenuCreator;
import edu.swu.pulltorefreshswipemenulistview.library.util.RefreshTime;

public class FragmentFirstMenu extends Fragment implements IXListViewListener {
    View v;
    Context context;
    private static List<clsSwipeList> swipeList = new ArrayList<clsSwipeList>();
    private AppAdapterViewCusBase mAdapter;
    private PullToRefreshSwipeMenuListView mListView;
    private Handler mHandler;
    private static Map<String, HashMap> mapMenu;
    private SliderLayout mDemoSlider;
    private FloatingActionButton fab;

    mProductRepo productRepo;
    tOrderHeaderRepo orderHeaderRepo;
    tOrderDetailRepo orderDetailRepo;
    List<tOrderHeader> dtHeader;
    List<tOrderDetail> dtDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragment_first_menu, container, false);
        context = getActivity().getApplicationContext();

        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        // click Button +
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                toolbar.setTitle("Sub Menu-1");

                FragmentSubFirstMenu fragmentSubMenu = new FragmentSubFirstMenu();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragmentSubMenu);
                fragmentTransaction.commit();
            }
        });

        loadData();
        return v;
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
                mListView.stopRefresh();
                mListView.stopLoadMore();
            }
        }, 500);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 1);
    }

    private void onLoad() {
        mListView.setRefreshTime(RefreshTime.getRefreshTime(getActivity().getApplicationContext()));
        mListView.stopRefresh();
        mListView.stopLoadMore();
    }

    private void viewList(Context ctx, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        final View promptView = layoutInflater.inflate(R.layout.preview, null);

        try {
            dtDetail = (List<tOrderDetail>) orderDetailRepo.findByIdString(dtHeader.get(position).getTxtGuiId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final TextView _tvNoSO = (TextView) promptView.findViewById(R.id.tvnoSOtbl);
        final TextView _tvKet = (TextView) promptView.findViewById(R.id.tvkettbl);
        _tvNoSO.setText(": " + dtHeader.get(position).getTxtNoTransaksi());
        _tvKet.setText(": " + dtHeader.get(position).getTxtNoTransaksi());
        final TextView tv_item = (TextView) promptView.findViewById(R.id.tvItemtbl);
        tv_item.setTypeface(null, Typeface.BOLD);
        tv_item.setText(": " + String.valueOf(dtDetail.size()));
        final  TextView tv_amount = (TextView) promptView.findViewById(R.id.tvSumAmount) ;
        tv_amount.setTypeface(null, Typeface.BOLD);
        tv_amount.setText(": " + String.valueOf(dtHeader.get(position).getTxtNoTransaksi()));
        final  TextView tv_status = (TextView) promptView.findViewById(R.id.tvStatus);
        tv_status.setTypeface(null, Typeface.BOLD);
        tv_status.setText(": Sync");

        TableLayout tlb = (TableLayout) promptView.findViewById(R.id.tlProduct);
        tlb.removeAllViews();

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        params.setMargins(1, 1, 1, 1);

        TableRow tr = new TableRow(getContext());

        TableLayout tl = new TableLayout(getContext());

        String[] colTextHeader = {"Nama", "Qty", "Price", "Amount"};

        for (String text : colTextHeader) {
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));

            tv.setTextSize(14);
            tv.setPadding(10, 10, 10, 10);
            tv.setText(text);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(Color.parseColor("#4CAF50"));
            tv.setTextColor(Color.WHITE);
            tr.addView(tv,params);
        }
        tl.addView(tr);

        double qtySum=0;
        double qtyNum;
        int i = 0;
        List<mProduct> dtJoin = null;
        for(tOrderDetail dat : dtDetail){
            try {
                orderDetailRepo = new tOrderDetailRepo(context);
                dtJoin = (List<mProduct>) productRepo.findJoin(dtDetail.get(i).getProduct().txtGuiId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            tr = new TableRow(getContext());
            TableLayout.LayoutParams tableRowParams=
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

            int leftMargin=0;
            int topMargin=0;
            int rightMargin=0;
            int bottomMargin=0;
            tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

            tr.setLayoutParams(tableRowParams);

            TextView product = new TextView(getContext());
            product.setTextSize(12);
            product.setWidth(200);
            product.setPadding(10, 10, 10, 10);
            product.setBackgroundColor(Color.parseColor("#f0f0f0"));
            product.setTextColor(Color.BLACK);
            product.setText(dtJoin.get(0).txtProductName);
            tr.addView(product,params);

            TextView qty = new TextView(getContext());
            qty.setTextSize(12);
            qty.setPadding(10, 10, 10, 10);
            qty.setBackgroundColor(Color.parseColor("#f0f0f0"));
            qty.setTextColor(Color.BLACK);
            qty.setGravity(Gravity.RIGHT);
            qty.setText(dat.getTxtQuantity());
            tr.addView(qty,params);

            TextView price = new TextView(getContext());
            price.setTextSize(12);
            price.setPadding(10, 10, 10, 10);
            price.setBackgroundColor(Color.parseColor("#f0f0f0"));
            price.setTextColor(Color.BLACK);
            price.setGravity(Gravity.RIGHT);
            price.setText(dtJoin.get(0).txtPrice.toString());
            tr.addView(price,params);

            TextView amount = new TextView(getContext());
            amount.setTextSize(12);
            amount.setWidth(200);
            amount.setPadding(10, 10, 10, 10);
            amount.setBackgroundColor(Color.parseColor("#f0f0f0"));
            amount.setTextColor(Color.BLACK);
            amount.setGravity(Gravity.RIGHT);
            double prc = Double.valueOf(dtJoin.get(0).txtPrice.toString());
            double itm = Double.valueOf(dat.getTxtQuantity());
            qtyNum = prc * itm;
            qtySum += qtyNum;
            amount.setText(new clsActivity().convertNumberDec(qtyNum));
            tr.addView(amount,params);

            tl.addView(tr, tableRowParams);
            i++;
        }

        tlb.addView(tl);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    private void loadData() {
        clsSwipeList swplist;
        productRepo = new mProductRepo(context);
        orderHeaderRepo = new tOrderHeaderRepo(context);
        orderDetailRepo = new tOrderDetailRepo(context);

        try {
            dtHeader = (List<tOrderHeader>) orderHeaderRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        swipeList.clear();

        if (dtHeader.size() > 0) {
            for (int i = 0; i < dtHeader.size(); i++) {
                swplist = new clsSwipeList();
                swplist.set_txtTitle("Id : " + dtHeader.get(i).getTxtGuiId());
                swplist.set_txtDescription("Description : " + dtHeader.get(i).getTxtNoTransaksi());
                swplist.set_txtDescription2("Submit");
//                if (dtHeader.get(i).get_intSubmit().equals("1")&&dtHeader.get(i).get_intSync().equals("0")){
//                    swplist.set_txtDescription2("Submit");
//                } else if (dtHeader.get(i).get_intSubmit().equals("1")&&dtHeader.get(i).get_intSync().equals("1")){
//                    swplist.set_txtDescription2("Sync");
//                }

                swipeList.add(swplist);
            }
        }

        mListView = (PullToRefreshSwipeMenuListView) v.findViewById(R.id.listView);
        mAdapter = new clsActivity().setListViewCusBase(context, swipeList);
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setEmptyView(v.findViewById(R.id.LayoutEmpty));
        mListView.setXListViewListener(this);
        mHandler = new Handler();

        HashMap<String, String> mapView = new HashMap<String, String>();

        mapView.put("name", "View");
        mapView.put("bgColor", "#3498db");

        mapMenu = new HashMap<String, HashMap>();
        mapMenu.put("0", mapView);

        SwipeMenuCreator creator = new clsActivity().setCreator(context, mapMenu);
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                clsSwipeList item = swipeList.get(position);
                switch (index) {
                    case 0:
                        viewList(context, position);
                        break;
                }
            }
        });

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        RefreshTime.setRefreshTime(getContext(), " " + df.format(new Date()));
        mListView.setRefreshTime(RefreshTime.getRefreshTime(getActivity().getApplicationContext()));
    }
}
