package com.example.wangchang.testbottomnavigationbar;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Beans.Course;
import Beans.User;

import static com.example.wangchang.testbottomnavigationbar.BaseActivity.mlatitute1;
import static com.example.wangchang.testbottomnavigationbar.BaseActivity.mlongitute1;
import static com.example.wangchang.testbottomnavigationbar.R.drawable.sign;

public class Class_Detail_Student extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private Button Open;
    private Course course;
    private TextView Name;
    private TextView TeacherName;
    private TextView Detail;
    private TextView Position;
    private TextView Time;

    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class__detail__student);

        init();
    }

    private void init()
    {
        listView = (ListView) findViewById(R.id.Record);
        Name = (TextView)findViewById(R.id.Name);
        TeacherName = (TextView)findViewById(R.id.TeacherName);
        Detail = (TextView)findViewById(R.id.Detail);
        Position = (TextView)findViewById(R.id.Position);
        Time = (TextView)findViewById(R.id.Time);

        Open=(Button)findViewById(R.id.Open);
        Open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "http://www.hitolx.cn:8080/web0427/android/getJoinCourseOne.action?sessionId=";
                path=path+ User.sessionId+"&courseId="+course.getCourseId();
                HttpUtils.getHttpData(path,handler);
            }
        });

        Intent intent = getIntent();
        course=(Course)intent.getSerializableExtra("course");
        Detail.setText(course.getCourseDescribe());
        Name.setText(course.getCourseName());
        Position.setText(course.getCourseLocation());
        Time.setText(course.getCourseTime());
        TeacherName.setText(course.getMemberName());

        String path = "http://www.hitolx.cn:8080/web0427/android/getAllSign.action?sessionId=";//获取签到列表
        path=path+ User.sessionId+"&courseId="+course.getCourseId();
        HttpUtils.getHttpData(path,handler);
    }

    private void Start()
    {
        FingerPrint_Fragment passwordDialogFragment = new FingerPrint_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("courseId", course.getCourseId());
        bundle.putString("longitude",course.getLongitude());
        bundle.putString("latitude",course.getLatitude());
        passwordDialogFragment.setArguments(bundle);
        passwordDialogFragment.show(getFragmentManager(), "PasswordDialogFragment");

    }

    private void Close()
    {
        Toast.makeText(Class_Detail_Student.this,"尚未开启签到",Toast.LENGTH_SHORT).show();
    }

    public void sign()
    {
        String path = "http://www.hitolx.cn:8080/web0427/android/signCourses.action?sessionId=";
        path=path+ User.sessionId+"&courseId="+course.getCourseId();
        HttpUtils.getHttpData(path,handler);
    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", "5月25日");
        map.put("YesOrNo", "已签到");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("time", "5月28日");
        map.put("YesOrNo", "尚未签到");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("time", "5月28日");
        map.put("YesOrNo", "尚未签到");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("time", "5月28日");
        map.put("YesOrNo", "尚未签到");
        list.add(map);

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
                    Toast.makeText(Class_Detail_Student.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(Class_Detail_Student.this, "获取的CODE码不为200！",
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
        /**
         * 在你获取的string这个JSON对象中，提取你所需要的信息。
         */
        String result = object.optString("result");
        String message = object.optString("message");
        if(message.equals("获取学生签到信息成功")){
            try {
                JSONArray jsonArray=object.getJSONArray("signVOs");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject ObjectInfo = jsonArray.optJSONObject(i);
                    String time=ObjectInfo.optString("startTime")==null?"":ObjectInfo.optString("startTime");
                    time = time.substring(5,10);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("time", time);
                    map.put("YesOrNo", ObjectInfo.optString("isSign")==null?"":ObjectInfo.optString("isSign"));
                    mData.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.record_item,new String[]{"time","YesOrNo"},new int[]{R.id.Record_item,R.id.Record_time});
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();}
        }if(message.equals("查询课程信息成功")){
            JSONObject ObjectInfo = object.optJSONObject("courseVO");
            course.setCourseId(ObjectInfo.optString("courseId")==null?"":ObjectInfo.optString("courseId"));
            course.setCourseDescribe(ObjectInfo.optString("courseDescribe")==null?"":ObjectInfo.optString("courseDescribe"));
            course.setCourseLocation(ObjectInfo.optString("courseLocation")==null?"":ObjectInfo.optString("courseLocation"));
            course.setCourseName(ObjectInfo.optString("courseName")==null?"":ObjectInfo.optString("courseName"));
            course.setCourseStatus(ObjectInfo.optString("courseStatus")==null?"":ObjectInfo.optString("courseStatus"));
            course.setCourseTime(ObjectInfo.optString("courseTime")==null?"":ObjectInfo.optString("courseTime"));
            course.setCreateTime(ObjectInfo.optString("createTime")==null?"":ObjectInfo.optString("createTime"));
            course.setMemberId(ObjectInfo.optString("memberId")==null?"":ObjectInfo.optString("memberId"));
            course.setSignStatus(ObjectInfo.optString("signStatus")==null?"":ObjectInfo.optString("signStatus"));
            course.setMemberName(ObjectInfo.optString("memberName")==null?"":ObjectInfo.optString("memberName"));

            if (!course.getSignStatus().equals("close")){
                Start();
            }else{
                Close();
            }
        }else{
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        }

    }

}
