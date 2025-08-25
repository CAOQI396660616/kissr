package com.dubu.rtc.country;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by weizhenguo on 2019-10-14.
 */
public class CountryJsonManager {

    private static String ASSETS_FILE_NAME = "json/country.txt";

    private List<CountryInfo> countryInfoList;

    private volatile static CountryJsonManager sInstance;

    private CountryJsonManager() {

    }

    public static CountryJsonManager getInstance() {
        if (sInstance == null) {
            synchronized (CountryJsonManager.class) {
                if (sInstance == null) {
                    sInstance = new CountryJsonManager();
                }
            }
        }
        return sInstance;
    }


    public List<CountryInfo> getCountryList(Context context) {
        if (countryInfoList == null) {
            loadCountryJson(context);
        }
        return countryInfoList;
    }

    private void loadCountryJson(Context context) {
        String configStr = CountryJsonUtils.getStringFromAssets(context, ASSETS_FILE_NAME);
        if (TextUtils.isEmpty(configStr)) {
            return;
        }
        try {
            countryInfoList = CountryJsonUtils.parseToList(new JSONArray(configStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
