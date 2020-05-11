package com.li.knowledgefarm.Util;

import android.content.Context;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkHttpUtils {

	private static final byte[] LOCKER = new byte[0];
	private static OkHttpClient client;

	private OkHttpUtils(Context context) {
		PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
		okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder();
		ClientBuilder.readTimeout(30, TimeUnit.SECONDS);//读取超时
		ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
		ClientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
		ClientBuilder.cookieJar(cookieJar);
		client = ClientBuilder.build();
	}

	public static OkHttpClient getInstance(Context context) {
		if (client == null) {
			synchronized (LOCKER) {
				if (client == null) {
					new OkHttpUtils(context);
				}
			}
		}
		return client;
	}
}