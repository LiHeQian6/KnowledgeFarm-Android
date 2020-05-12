package com.li.knowledgefarm.Study.Interface;

import android.content.Intent;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;

public abstract class SubjectInterface {

    protected OkHttpClient okHttpClient;
    protected Gson gson;

    public abstract void getQuestion();
    public abstract void getMathHandler(Intent intent);
}
