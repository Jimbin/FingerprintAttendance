package com.example.wangchang.testbottomnavigationbar;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Beans.User;

import static android.R.attr.path;
import static com.example.wangchang.testbottomnavigationbar.R.id.register;
import static com.example.wangchang.testbottomnavigationbar.R.id.submit;
import static com.example.wangchang.testbottomnavigationbar.R.id.toolbar;

public class Register extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private EditText password2;
    private Button   submit;
    private Button   cancel;
    private Toolbar  toolbar;

    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init(){
        account=(EditText) findViewById(R.id.account);
        password=(EditText) findViewById(R.id.password);
        password2=(EditText)findViewById(R.id.password2);
        submit=(Button) findViewById(R.id.submit);
        cancel=(Button) findViewById(R.id.cancel);
        toolbar=(Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(password2.getText().toString())) {
                    String path = "http://www.hitolx.cn:8080/web0427/android/member_registered.action?memberAccount=";
                    path = path + account.getText().toString() + "&psWord=" + password.getText().toString();
                    HttpUtils.getHttpData(path, handler);
                }else{
                    Toast.makeText(Register.this,"两次密码不同",Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(Register.this, "获取数据失败", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case ERRORCODE:
                    Toast.makeText(Register.this, "获取的CODE码不为200！",
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
            User.sessionId = sessionId;
            Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(Register.this,MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(Register.this, "注册失败", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
