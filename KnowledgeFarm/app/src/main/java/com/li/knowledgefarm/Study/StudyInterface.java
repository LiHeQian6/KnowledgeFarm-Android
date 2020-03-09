package com.li.knowledgefarm.Study;

public interface StudyInterface{

    /**
     * @Description 播放回答错误音效
     * @Auther 孙建旺
     * @Date 上午 10:00 2020/03/09
     * @Param []
     * @return void
     */
    public void PlayFalseSound();

    /**
     * @Description 播放回答正确音效
     * @Auther 孙建旺
     * @Date 上午 10:00 2020/03/09
     * @Param []
     * @return void
     */
    public void PlayTrueSound();

    /**
     * @Description 加载视图
     * @Auther 孙建旺
     * @Date 上午 10:02 2020/03/09
     * @Param []
     * @return void
     */
    public void getViews();

    /**
     * @Description 注册事件监听器
     * @Auther 孙建旺
     * @Date 上午 10:03 2020/03/09
     * @Param []
     * @return void
     */
    public void registListener();

    /**
     * @Description 询问是否返回
     * @Auther 孙建旺
     * @Date 上午 10:05 2020/03/09
     * @Param []
     * @return void
     */
    public void showIfReturn();

    /**
     * @Description 回答正确获得奖励
     * @Auther 孙建旺
     * @Date 上午 10:05 2020/03/09
     * @Param []
     * @return void
     */
    public void getWaterAndFertilizer();

    /**
     * @Description 获得水和肥料成功
     * @Auther 孙建旺
     * @Date 上午 10:09 2020/03/09
     * @Param []
     * @return void
     */
    public void getWandFCallBack();

    /**
     * @Description 展示下一道题目
     * @Auther 孙建旺
     * @Date 上午 10:10 2020/03/09
     * @Param []
     * @return void
     */
    public void showQuestion(int pos);

    /**
     * @Description 设置标题栏
     * @Auther 孙建旺
     * @Date 上午 10:16 2020/03/09
     * @Param []
     * @return void
     */
    public void setStatusBar();
}
