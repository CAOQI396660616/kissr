package com.dubu.rtc.model;


import com.dubu.common.R;
import com.dubu.rtc.tools.EaseSmileUtils;

import java.util.ArrayList;

public class EaseDefaultEmojiconDatasV3 {
    
    private static String[] emojis = new String[]{
        EaseSmileUtils.ic_emoji_1,
        EaseSmileUtils.ic_emoji_2,
        EaseSmileUtils.ic_emoji_3,
        EaseSmileUtils.ic_emoji_4,
        EaseSmileUtils.ic_emoji_5,
        EaseSmileUtils.ic_emoji_6,
        EaseSmileUtils.ic_emoji_7,
        EaseSmileUtils.ic_emoji_8,
        EaseSmileUtils.ic_emoji_9,
        EaseSmileUtils.ic_emoji_10,
        EaseSmileUtils.ic_emoji_11,
        EaseSmileUtils.ic_emoji_12,
        EaseSmileUtils.ic_emoji_13,
        EaseSmileUtils.ic_emoji_14,
        EaseSmileUtils.ic_emoji_15,
        EaseSmileUtils.ic_emoji_16,
        EaseSmileUtils.ic_emoji_17,
        EaseSmileUtils.ic_emoji_18,
        EaseSmileUtils.ic_emoji_19,
        EaseSmileUtils.ic_emoji_20,
        EaseSmileUtils.ic_emoji_21,
        EaseSmileUtils.ic_emoji_22,
        EaseSmileUtils.ic_emoji_23,
        EaseSmileUtils.ic_emoji_24,
        EaseSmileUtils.ic_emoji_25,
        EaseSmileUtils.ic_emoji_26,
        EaseSmileUtils.ic_emoji_27,
        EaseSmileUtils.ic_emoji_28,
        EaseSmileUtils.ic_emoji_29,
        EaseSmileUtils.ic_emoji_30,
        EaseSmileUtils.ic_emoji_31,
        EaseSmileUtils.ic_emoji_32,
        EaseSmileUtils.ic_emoji_33,
        EaseSmileUtils.ic_emoji_34,
        EaseSmileUtils.ic_emoji_35,
       
    };
    
    private static int[] icons = new int[]{
        R.drawable.ic_emoji_1,
        R.drawable.ic_emoji_2,
        R.drawable.ic_emoji_3,
        R.drawable.ic_emoji_4,
        R.drawable.ic_emoji_5,
        R.drawable.ic_emoji_6,
        R.drawable.ic_emoji_7,
        R.drawable.ic_emoji_8,
        R.drawable.ic_emoji_9,
        R.drawable.ic_emoji_10,
        R.drawable.ic_emoji_11,
        R.drawable.ic_emoji_12,
        R.drawable.ic_emoji_13,
        R.drawable.ic_emoji_14,
        R.drawable.ic_emoji_15,
        R.drawable.ic_emoji_16,
        R.drawable.ic_emoji_17,
        R.drawable.ic_emoji_18,
        R.drawable.ic_emoji_19,
        R.drawable.ic_emoji_20,
        R.drawable.ic_emoji_21,
        R.drawable.ic_emoji_22,
        R.drawable.ic_emoji_23,
        R.drawable.ic_emoji_24,
        R.drawable.ic_emoji_25,
        R.drawable.ic_emoji_26,
        R.drawable.ic_emoji_27,
        R.drawable.ic_emoji_28,
        R.drawable.ic_emoji_29,
        R.drawable.ic_emoji_30,
        R.drawable.ic_emoji_31,
        R.drawable.ic_emoji_32,
        R.drawable.ic_emoji_33,
        R.drawable.ic_emoji_34,
        R.drawable.ic_emoji_35,
    };
    

    private static int[] emojiList = new int[]{
            0x1F600, 0x1F601, 0x1F602, 0x1F603, 0x1F604, 0x1F605, 0x1F606, 0x1F607, 0x1F608, 0x1F609, 0x1F60a, 0x1F60b, 0x1F60c, 0x1F60d, 0x1F60e, 0x1F60f,
            0x1F610, 0x1F611, 0x1F612, 0x1F613, 0x1F614, 0x1F615, 0x1F616, 0x1F617, 0x1F618, 0x1F619, 0x1F61a, 0x1F61b, 0x1F61c, 0x1F61d, 0x1F61e, 0x1F61f,
            0x1F620, 0x1F621, 0x1F622, 0x1F623, 0x1F624, 0x1F625, 0x1F626, 0x1F627, 0x1F628, 0x1F629, 0x1F62a, 0x1F62b, 0x1F62c, 0x1F62d, 0x1F62e, 0x1F62f,
            0x1F630, 0x1F631, 0x1F632, 0x1F633, 0x1F634, 0x1F635, 0x1F636, 0x1F637, 0x1F638, 0x1F639, 0x1F63a, 0x1F63b, 0x1F63c, 0x1F63d, 0x1F63e, 0x1F63f,
            0x1F640, 0x1F641, 0x1F642, 0x1F643, 0x1F644, 0x1F645, 0x1F646, 0x1F647, 0x1F648, 0x1F649, 0x1F64a, 0x1F64b, 0x1F64c, 0x1F64d, 0x1F64e, 0x1F64f,
    };

    private static final ArrayList<EaseEmojicon> DATAV2 = createDataV3();


    public static ArrayList<EaseEmojicon> getData(){
        return DATAV2;
    }


    private static ArrayList<EaseEmojicon> createDataV3(){
        ArrayList<EaseEmojicon> easeEmojicons = new ArrayList<>(emojiList.length);
        for(int i = 0; i < icons.length; i++){
            //注意这个 0  0  表示我们的表情 默认使用一个
            EaseEmojicon emoji = new EaseEmojicon(icons[i], emojis[i], EaseEmojicon.Type.NORMAL);
            emoji.setIdentityCode(EaseEmojicon.newEmojiText((emojiList[i])));
            emoji.setName("emoji" + (i + 1));
            easeEmojicons.add(emoji);
        }
        return easeEmojicons;
    }

}
