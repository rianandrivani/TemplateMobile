package com.kalbe.project.templatemobile.Common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalbe.project.templatemobile.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {

    private Context context;
    private List<String> mAppList;

    public AppAdapter(Context context, List<String> mAppList) {
        this.context = context;
        this.mAppList = mAppList;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public String getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_app, null);
            new ViewHolder(convertView);
        }
        String item = getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tv_name.setText(item);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);

            iv_icon.setVisibility(View.GONE);
            view.setTag(this);
        }
    }
}
