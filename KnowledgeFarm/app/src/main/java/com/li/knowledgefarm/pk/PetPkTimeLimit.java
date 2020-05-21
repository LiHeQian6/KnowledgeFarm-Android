package com.li.knowledgefarm.pk;

import android.os.AsyncTask;
import android.widget.TextView;

public class PetPkTimeLimit extends AsyncTask<String,Integer,String> {

    private TextView time_limit;
    private Stop stop;

    public PetPkTimeLimit(TextView textView,Stop stop) {
        this.time_limit = textView;
        this.stop=stop;
    }

    /**
     * @Description 执行耗时任务
     * @Author 孙建旺
     * @Date 下午4:40 2020/05/19
     * @Param [strings]
     * @return java.lang.String
     */
    @Override
    protected String doInBackground(String... strings) {
        int i = 3;
        try {
            while (i >= 0&&!isCancelled()){
                publishProgress(i--);
                Thread.sleep(1000);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @Description 更新UI
     * @Author 孙建旺
     * @Date 下午4:41 2020/05/19
     * @Param [values]
     * @return void
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        time_limit.setText(values[0]+"");
    }

    /**
     * @Description 任务结束
     * @Author 孙建旺
     * @Date 下午4:41 2020/05/19
     * @Param [s]
     * @return void
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        time_limit.setText("第"+PkActivity.ROUND_COUNT+"回合");
        stop.onStop();

    }

    public interface Stop{
        void onStop();
    }
}
