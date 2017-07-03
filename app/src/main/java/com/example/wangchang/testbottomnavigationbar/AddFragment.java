package com.example.wangchang.testbottomnavigationbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Beans.Course;
import Beans.User;

import static android.R.attr.path;
import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static com.example.wangchang.testbottomnavigationbar.R.id.courseId;
import static com.example.wangchang.testbottomnavigationbar.R.id.tv;

/**
 * Created by 打错的明天 on 2017/5/15.
 */
public class AddFragment extends Fragment {
    private ListView listView;
    private List<Map<String, Object>> mData;
    private List<Course> Courses;
    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TextView tv = (TextView) getActivity().findViewById(R.id.tv);
        //tv.setText(getArguments().getString("ARGS"));
        listView = (ListView) getActivity().findViewById(R.id.AddListView);


        String path = "http://www.hitolx.cn:8080/web0427/android/getCoursesBelongMember.action?sessionId=";
        path=path+ User.sessionId;
        HttpUtils.getHttpData(path,handler);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.add_item:
                AddCourse passwordDialogFragment = new AddCourse();
                passwordDialogFragment.show(getActivity().getFragmentManager(), "PasswordDialogFragment");

                break;
            case R.id.remove_item:

                Toast.makeText(getActivity(), ""+"删除", Toast.LENGTH_SHORT).show();
                break;
        }
//         Toast.makeText(MainActivity.this, ""+item.getItemId(), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    public static AddFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        AddFragment fragment = new AddFragment();
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
        Courses = new ArrayList<Course>();

        try {
            object = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String message = object.optString("message");
        if(!message.equals("删除成功")){
        try {
            JSONArray jsonArray=object.getJSONArray("courses");
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

                Courses.add(temp);
            }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        mData = getData();
        MyAdapter myAdapter=new MyAdapter(getActivity().getBaseContext());
        listView.setAdapter(myAdapter);
        }else {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            String path = "http://www.hitolx.cn:8080/web0427/android/getCoursesBelongMember.action?sessionId=";
            path=path+ User.sessionId;
            HttpUtils.getHttpData(path,handler);
        }
    }


    public final class ViewHolder{
        public TextView ClassName;
        public TextView ClassTime;
        public TextView ClassLocation;
        public LinearLayout item;
        public Button button;
    }


    public class MyAdapter extends BaseAdapter{

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
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            final int pos=position;
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

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),Class_Detail_Teacher.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course", Courses.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public  void onClick(View view) {
                    String url="http://www.hitolx.cn:8080/web0427/android/deleteCourse.action";
                    String body = "sessionId="+ URLEncoder.encode(User.sessionId)+"&courseId=" + URLEncoder.encode(Courses.get(position).getCourseId());
                    HttpUtils.postHttpData(url,handler,body);
                }
            });

            return convertView;
        }

    }
}
