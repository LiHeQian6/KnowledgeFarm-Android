package com.li.knowledgefarm.Study.Interface;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.li.knowledgefarm.entity.Question3Num;

import java.util.List;

import okhttp3.OkHttpClient;

public abstract class SubjectInterface {

    protected OkHttpClient okHttpClient;
    protected Gson gson;

    public abstract void getQuestion();
    public abstract void getMathHandler(Intent intent);
}
