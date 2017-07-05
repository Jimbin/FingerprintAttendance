package com.example.wangchang.testbottomnavigationbar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 打错的明天 on 2017/5/15.
 */
public class AccountFragment extends Fragment{
    private List<StringAndInt> list;
    private ListView item_list;
    private static Context mcontext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        item_list= (ListView) view.findViewById(R.id.item_list);
        initList();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public static AccountFragment newInstance(String content,Context context) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        mcontext=context;
        return fragment;
    }
    private void initList(){
        list=new ArrayList<StringAndInt>();
        list.add(new StringAndInt("个人信息",R.drawable.person,true));
        list.add(new StringAndInt("商务合作",R.drawable.business,false));
        list.add(new StringAndInt("我的记录",R.drawable.sign,true));
        list.add(new StringAndInt("使用帮助",R.drawable.help,false));
        list.add(new StringAndInt("用户反馈",R.drawable.feedback,false));
        list.add(new StringAndInt("关于我们",R.drawable.about_us,true));
        list.add(new StringAndInt("设置",R.drawable.setting_,true));
        ListitemAdaper itemListAdapter=new ListitemAdaper(mcontext,list);
        item_list.setAdapter(itemListAdapter);
    }
}
