package com.li.knowledgefarm.Settings;

import android.os.AsyncTask;
import android.widget.TextView;

public class TestCodeEffectAsyncTask extends AsyncTask{
    private TextView tv_testCode;
    private String oldTestCode = "";

    public TestCodeEffectAsyncTask(TextView tv_testCode){
        this.tv_testCode = tv_testCode;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        int i = 119;
        while(i >= 1) {
            if(isCancelled()){
                return null;
            }
            try {
                Thread.sleep(1000);
                publishProgress();
                i--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        if(!tv_testCode.equals("")){
            oldTestCode = (String)tv_testCode.getText();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(tv_testCode.equals(oldTestCode)){
            tv_testCode.setText("");
        }
    }

}
