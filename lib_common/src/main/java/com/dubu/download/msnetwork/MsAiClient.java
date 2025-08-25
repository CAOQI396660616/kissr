package com.dubu.download.msnetwork;

import com.blankj.utilcode.util.EncryptUtils;

import java.nio.charset.StandardCharsets;

/**
 *
 */
public final class MsAiClient {
    public static final String Data1Encode = "1FB5D4452D4D7BF7FFA1233F1DD397F03DFE709EDF535A3AF6D3F5C3C8CC4805F72A2268D4B885056FE23A935970C9DD";
    public static final String Data2Encode = "1B718736AD83AD7959D60DC7BF3054F571BC8A2A82161E892A3E1B4F60E42E52";
    public static final String Data3Encode = "21FFBFA15985822895E945E7AE68656E39598743E480C2EDEA3AC38DCA06981A8C1BD64ED2DA01667444FA98F06FFF9C0B90C6A6A077357B07AF9CE51C2230AC";

    public static String getMsHttpUrl() {
        return decode(Data1Encode);
    }
    public static String getMsHttpUrl2() {
        return decode(Data2Encode);
    }

    public static String getMsHttpUrl3() {
        return decode(Data3Encode);
    }

    public static final String BASE_PATH_None1 = "1B718736AD83AD7959D60DC7BF3054F583DEF302F96A3CDE5C1F6B215B9A803B9E2AC6CC135C6E58E72AFD0CD22118D53EFF580D336943A3CA103E5DBFC2AEA077B11AA4EDF7B22556BD6D4D7920569D777CA3982D5E753AF1E92B84BBB486C642B1F61A894C2ADC587C6FA0836ECCE140A381A93071493EA766396BE19D3D5D09AE0D48518B66EDC2D94F16249012DD";
    public static final String BASEPATH_None1 = "1B718736AD83AD7959D60DC7BF3054F583DEF302F96A3CDE5C1F6B215B9A803B9E2AC6CC135C6E58E72AFD0CD22118D53EFF580D336943A3CA103E5DBFC2AEA077B11AA4EDF7B22556BD6D4D7920569D777CA3982D5E753AF1E92B84BBB486C63CAF74457B1EFB5CEE4C23A9CB9DB6D9F69F156106766D34C11B15E8A2B8088A1EB6C3891EA8FD70A9B56C1FD22A297E";
    public static final String RESOURCE_OLD_PATH_None1 = "D072B4EBD2089AEBEA1EA82BC21FB78237D07D92C0F1869A30614668872E3839";
    public static final String RESOURCE_NEW_PATH_None1 = "21FFBFA15985822895E945E7AE68656E4D9AE2FAAE7859CB74BC031D171B3768";

    public static final String NV_DOMAIN_URL1 = "6B570E055A966090413E9CC88DFBE9E0A27890223D2936D8ED5C1136D97E46594BA2EAEB7A59FE6D42A22B2E172371F9";
    public static final String NV_CDN_URL1 = "063191D5A6ECF1FB9127A0BFEE225E0D73FFDC7B27D3A9B3F657B8E8C9A36F3A46E750D4FA6A3C851470AB53E227F9A5";
    public static final String NV_CDN_URL111 = "3DF55599ED56B3DF8278E9DF0461492B00A68664B89FDD4936F45DB69F2D4BD5";
    public static final String NV_CDN_URL222 = "924AA784FF1C4345FC4720E85A097A94A146C30A67044C51502EDF03E760E3FE";
    public static final String NV_CDN_URL333 = "1B718736AD83AD7959D60DC7BF3054F5DDE083B614FAC470344FEBE467F4A32EAC599E8A40765064F55719D17AD328655EE65270B67515F82BADA607056EE88D";

    /*
       ╔════════════════════════════════════════════════════════════════════════════════════════╗
       ║   PS:  下面API 分装
       ╚════════════════════════════════════════════════════════════════════════════════════════╝
    */
    public static final String DEFAULT_DES_KEY = "1728efbda81692282ba642aafd57be3a";
    public static final String DEFAULT_IV = "xgCoSzGxpIbu7Bis";
    private static final String KEY_ALGORITHM = "DES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    public static String encode(String con) {

        String bytes = EncryptUtils.encryptAES2HexString(
                con.getBytes(),
                DEFAULT_DES_KEY.getBytes(StandardCharsets.UTF_8),
                CIPHER_ALGORITHM,
                DEFAULT_IV.getBytes(StandardCharsets.UTF_8)
        );

        return bytes;
    }

    public static String decode(String con) {
        byte[] bytes = EncryptUtils.decryptHexStringAES(con,
                DEFAULT_DES_KEY.getBytes(StandardCharsets.UTF_8),
                CIPHER_ALGORITHM,
                DEFAULT_IV.getBytes(StandardCharsets.UTF_8));

        String s = totoStr(bytes);
        return s;
    }

    private static String totoStr(byte[] buf) {
        return new String(buf);
    }


}

