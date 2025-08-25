package com.dubu.rtc.language;

import android.content.Context;

import com.dubu.common.beans.config.LanguageInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LanguageJsonUtils {

    static String getStringFromAssets(Context context, String fileName) {
        if (context == null) {
            return "";
        }

        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = context.getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "";
    }

    static List<LanguageInfo> parseToList(JSONArray array) {
        if (array == null) {
            return null;
        }
        int length = array.length();
        List<LanguageInfo> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            LanguageInfo info = parse(array.optJSONObject(i));
            list.add(info);
        }
        return list;
    }

    static LanguageInfo parse(JSONObject object) {
        if (object == null) {
            return null;
        }
        LanguageInfo info = new LanguageInfo();
        info.setName(object.optString("language"));
        info.setCountry_code(object.optString("country_code"));
        info.setNative_name(object.optString("native_name"));
        info.set_current(object.optString("is_current"));
        return info;
    }

}
