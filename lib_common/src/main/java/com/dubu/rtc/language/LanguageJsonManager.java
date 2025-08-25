package com.dubu.rtc.language;

import android.content.Context;
import android.text.TextUtils;

import com.dubu.common.beans.config.LanguageInfo;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by weizhenguo on 2019-10-14.
 */
public class LanguageJsonManager {

    private static String ASSETS_FILE_NAME = "json/language.txt";

    private List<LanguageInfo> countryInfoList;

    private volatile static LanguageJsonManager sInstance;

    private LanguageJsonManager() {

    }

    public static LanguageJsonManager getInstance() {
        if (sInstance == null) {
            synchronized (LanguageJsonManager.class) {
                if (sInstance == null) {
                    sInstance = new LanguageJsonManager();
                }
            }
        }
        return sInstance;
    }


    public List<LanguageInfo> getLanguageList(Context context) {
        if (countryInfoList == null) {
            loadCountryJson(context);
        }
        return countryInfoList;
    }

    public LanguageInfo getLanguageInfo(Context context, String name) {
        if (countryInfoList == null) {
            loadCountryJson(context);
        }

        for (int i = 0; i < countryInfoList.size(); i++) {
            LanguageInfo info = countryInfoList.get(i);
            if (info.getNative_name().equals(name)){
                return info;
            }
        }

        return null;
    }

    private void loadCountryJson(Context context) {
        String configStr = LanguageJsonUtils.getStringFromAssets(context, ASSETS_FILE_NAME);
        if (TextUtils.isEmpty(configStr)) {
            return;
        }
        try {
            countryInfoList = LanguageJsonUtils.parseToList(new JSONArray(configStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
