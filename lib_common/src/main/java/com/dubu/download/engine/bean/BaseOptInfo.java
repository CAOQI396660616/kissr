package com.dubu.download.engine.bean;

import com.blankj.utilcode.util.GsonUtils;


public class BaseOptInfo implements IOptBaseInfo {
    /**
     * 内建特效
     * Built-in effects
     */
    public static int EFFECT_MODE_OptBUILTIN = 0;
    /**
     * Asset中预装
     * Pre-installed in Asset
     */
    public static int EFFECT_MODE_OptBUNDLE = 1;
    /**
     * 包裹特效
     * Package effects
     */
    public static int EFFECT_MODE_OptPACKAGE = 2;

    /**
     * 不适配比例
     * Unfit ratio
     * */
    public static final int AspectRatioOpt_NoFitRatio = 0;
    public static final int AspectRatioOpt_16v9 = 1;
    public static final int AspectRatioOpt_1v1 = 2;
    public static final int AspectRatioOpt_9v16 = 4;
    public static final int AspectRatioOpt_4v3 = 8;
    public static final int AspectRatioOpt_3v4 = 16;

    public static final int AspectRatioOpt_18v9 = 32;
    public static final int AspectRatioOpt_9v18 = 64;
    public static final int AspectRatioOpt_21v9 = 512;
    public static final int AspectRatioOpt_9v21 = 1024;
    public static final int AspectRatioOpt_All = AspectRatioOpt_16v9
            | AspectRatioOpt_1v1
            | AspectRatioOpt_9v16
            | AspectRatioOpt_3v4
            | AspectRatioOpt_4v3
            | AspectRatioOpt_18v9
            | AspectRatioOpt_9v18
            | AspectRatioOpt_21v9
            | AspectRatioOpt_9v21;

    private String name;
    private int type;
    private String coverOptPath;
    private int coverOptId;
    private String commonOptInfo;
    private boolean isOptAuthorized;
    private Object tag;
    private String description;
    private long duration;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getCoverOptPath() {
        return coverOptPath;
    }

    @Override
    public void setCoverOptPath(String coverOptPath) {
        this.coverOptPath = coverOptPath;
    }

    @Override
    public int getCoverOptId() {
        return coverOptId;
    }

    @Override
    public void setCoverOptId(int resId) {
        coverOptId = resId;
    }

    @Override
    public String getPackageId() {
        return null;
    }

    @Override
    public void setPackageId(String packageId) {

    }

    @Override
    public String getAssetPath() {
        return null;
    }

    @Override
    public void setAssetPath(String assetPath) {

    }

    @Override
    public String getEffectId() {
        return null;
    }

    @Override
    public void setEffectId(String effectId) {

    }

    @Override
    public void setEffectMode(int effectMode) {

    }

    @Override
    public int getEffectMode() {
        return 0;
    }

    @Override
    public void setEffectStrength(float strength) {

    }

    @Override
    public float getEffectStrength() {
        return 0;
    }

    @Override
    public void setCommonOptInfo(String info) {
        this.commonOptInfo = info;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String getCommonOptInfo() {
        return commonOptInfo;
    }

    public boolean isOptAuthorized() {
        return isOptAuthorized;
    }

    public void setOptAuthorized(boolean optAuthorized) {
        isOptAuthorized = optAuthorized;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public IOptBaseInfo copy() {

        return GsonUtils.fromJson(GsonUtils.toJson(this), getClass());
    }

    @Override
    public void update(IOptBaseInfo newInfo) {
        if (newInfo != null) {
            setId(newInfo.getId());
            setName(newInfo.getName());
            setType(newInfo.getType());
            setCoverOptPath(newInfo.getCoverOptPath());
            setAssetPath(newInfo.getAssetPath());
            setCoverOptId(newInfo.getCoverOptId());
            setCommonOptInfo(newInfo.getCommonOptInfo());
            setPackageId(newInfo.getPackageId());
            setEffectId(newInfo.getEffectId());
            setEffectMode(newInfo.getEffectMode());
            setEffectStrength(newInfo.getEffectStrength());
            setOptAuthorized(newInfo.isOptAuthorized());
            setTag(getTag());
            setDescription(getDescription());
            setDuration(getDuration());
        }
    }

    @Override
    public void setTag(Object obj) {
        this.tag = obj;
    }

    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "BaseOptInfo{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", coverOptPath='" + coverOptPath + '\'' +
                ", coverOptId=" + coverOptId +
                ", commonOptInfo='" + commonOptInfo + '\'' +
                ", isOptAuthorized=" + isOptAuthorized +
                ", tag=" + tag +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                '}';
    }
}


