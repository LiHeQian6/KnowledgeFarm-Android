package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.li.knowledgefarm.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetTestCodeAsyncTask extends AsyncTask<Object,Integer,Object> {
    private Context context;
    private TextView tv_email;
    private TextView tv_getTestCode;
    private TextView tv_testCode;
    private String email;
    private OkHttpClient okHttpClient;

    public GetTestCodeAsyncTask(Context context, TextView tv_email, TextView tv_getTestCode, TextView tv_testCode, String email){
        this.context = context;
        this.tv_email = tv_email;
        this.tv_getTestCode = tv_getTestCode;
        this.tv_testCode = tv_testCode;
        this.email = email;
        okHttpClient = new OkHttpClient();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if(isCancelled()){
            return null;
        }
        FormBody formBody = new FormBody.Builder().add("email", email).build();
        final Request request = new Request.Builder().post(formBody).url(context.getResources().getString(R.string.URL)+"/user/sendTestCodeBingEmail").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lww","请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.equals("already")){
                    Toast.makeText(context,"该邮箱已被其他账号绑定",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    tv_testCode.setText(result);
                }
            }
        });
        int i = 60;
        while(i >= 1){
            try {
                if(isCancelled()){
                    return null;
                }
                Thread.sleep(1000);
                i--;
                publishProgress(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        super.onProgressUpdate(values);
        tv_email.setText("正在向" + email + "发送验证码...");
        tv_getTestCode.setText(values[0] + "秒后可重新发送");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        tv_email.setText("");
        tv_getTestCode.setText("获取验证码");
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        tv_email.setText("");
        tv_getTestCode.setText("获取验证码");
    }

}
