package org.dync.giftlibrary.widget;

import java.util.Objects;

/**
 * Created by Allen on 2017/1/8.
 */
public class GiftModel {

    private String giftId;//礼物的id
    private String giftName;//礼物的名字
    private int giftCount;//一次发送礼物的数量
    private String giftPic;//礼物的图片
    private String giftPrice;//礼物的价格
    private String sendUserId;//发送者的id
    private String sendUserName;//发送者的名字
    private String receiveUserName;//接受者的名字
    private String sendUserPic;//发送者的头像
    private int hitCombo;//上一次要连击的礼物数
    private Long sendGiftTime;//发送礼物的时间
    private boolean currentStart;//是否从当前数开始连击
    private int jumpCombo;//跳到指定连击数，例如：从1直接显示3，这里的值就是2

    public GiftModel() {
    }

    public String getGiftId() {
        return giftId;
    }

    public GiftModel setGiftId(String giftId) {
        this.giftId = giftId;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public GiftModel setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public GiftModel setGiftCount(int giftCount) {
        this.giftCount = giftCount;
        return this;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public GiftModel setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
        return this;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public GiftModel setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
        return this;
    }

    public String getSendUserPic() {
        return sendUserPic;
    }

    public GiftModel setSendUserPic(String sendUserPic) {
        this.sendUserPic = sendUserPic;
        return this;
    }

    public String getGiftPic() {
        return giftPic;
    }

    public GiftModel setGiftPic(String giftPic) {
        this.giftPic = giftPic;
        return this;
    }

    public String getGiftPrice() {
        return giftPrice;
    }

    public GiftModel setGiftPrice(String giftPrice) {
        this.giftPrice = giftPrice;
        return this;
    }

    public int getHitCombo() {
        return hitCombo;
    }

    public GiftModel setHitCombo(int hitCombo) {
        this.hitCombo = hitCombo;
        return this;
    }

    public Long getSendGiftTime() {
        return sendGiftTime;
    }

    public GiftModel setSendGiftTime(Long sendGiftTime) {
        this.sendGiftTime = sendGiftTime;
        return this;
    }

    public boolean isCurrentStart() {
        return currentStart;
    }

    public GiftModel setCurrentStart(boolean currentStart) {
        this.currentStart = currentStart;
        return this;
    }

    public int getJumpCombo() {
        return jumpCombo;
    }

    public void setJumpCombo(int jumpCombo) {
        this.jumpCombo = jumpCombo;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public GiftModel setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftModel giftModel = (GiftModel) o;
        return Objects.equals(giftId, giftModel.giftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(giftId);
    }

    @Override
    public String toString() {
        return "GiftModel{" +
                "giftId='" + giftId + '\'' +
                ", giftName='" + giftName + '\'' +
                ", giftCount=" + giftCount +
                ", giftPic='" + giftPic + '\'' +
                ", giftPrice='" + giftPrice + '\'' +
                ", sendUserId='" + sendUserId + '\'' +
                ", sendUserName='" + sendUserName + '\'' +
                ", receiveUserName='" + receiveUserName + '\'' +
                ", sendUserPic='" + sendUserPic + '\'' +
                ", hitCombo=" + hitCombo +
                ", sendGiftTime=" + sendGiftTime +
                ", currentStart=" + currentStart +
                ", jumpCombo=" + jumpCombo +
                '}';
    }
}
