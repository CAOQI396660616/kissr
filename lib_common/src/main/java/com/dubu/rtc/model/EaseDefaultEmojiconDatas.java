package com.dubu.rtc.model;


import com.dubu.common.R;
import com.dubu.rtc.tools.EaseSmileUtils;

import java.util.ArrayList;

public class EaseDefaultEmojiconDatas {
    
    private static String[] emojis = new String[]{
        EaseSmileUtils.ee_1,
        EaseSmileUtils.ee_2,
        EaseSmileUtils.ee_3,
        EaseSmileUtils.ee_4,
        EaseSmileUtils.ee_5,
        EaseSmileUtils.ee_6,
        EaseSmileUtils.ee_7,
        EaseSmileUtils.ee_8,
        EaseSmileUtils.ee_9,
        EaseSmileUtils.ee_10,
        EaseSmileUtils.ee_11,
        EaseSmileUtils.ee_12,
        EaseSmileUtils.ee_13,
        EaseSmileUtils.ee_14,
        EaseSmileUtils.ee_15,
        EaseSmileUtils.ee_16,
        EaseSmileUtils.ee_17,
        EaseSmileUtils.ee_18,
        EaseSmileUtils.ee_19,
        EaseSmileUtils.ee_20,
        EaseSmileUtils.ee_21,
        EaseSmileUtils.ee_22,
        EaseSmileUtils.ee_23,
        EaseSmileUtils.ee_24,
        EaseSmileUtils.ee_25,
        EaseSmileUtils.ee_26,
        EaseSmileUtils.ee_27,
        EaseSmileUtils.ee_28,
        EaseSmileUtils.ee_29,
        EaseSmileUtils.ee_30,
        EaseSmileUtils.ee_31,
        EaseSmileUtils.ee_32,
        EaseSmileUtils.ee_33,
        EaseSmileUtils.ee_34,
        EaseSmileUtils.ee_35,
       
    };
    
    private static int[] icons = new int[]{
        R.drawable.ee_1,
        R.drawable.ee_2,  
        R.drawable.ee_3,  
        R.drawable.ee_4,  
        R.drawable.ee_5,  
        R.drawable.ee_6,  
        R.drawable.ee_7,  
        R.drawable.ee_8,  
        R.drawable.ee_9,  
        R.drawable.ee_10,  
        R.drawable.ee_11,  
        R.drawable.ee_12,  
        R.drawable.ee_13,  
        R.drawable.ee_14,  
        R.drawable.ee_15,  
        R.drawable.ee_16,  
        R.drawable.ee_17,  
        R.drawable.ee_18,  
        R.drawable.ee_19,  
        R.drawable.ee_20,  
        R.drawable.ee_21,  
        R.drawable.ee_22,  
        R.drawable.ee_23,  
        R.drawable.ee_24,  
        R.drawable.ee_25,  
        R.drawable.ee_26,  
        R.drawable.ee_27,  
        R.drawable.ee_28,  
        R.drawable.ee_29,  
        R.drawable.ee_30,  
        R.drawable.ee_31,  
        R.drawable.ee_32,  
        R.drawable.ee_33,  
        R.drawable.ee_34,  
        R.drawable.ee_35,  
    };
    
    
    private static final EaseEmojicon[] DATA = createData();
    
    private static EaseEmojicon[] createData(){
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            EaseEmojicon emoji = new EaseEmojicon(icons[i], emojis[i], EaseEmojicon.Type.NORMAL);
            datas[i] = emoji;
        }
        return datas;
    }

    // allen 暂时关闭这个方法 之后要是恢复 可以打开
    /*public static EaseEmojicon[] getData(){
        return DATA;
    }*/

    //---------api allen 新增
    private static int[] emojiList = new int[]{
            0x1F600, 0x1F601, 0x1F602, 0x1F603, 0x1F604, 0x1F605, 0x1F606, 0x1F607, 0x1F608, 0x1F609, 0x1F60a, 0x1F60b, 0x1F60c, 0x1F60d, 0x1F60e, 0x1F60f,
            0x1F610, 0x1F611, 0x1F612, 0x1F613, 0x1F614, 0x1F615, 0x1F616, 0x1F617, 0x1F618, 0x1F619, 0x1F61a, 0x1F61b, 0x1F61c, 0x1F61d, 0x1F61e, 0x1F61f,
            0x1F620, 0x1F621, 0x1F622, 0x1F623, 0x1F624, 0x1F625, 0x1F626, 0x1F627, 0x1F628, 0x1F629, 0x1F62a, 0x1F62b, 0x1F62c, 0x1F62d, 0x1F62e, 0x1F62f,
            0x1F630, 0x1F631, 0x1F632, 0x1F633, 0x1F634, 0x1F635, 0x1F636, 0x1F637, 0x1F638, 0x1F639, 0x1F63a, 0x1F63b, 0x1F63c, 0x1F63d, 0x1F63e, 0x1F63f,
            0x1F640, 0x1F641, 0x1F642, 0x1F643, 0x1F644, 0x1F645, 0x1F646, 0x1F647, 0x1F648, 0x1F649, 0x1F64a, 0x1F64b, 0x1F64c, 0x1F64d, 0x1F64e, 0x1F64f,
    };

    private static final EaseEmojicon[] DATAV2 = createDataV2();

    private static EaseEmojicon[] createDataV2(){
        EaseEmojicon[] datas = new EaseEmojicon[emojiList.length];
        for(int i = 0; i < emojiList.length; i++){
            //注意这个 0  0  表示我们的表情 默认使用一个
            EaseEmojicon emoji = new EaseEmojicon(icons[0], emojis[0], EaseEmojicon.Type.NORMAL);
            emoji.setIdentityCode(EaseEmojicon.newEmojiText((emojiList[i])));
            emoji.setName("" + (i + 1));
            datas[i] = emoji;
        }
        return datas;
    }

    public static EaseEmojicon[] getData(){
        return DATAV2;
    }


    private static ArrayList<EaseEmojicon> createDataV3(){
        ArrayList<EaseEmojicon> easeEmojicons = new ArrayList<>(emojiList.length);
        for(int i = 0; i < emojiList.length; i++){
            //注意这个 0  0  表示我们的表情 默认使用一个
            EaseEmojicon emoji = new EaseEmojicon(icons[0], emojis[0], EaseEmojicon.Type.NORMAL);
            emoji.setIdentityCode(EaseEmojicon.newEmojiText((emojiList[i])));
            emoji.setName("emoji" + (i + 1));
            easeEmojicons.add(emoji);
        }
        return easeEmojicons;
    }

}
