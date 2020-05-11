package com.li.knowledgefarm.Util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

public class OkHttpUtils {

	private static final byte[] LOCKER = new byte[0];
	private static OkHttpClient client;
	private static OkHttpUtils okHttpUtils;
	private static Handler handler;//未授权状态提示

	private OkHttpUtils(final Context context) {
		PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
		okhttp3.OkHttpClient.Builder ClientBuilder = new okhttp3.OkHttpClient.Builder();
		ClientBuilder.readTimeout(30, TimeUnit.SECONDS);//读取超时
		ClientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
		ClientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
		ClientBuilder.cookieJar(cookieJar);
		client = ClientBuilder.build();
		handler=new Handler(){
			@Override
			public void handleMessage(@NonNull Message msg) {
				Toast.makeText(context,"登录信息已过期，请重新登录！",Toast.LENGTH_SHORT).show();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
						PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
						AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
						mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
						System.exit(0);
					}
				};
				Timer timer = new Timer();
				timer.schedule(task, 3000);
			}
		};
	}

	/**
	 * @Author li
	 * @param context
	 * @return okhttp3.OkHttpClient
	 * @Description 得到连接对象
	 * @Date 9:48 2020/5/11
	 **/
	public static OkHttpClient getInstance(Context context) {
		if (client == null) {
			synchronized (LOCKER) {
				if (client == null) {
					okHttpUtils=new OkHttpUtils(context);
				}
			}
		}
		return client;
	}

	/**
	 * @Author li
	 * @param code
	 * @return void
	 * @Description 处理未授权状态
	 * @Date 15:31 2020/5/11
	 **/
	public static void unauthorized(int code){
		if (code==401){
			handler.sendMessage(new Message());
		}
	}

}