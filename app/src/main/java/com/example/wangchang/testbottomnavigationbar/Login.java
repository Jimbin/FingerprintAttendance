package com.example.wangchang.testbottomnavigationbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Beans.User;

public class Login extends BaseActivity {
    private EditText account;
    private EditText password;
    private Button   submit;
    private Button   register;
    private Toolbar  toolbar;

    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

    }
//    public static void show(){
//        Log.d("weizhi", "纬度: "+mlatitute1+'\n'+"经度"+mlongitute1);
//
//    }
    private void init(){
        account=(EditText) findViewById(R.id.account);
        password=(EditText) findViewById(R.id.password);
        submit=(Button) findViewById(R.id.submit);
        register=(Button) findViewById(R.id.register);
        toolbar=(Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = "http://www.hitolx.cn:8080/web0427/android/member_login.action?memberAccount=";
                path=path+ account.getText().toString()+"&psWord="+password.getText().toString();
                HttpUtils.getHttpData(path,handler);
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
                    Toast.makeText(Login.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(Login.this, "获取的CODE码不为200！",
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
        String result = object.optString("result");
        if (result.equals("success")) {
            String sessionId = object.optString("sessionId");
            login(sessionId);
        }else{
            Toast.makeText(Login.this, "用户密码输入错误", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void login(String sessionId)
    {
        User.sessionId = sessionId;
        SharedPreferences sp = getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username",account.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.commit();

        Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(Login.this,MainActivity.class);
        startActivity(intent);
    }
}
