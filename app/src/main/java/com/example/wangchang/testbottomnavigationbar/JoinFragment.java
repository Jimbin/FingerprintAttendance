package com.example.wangchang.testbottomnavigationbar;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Beans.Course;
import Beans.User;

import static android.R.attr.button;

/**
 * Created by 打错的明天 on 2017/5/15.
 */
public class JoinFragment extends Fragment{
    private ListView listView;
    private List<Map<String, Object>> mData;
    private List<Course> Courses;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;

    private FloatingActionButton fab_del,fab_add,fab_menu;
    private boolean IsOpen=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_fragment, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    //07.04  Jim
    private void init(){

        initfab();
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinCourse passwordDialogFragment = new JoinCourse();
                passwordDialogFragment.show(getActivity().getFragmentManager(), "JoinCourseFragment");
            }
        });
        fab_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除课程
                for (int i=0;i<Courses.size();i++)
                {
                    mData.get(i).put("Button","删除");
                }
                MyAdapter myAdapter=new MyAdapter(getActivity().getBaseContext());
                listView.setAdapter(myAdapter);
            }
        });
        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsOpen){
                    //按钮回收
                    endAnimation(fab_menu,0);
                    endAnimation(fab_add,1);
                    endAnimation(fab_del,2);
                    IsOpen=false;
                }else {
                    //按钮展开
                    startAnimation(fab_menu,0);
                    startAnimation(fab_add,1);
                    startAnimation(fab_del,2);
                    IsOpen=true;
                }
            }
        });
    }
    private void initfab(){
        fab_add= (FloatingActionButton)getActivity().findViewById(R.id.fab_add);
        fab_del= (FloatingActionButton)getActivity().findViewById(R.id.fab_del);
        fab_menu= (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);
    }
    //07.04  Jim
    //按钮动画实现
    private void startAnimation(View view,int i) {
        PropertyValuesHolder t1=PropertyValuesHolder.ofFloat( "translationY",0f,-i*150f);
        PropertyValuesHolder r1=PropertyValuesHolder.ofFloat("rotation",0.0f,360.0f);
        ObjectAnimator.ofPropertyValuesHolder(view,t1,r1).setDuration(500).start();
    }
    //07.04  Jim
    private void endAnimation(View view,int i) {
        PropertyValuesHolder t1=PropertyValuesHolder.ofFloat( "translationY",-i*150f,0f);
        PropertyValuesHolder r1=PropertyValuesHolder.ofFloat("rotation",0.0f,360.0f);
        ObjectAnimator.ofPropertyValuesHolder(view,t1,r1).setDuration(500).start();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        //tv.setText(getArguments().getString("ARGS"));
        listView = (ListView) getActivity().findViewById(R.id.JoinListView);


        String path = "http://www.hitolx.cn:8080/web0427/android/getJoinCoursesAll.action?sessionId=";
        path=path+ User.sessionId;
        HttpUtils.getHttpData(path,handler);
        init();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.add_item:
                JoinCourse passwordDialogFragment = new JoinCourse();
                passwordDialogFragment.show(getActivity().getFragmentManager(), "JoinCourseFragment");

                break;
            case R.id.remove_item:

                for (int i=0;i<Courses.size();i++)
                {
                    mData.get(i).put("Button","删除");
                }
                MyAdapter myAdapter=new MyAdapter(getActivity().getBaseContext());
                listView.setAdapter(myAdapter);
                break;
        }
