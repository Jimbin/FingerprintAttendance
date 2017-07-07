package com.example.wangchang.testbottomnavigationbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Beans.Course;

public class Class_Detail_Student extends BaseActivity {
    private ListView listView;
    private List<Map<String, Object>> mData;
    private Button Open;
    private Course course;
    private TextView Name;
    private TextView TeacherName;
    private TextView Detail;
    private TextView Position;
    private TextView Time;

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

        mData = getData();

        SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.record_item,
                new String[]{"time","YesOrNo"},new int[]{R.id.Record_item,R.id.Record_time});
        listView.setAdapter(adapter);

        Open=(Button)findViewById(R.id.Open);
        Open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FingerPrint_Fragment passwordDialogFragment = new FingerPrint_Fragment();
                passwordDialogFragment.show(getFragmentManager(), "PasswordDialogFragment");
            }
        });

        Intent intent = getIntent();
        course=(Course)intent.getSerializableExtra("course");
        Detail.setText(course.getCourseDescribe());
        Name.setText(course.getCourseName());
        Position.setText(course.getCourseLocation());
        Time.setText(course.getCourseTime());
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

        map = new HashMap<String, Object>();
        map.put("time", "5月28日");
        map.put("YesOrNo", "尚未签到");
        list.add(map);

        return list;
    }
}
