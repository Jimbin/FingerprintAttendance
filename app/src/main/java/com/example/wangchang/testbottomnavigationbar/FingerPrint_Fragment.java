package com.example.wangchang.testbottomnavigationbar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import Beans.User;

import static android.R.attr.path;
import static com.example.wangchang.testbottomnavigationbar.BaseActivity.mlatitute1;
import static com.example.wangchang.testbottomnavigationbar.BaseActivity.mlongitute1;


public class FingerPrint_Fragment extends DialogFragment {
    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    private FingerprintManagerCompat manager;
    private CancellationSignal mCancellationSignal = new CancellationSignal();
    private TextView mErrorTextView;
    private ImageView mIcon;
    private Button cancel;
    double dis;

    private final int SUCCESS = 1;
    private final int FAILURE = 0;
    private final int ERRORCODE = 2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finger_print_, container);
        mErrorTextView = (TextView)view.findViewById(R.id.fingerprint_status);
        mIcon = (ImageView)view.findViewById(R.id.fingerprint_icon);
        cancel=(Button)view.findViewById(R.id.cancel_button);

        // 获取一个FingerPrintManagerCompat的实例
        manager = FingerprintManagerCompat.from(getActivity());

        if (mCancellationSignal.isCanceled()) {
            mCancellationSignal = new CancellationSignal();
        }
        manager.authenticate(null, 0, mCancellationSignal, new MyCallBack(), null);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancellationSignal.cancel();
                getDialog().dismiss();
            }
        });

        // 如果没有录入指纹，则不能使用指纹识别
        //if (!manager.hasEnrolledFingerprints()) {
        //    Toast.makeText(getActivity(), "您还没有录入指纹, 请在设置界面录入至少一个指纹",
        //            Toast.LENGTH_LONG).show();
        //}

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


    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            Log.d(TAG, "onAuthenticationError: " + errString);
            mErrorTextView.setText(errString);
            mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
            mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
            Thread thread=new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        Thread.sleep(SUCCESS_DELAY_MILLIS);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    getDialog().dismiss();
                }
            });
            thread.start();
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            Log.d(TAG, "onAuthenticationFailed: " + "验证失败");
            showError(mIcon.getResources().getString(
                    R.string.fingerprint_not_recognized));
            //mErrorTextView.setText("验证失败");
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            Log.d(TAG, "onAuthenticationHelp: " + helpString);
            showError(helpString);
            //mErrorTextView.setText(helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult
                                                      result) {
            Log.d(TAG, "onAuthenticationSucceeded: " + "验证成功");
            mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
            mIcon.setImageResource(R.drawable.ic_fingerprint_success);
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.success_color));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_success));
            mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
            mErrorTextView.postDelayed(mResetErrorTextRunnable, SUCCESS_DELAY_MILLIS);


            Thread thread=new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                            Thread.sleep(SUCCESS_DELAY_MILLIS);
                            } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            }
                        sign();
                }
            });
            thread.start();

            //mErrorTextView.setText("验证成功");
        }
    }

    private void showError(CharSequence error) {
        mIcon.setImageResource(R.drawable.ic_fingerprint_error);
        mErrorTextView.setText(error);
        mErrorTextView.setTextColor(
                mErrorTextView.getResources().getColor(R.color.warning_color));
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable);
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);

    }
    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            mErrorTextView.setTextColor(
                    mErrorTextView.getResources().getColor(R.color.hint_color));
            mErrorTextView.setText(
                    mErrorTextView.getResources().getString(R.string.fingerprint_hint));
            mIcon.setImageResource(R.drawable.ic_fp_40px);
        }
    };

    public void sign()
    {
        Bundle bundle=getArguments();
        dis = Distance.Distance(mlongitute1,mlatitute1,Double.valueOf(bundle.getString("longitude","")),Double.valueOf(bundle.getString("latitude","")));
        if (dis<200.0){//如果距离小于200则签到成功，否则失败
            String path = "http://www.hitolx.cn:8080/web0427/android/signCourses.action?sessionId=";
            path=path+ User.sessionId+"&courseId="+bundle.getString("courseId","");
            HttpUtils.getHttpData(path,handler);}
        else{
            Toast.makeText(getActivity(),"你距离老师过远达"+String.valueOf(dis)+"米,签到失败!",Toast.LENGTH_SHORT).show();
        }
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
        Toast.makeText(getActivity(),"你距离老师"+String.valueOf(dis)+"米,"+message,Toast.LENGTH_SHORT).show();
        getDialog().dismiss();

    }

}