package com.kalbe.project.templatemobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalbe.project.templatemobile.Data.clsSwipeList;
import com.kalbe.project.templatemobile.R;

import java.util.List;

/**
 * Created by ASUS ZE on 18/04/2017.
 */

public class AppAdapterViewCusBase extends BaseAdapter {

    private Context context;
    private List<clsSwipeList> mAppList;

    public AppAdapterViewCusBase(Context context, List<clsSwipeList> mAppList) {
        this.context = context;
        this.mAppList = mAppList;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public clsSwipeList getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_app2, null);
            new ViewHolder(convertView);
        }
        clsSwipeList item = getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tv_id.setText(item.get_txtTitle());
        holder.tv_name.setText(item.get_txtDescription());
        holder.tv_status.setText(item.get_txtDescription2());
        if(item.get_txtDescription2().equals("Sync")){
            holder.tv_status.setTextColor(Color.RED);
        } else if (item.get_txtDescription2().equals("Submit")){
            holder.tv_status.setTextColor(Color.BLUE);
        } else if(item.get_txtDescription2().equals("Saved")){
            holder.tv_status.setTextColor(Color.DKGRAY);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_id;
        TextView tv_name;
        TextView tv_status;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.icon);
            tv_id = (TextView) view.findViewById(R.id.title);
            tv_name = (TextView) view.findViewById(R.id.description);
            tv_status = (TextView) view.findViewById(R.id.description1);

            iv_icon.setVisibility(View.GONE);
            view.setTag(this);
        }
    }
}

