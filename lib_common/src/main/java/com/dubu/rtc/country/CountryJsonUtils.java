package com.dubu.rtc.country;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.dubu.rtc.emoji.EmojiInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CountryJsonUtils {

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

    static List<CountryInfo> parseToList(JSONArray array) {
        if (array == null) {
            return null;
        }
        int length = array.length();
        List<CountryInfo> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            CountryInfo info = parse(array.optJSONObject(i));
            info.id = i;
            list.add(info);
        }
        return list;
    }

    static CountryInfo parse(JSONObject object) {
        if (object == null) {
            return null;
        }
        CountryInfo info = new CountryInfo();
        info.countryName = object.optString("country");
        return info;
    }

}
