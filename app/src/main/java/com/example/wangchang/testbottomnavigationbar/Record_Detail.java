package com.example.wangchang.testbottomnavigationbar;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Beans.Course;
import Beans.User;

import static android.R.id.list;

public class Record_Detail extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    private TextView signNumber;
    private TextView courseNumber;
    private TextView Record_time;


    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record__detail);

        init();
    }

    private void init()
    {
        signNumber = (TextView)findViewById(R.id.signNumber);
        courseNumber = (TextView)findViewById(R.id.courseNumber);
        Record_time = (TextView)findViewById(R.id.Record_time);
        listView = (ListView)findViewById(R.id.Record_listview);

        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
        String path = "http://www.hitolx.cn:8080/web0427/android/getOneSignInformation.action?sessionId=";
        path=path+ User.sessionId+"&courseId="+bundle.getString("courseId")+"&startCourseId="+bundle.getString("startCourseId");
        HttpUtils.getHttpData(path,handler);

        Record_time.setText(bundle.getString("time"));
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Name", "姓名");
        map.put("Id", "学号");
        map.put("YesOrNo", "是否签到成功");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("Name", "黄嘉琪");
        map.put("Id", "20142100248");
        map.put("YesOrNo", "已签到");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("Name", "黄嘉琪");
        map.put("Id", "20142100248");
        map.put("YesOrNo", "已签到");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("Name", "黄嘉琪");
        map.put("Id", "20142100248");
        map.put("YesOrNo", "已签到");
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
                    Toast.makeText(Record_Detail.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(Record_Detail.this, "获取的CODE码不为200！",
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
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

        courseNumber.setText(object.optString("courseNumber"));
        signNumber.setText(object.optString("signNumber"));
        try {
            JSONArray jsonArray=object.getJSONArray("studentVOs");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("Name", "姓名");
            map.put("Id", "学号");
            map.put("YesOrNo", "是否签到成功");
            mData.add(map);
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject ObjectInfo = jsonArray.optJSONObject(i);
                map = new HashMap<String, Object>();
                map.put("Name", ObjectInfo.optString("memberName")==null?"":ObjectInfo.optString("memberName"));
                map.put("Id", ObjectInfo.optString("studentId")==null?"":ObjectInfo.optString("studentId"));
                map.put("YesOrNo", ObjectInfo.optString("isSign")==null?"":ObjectInfo.optString("isSign"));
                mData.add(map);
            }

            SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.record_listview_item,new String[]{"Name","Id","YesOrNo"},new int[]{R.id.student_name,R.id.student_id,R.id.flag});
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
