package com.example.wangchang.testbottomnavigationbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jim斌 on 2017/7/1.
 */

public class ListitemAdaper extends BaseAdapter {
    private List<StringAndInt>list;
    private LayoutInflater mInflater;
    private Context context;
    public ListitemAdaper(Context context, List<StringAndInt> list){
        mInflater=LayoutInflater.from(context);
        this.list=list;
        this.context=context;
    }
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=mInflater.inflate(R.layout.setting_list_item,null);
            viewHolder=new ViewHolder();
            viewHolder.viewIcon=(ImageView)convertView.findViewById(R.id.icon);
            viewHolder.viewItem_Name=(TextView)convertView.findViewById(R.id.item_name);
            viewHolder.viewArrow=(ImageView)convertView.findViewById(R.id.arrow);
            viewHolder.gap_view=(View)convertView.findViewById(R.id.gap_view);
            viewHolder.small_gap_view=(View)convertView.findViewById(R.id.small_gap_view);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.viewItem_Name.setText(list.get(position).getName());
        viewHolder.viewIcon.setImageResource(list.get(position).getID());
        if(!list.get(position).isView_flag()){
            viewHolder.gap_view.setVisibility(View.GONE);
        }
        if(viewHolder.gap_view.getVisibility()==View.VISIBLE){
            viewHolder.small_gap_view.setVisibility(View.GONE);
        }
        viewHolder.viewArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0) {
                    Intent intent = new Intent(context, Person_Information.class);
                    context.startActivity(intent);
                }if (position==7){
                    SharedPreferences sp= context.getSharedPreferences("dataInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("autologin",false);
                    editor.commit();
                    context.startActivity(new Intent(context,Login.class));
                }
            }
        });
        return convertView;
    }

    public final class ViewHolder{
        public ImageView viewIcon;
        public TextView viewItem_Name;
        public ImageView viewArrow;
        public View gap_view;
        public View small_gap_view;
    }
}
