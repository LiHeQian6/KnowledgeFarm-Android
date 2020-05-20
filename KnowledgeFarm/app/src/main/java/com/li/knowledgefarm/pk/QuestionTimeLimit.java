package com.li.knowledgefarm.pk;

import android.os.AsyncTask;
import android.widget.TextView;

public class QuestionTimeLimit extends AsyncTask<String,Integer,String> {

    private TextView time_limit;
    private PetPkQuestionDialog.OnAnswerSelectListener listener;
    private PetPkQuestionDialog dialog;
    private long startTime;
    private int time = 10;

    public QuestionTimeLimit(TextView time_limit, PetPkQuestionDialog.OnAnswerSelectListener listener, PetPkQuestionDialog dialog, long startTime) {
        this.time_limit = time_limit;
        this.listener = listener;
        this.dialog = dialog;
        this.startTime = startTime;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            while (time>=0){
                publishProgress(time--);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        time_limit.setText("倒计时"+values[0]+"秒");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        time = 10;
        listener.select(false,System.currentTimeMillis() - startTime);
        dialog.dismiss();
    }
}
