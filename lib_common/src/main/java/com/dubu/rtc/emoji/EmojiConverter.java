package com.dubu.rtc.emoji;
import android.content.Context;
import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmojiConverter {
    // 表情文本到编码的映射（如 "[女孩]" -> "[29]"）
    private static Map<String, String> textToCodeMap = new HashMap<>();
    // 编码到表情文本的映射（如 "[29]" -> "[女孩]"）
    private static Map<String, String> codeToTextMap = new HashMap<>();
    private static boolean isLoaded = false;

    /**
     * 从 assets 加载表情映射表
     */
    private static synchronized void loadEmojiMaps(Context context) {
        if (isLoaded) return;

        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open("emoji/emoji.txt"); // 替换为你的JSON文件名
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                int code = obj.getInt("code");
                String value = obj.getString("value");

                // 创建编码字符串格式
                String codeStr = "[" + code + "]";

                // 填充双向映射
                textToCodeMap.put(value, codeStr);
                codeToTextMap.put(codeStr, value);
            }
            isLoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文本中的表情替换为编码（文本 → 编码）
     */
    public static String textToCodes(Context context, String text) {
        if (text == null || text.isEmpty()) return text;
        loadEmojiMaps(context);
        return replaceUsingMap(text, textToCodeMap);
    }

    /**
     * 将文本中的编码替换为表情（编码 → 文本）
     */
    public static String codesToText(Context context, String text) {
        if (text == null || text.isEmpty()) return text;
        loadEmojiMaps(context);
        return replaceUsingMap(text, codeToTextMap);
    }

    /**
     * 使用指定映射表进行替换的通用方法 [*(!@#123$)]
     */
    private static String replaceUsingMap(String text, Map<String, String> map) {
        // 按键的长度降序排序（确保长文本优先匹配）
        String[] keys = map.keySet().toArray(new String[0]);
        java.util.Arrays.sort(keys, (s1, s2) -> Integer.compare(s2.length(), s1.length()));

        for (String key : keys) {
            if (text.contains(key)) {
                text = text.replace(key, map.get(key));
            }
        }
        return text;
    }

    /**
     * 预加载表情映射表（可选）
     */
    public static void preload(Context context) {
        loadEmojiMaps(context);
    }
}
