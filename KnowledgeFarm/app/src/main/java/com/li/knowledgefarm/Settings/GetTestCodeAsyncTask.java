package com.li.knowledgefarm.Settings;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;

public class GetTestCodeAsyncTask extends AsyncTask<Object,Integer,Object> {
    private Context context;
    private Button tv_getTestCode;

    public GetTestCodeAsyncTask(Context context, Button tv_getTestCode){
        this.context = context;
        this.tv_getTestCode = tv_getTestCode;
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
        tv_getTestCode.setText(values[0] + "秒后可重新发送");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        tv_getTestCode.setText("获取验证码");
        tv_getTestCode.setClickable(true);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        tv_getTestCode.setText("获取验证码");
        tv_getTestCode.setClickable(true);
    }

}