//         Toast.makeText(MainActivity.this, ""+item.getItemId(), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    public static JoinFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        JoinFragment fragment = new JoinFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i=0;i<Courses.size();i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ClassName", Courses.get(i).getCourseName());
            map.put("ClassTime", Courses.get(i).getCourseTime());
            map.put("ClassLocation", Courses.get(i).getCourseLocation());
            map.put("Button","详情");
            list.add(map);
        }
        return list;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    JSONAnalysis(msg.obj.toString());
                    //Toast.makeText(getActivity(), "获取数据成功", Toast.LENGTH_SHORT)
                    //        .show();
                    break;

                case FAILURE:
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(getActivity(), "获取的CODE码不为200！",
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        };
    };

    /**
     * JSON解析方法
     */
    protected void JSONAnalysis(String string) {
        JSONObject object = null;

        try {
            object = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String message = object.optString("message");
        if(message.equals("查询课程学生信息成功")){
        try {
            Courses = new ArrayList<Course>();

            JSONArray jsonArray=object.getJSONArray("courseVOs");
            for (int i=0;i<jsonArray.length();i++)
            {
                Course temp = new Course();
                JSONObject ObjectInfo = jsonArray.optJSONObject(i);
                temp.setCourseId(ObjectInfo.optString("courseId")==null?"":ObjectInfo.optString("courseId"));
                temp.setCourseDescribe(ObjectInfo.optString("courseDescribe")==null?"":ObjectInfo.optString("courseDescribe"));
                temp.setCourseLocation(ObjectInfo.optString("courseLocation")==null?"":ObjectInfo.optString("courseLocation"));
                temp.setCourseName(ObjectInfo.optString("courseName")==null?"":ObjectInfo.optString("courseName"));
                temp.setCourseStatus(ObjectInfo.optString("courseStatus")==null?"":ObjectInfo.optString("courseStatus"));
                temp.setCourseTime(ObjectInfo.optString("courseTime")==null?"":ObjectInfo.optString("courseTime"));
                temp.setCreateTime(ObjectInfo.optString("createTime")==null?"":ObjectInfo.optString("createTime"));
                temp.setMemberId(ObjectInfo.optString("memberId")==null?"":ObjectInfo.optString("memberId"));
                temp.setSignStatus(ObjectInfo.optString("signStatus")==null?"":ObjectInfo.optString("signStatus"));
                temp.setMemberName(ObjectInfo.optString("memberName")==null?"":ObjectInfo.optString("memberName"));
                temp.setLatitude(ObjectInfo.optString("latitude")==null?"":ObjectInfo.optString("latitude"));
                temp.setLongitude(ObjectInfo.optString("longitude")==null?"":ObjectInfo.optString("longitude"));
                Courses.add(temp);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mData = getData();
        MyAdapter myAdapter=new MyAdapter(getActivity().getBaseContext());
        listView.setAdapter(myAdapter);}
        if(message.equals("退出课程成功")){
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            String path = "http://www.hitolx.cn:8080/web0427/android/getJoinCoursesAll.action?sessionId=";
            path=path+ User.sessionId;
            HttpUtils.getHttpData(path,handler);
        }else {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }

    public final class ViewHolder{
        public TextView ClassName;
        public TextView ClassTime;
        public TextView ClassLocation;
        public LinearLayout item;
        public Button button;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            final int pos=position;
            final String button;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.listview_item, null);
                holder.ClassName = (TextView) convertView.findViewById(R.id.ClassName);
                holder.ClassTime = (TextView) convertView.findViewById(R.id.ClassTime);
                holder.ClassLocation = (TextView) convertView.findViewById(R.id.ClassLocation);
                holder.item=(LinearLayout)convertView.findViewById(R.id.item);
                holder.button=(Button)convertView.findViewById(R.id.detail);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.ClassName.setText((String)mData.get(position).get("ClassName"));
            holder.ClassTime.setText((String)mData.get(position).get("ClassTime"));
            holder.ClassLocation.setText((String)mData.get(position).get("ClassLocation"));
            holder.button.setText((String)mData.get(position).get("Button"));
            button =  holder.button.getText().toString();

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),Class_Detail_Student.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course", Courses.get(pos));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View view) {
                    if (button.equals("删除")){
                        String url="http://www.hitolx.cn:8080/web0427/android/exitCourse.action";
                        String body = "sessionId="+ URLEncoder.encode(User.sessionId)+"&courseId=" + URLEncoder.encode(Courses.get(pos).getCourseId());
                        HttpUtils.postHttpData(url,handler,body);
                    }else{
                        Intent intent = new Intent(getActivity(),Class_Detail_Student.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", Courses.get(pos));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });


            return convertView;
        }

    }
}
