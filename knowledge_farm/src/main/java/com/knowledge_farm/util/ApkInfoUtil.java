package com.knowledge_farm.util;

/**
 * @ClassName AnalyseApk
 * @Description
 * @Author 张帅华
 * @Date 2020-05-30 18:03
 */
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.UseFeature;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ApkInfoUtil {
    public static Map<String, Object> readAPK(File apkUrl) {
        Map<String, Object> resMap = new HashMap<>();
        try (ApkFile apkFile = new ApkFile(apkUrl)) {
            ApkMeta apkMeta = apkFile.getApkMeta();
            resMap.put("fileName", apkMeta.getName());
            resMap.put("packageName", apkMeta.getPackageName());
            resMap.put("versionCode", apkMeta.getVersionCode());
            resMap.put("versionName", apkMeta.getVersionName());
            for (UseFeature feature : apkMeta.getUsesFeatures()) {
                System.out.println("featureName" + feature.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resMap;
    }

}
