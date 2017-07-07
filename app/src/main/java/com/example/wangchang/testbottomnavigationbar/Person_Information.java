package com.example.wangchang.testbottomnavigationbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import Beans.User;

import static com.example.wangchang.testbottomnavigationbar.R.id.courseDescription;
import static com.example.wangchang.testbottomnavigationbar.R.id.courseLocation;
import static com.example.wangchang.testbottomnavigationbar.R.id.courseName;
import static com.example.wangchang.testbottomnavigationbar.R.id.courseTime;

public class Person_Information extends AppCompatActivity {
    private EditText UserName;
    private EditText UserId;
    private EditText UserSchool;
    private EditText UserEmail;
    private EditText UserSex;
    private EditText UserPhone;
    private Button submit;

    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person__information);

        init();
    }
    private void init()
    {
        UserName = (EditText)findViewById(R.id.UserName);
        UserId = (EditText)findViewById(R.id.UserId);
        UserSchool = (EditText)findViewById(R.id.UserSchool);
        UserEmail = (EditText)findViewById(R.id.UserEmail);
        UserSex = (EditText)findViewById(R.id.UserSex);
        UserPhone = (EditText)findViewById(R.id.UserPhone);
        submit = (Button) findViewById(R.id.submit);

        String path = "http://www.hitolx.cn:8080/web0427/android/getMemberData.action?sessionId=";//获取用户信息
        path=path+ User.sessionId;
        HttpUtils.getHttpData(path,handler);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "http://www.hitolx.cn:8080/web0427/android/updateMemberData.action";
                String body = "sessionId="+ URLEncoder.encode(User.sessionId)+"&memberName=" + URLEncoder.encode(UserName.getText().toString()) +
                        "&telephone=" + URLEncoder.encode(UserPhone.getText().toString())+"&studentID=" + URLEncoder.encode(UserId.getText().toString())+
                        "&memberSex=" + URLEncoder.encode(UserSex.getText().toString())+"&universityName=" + URLEncoder.encode(UserSchool.getText().toString())+
                        "&email=" + URLEncoder.encode(UserEmail.getText().toString());
                HttpUtils.postHttpData(path,handler,body);
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
                    Toast.makeText(Person_Information.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(Person_Information.this, "获取的CODE码不为200！",
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
        Toast.makeText(Person_Information.this,message,Toast.LENGTH_SHORT).show();
        if (message.equals("用户信息获取成功")){
            JSONObject jsonObject= object.optJSONObject("member");
            UserName.setText(jsonObject.optString("memberName").equals("null")?"":jsonObject.optString("memberName"));
            UserId.setText(jsonObject.optString("studentId").equals("null")?"":jsonObject.optString("studentId"));
            UserSchool.setText(jsonObject.optString("universityName").equals("null")?"":jsonObject.optString("universityName"));
            UserEmail.setText(jsonObject.optString("email").equals("null")?"":jsonObject.optString("email"));
            UserSex.setText(jsonObject.optString("memberSex").equals("null")?"":jsonObject.optString("memberSex"));
            UserPhone.setText(jsonObject.optString("telephone").equals("null")?"":jsonObject.optString("telephone"));
        }if (message.equals("用户信息修改成功")){
            finish();
        }
    }
}
