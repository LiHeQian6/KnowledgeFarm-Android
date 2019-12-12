package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class GetTestCodeAsyncTask extends AsyncTask<Object,Integer,Object> {
    private Context context;
    private TextView tv_email;
    private TextView tv_getTestCode;
    private TextView tv_testCode;
    private String email;

    public GetTestCodeAsyncTask(Context context, TextView tv_email, TextView tv_getTestCode, TextView tv_testCode, String email){
        this.context = context;
        this.tv_email = tv_email;
        this.tv_getTestCode = tv_getTestCode;
        this.tv_testCode = tv_testCode;
        this.email = email;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        int i = 59;
        while(i >= 1){
            try {
                if(isCancelled()){
                    return null;
                }
                publishProgress(i);
                Thread.sleep(1000);
                i--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        super.onProgressUpdate(values);
        if(tv_testCode.getText().equals("")){
            tv_email.setText("正在向" + email + "发送验证码...");
        }
        tv_getTestCode.setText(values[0] + "秒后可重新发送");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        tv_email.setText("");
        tv_getTestCode.setText("获取验证码");
        tv_getTestCode.setClickable(true);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        tv_email.setText("");
        tv_getTestCode.setText("获取验证码");
        tv_getTestCode.setClickable(true);
    }

}
