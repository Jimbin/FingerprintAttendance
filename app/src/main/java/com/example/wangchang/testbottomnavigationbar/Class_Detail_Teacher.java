package com.example.wangchang.testbottomnavigationbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Beans.Course;
import Beans.User;

import static java.security.AccessController.getContext;

public class Class_Detail_Teacher extends AppCompatActivity {
    private ListView listView;
    private List<Map<String, Object>> mData;
    private Course course;
    private TextView Name;
    private TextView TeacherName;
    private TextView Detail;
    private TextView Position;
    private TextView Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class__detail__teacher);

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

        SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.record_item,new String[]{"record","time"},new int[]{R.id.Record_item,R.id.Record_time});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Class_Detail_Teacher.this,Record_Detail.class);
                startActivity(intent);
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
        map.put("record", "记录1");
        map.put("time", "5月25日");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("record", "记录2");
        map.put("time", "5月28日");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("record", "记录3");
        map.put("time", "5月29日");
        list.add(map);

        return list;
    }
}
