package com.example.wangchang.testbottomnavigationbar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.ObbInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import Beans.Course;
import Beans.User;

import static android.R.string.cancel;

/**
 * Created by lele on 2017/6/29.
 */

public class AddCourse extends DialogFragment {
    private Button   submit;
    private Button   cancel;
    private EditText courseName;
    private EditText courseDescription;
    private EditText courseTime;
    private EditText courseLocation;

    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_course, container);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    private void init(View view)
    {
        submit=(Button)view.findViewById(R.id.submit);
        cancel=(Button)view.findViewById(R.id.cancel);
        courseName=(EditText) view.findViewById(R.id.courseName);
        courseDescription=(EditText)view.findViewById(R.id.courseDescription);
        courseTime=(EditText)view.findViewById(R.id.courseTime);
        courseLocation=(EditText)view.findViewById(R.id.courseLocation);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "http://www.hitolx.cn:8080/web0427/android/buildCourse.action";
                String body = "sessionId="+ URLEncoder.encode(User.sessionId)+"&courseName=" + URLEncoder.encode(courseName.getText().toString()) +
                        "&courseTime=" + URLEncoder.encode(courseTime.getText().toString())+"&courseLocation=" + URLEncoder.encode(courseLocation.getText().toString())+
                        "&courseDescribe=" + URLEncoder.encode(courseDescription.getText().toString());
                HttpUtils.postHttpData(path,handler,body);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

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
        /**
         * 在你获取的string这个JSON对象中，提取你所需要的信息。
         */
        String result = object.optString("result");
        String message = object.optString("message");
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        if (message.equals("新增课程成功")) {
            getDialog().dismiss();
        }
    }
}
