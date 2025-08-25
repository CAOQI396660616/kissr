package com.dubu.download.engine.bean;

import java.util.ArrayList;
import java.util.List;


/**
 * 网络资源包的信息列表
 */

public class AssetOptList {
    public int type;
    public int total;
    public int pageNum;
    public int pageSize;
    public boolean hasNext;
    public ArrayList<NvOptAssetInfo> list;
    /**
     * 真实资源列表（网络数据源的转化）
     * List of real resources,Network data source transformation
     */
    public List<AssetInfo> realAssetList;
    public List<NvOptAssetInfo> elements;

    public class NvOptAssetInfo {
        public String id;
        public int category;
        public int kind;
        public String name;
        public String desc;
        public String tags;
        public int version;
        public int type;
        public String minAppVersion;
        public String packageUrl;
        public int packageSize;
        public String coverUrl;
        public int supportedAspectRatio;
        public String previewVideoUrl;
        public String packageRelativePath;
        public String infoUrl;
        public long templateTotalDuration;
        public String description;
        public String descriptionZhCn;

        /**
         * display Name if asset is custom
         * <P></>
         * 自定义名称名称
         */
        public String customDisplayName;
        /**
         * English name
         * <P></>
         * 英文名称
         */
        public String displayName;
        /**
         * Chinese name
         * <p></>
         * 中文名称
         */
        public String displayNameZhCn;

        /**
         * Indicates Whether the asset is authorized.
         * <p></>
         * 是否已经授权
         */
        public boolean authed;
        /**
         * ratio flag 0: uncommon 1: common
         */
        public int ratioFlag;

        @Override
        public String toString() {
            return "NvOptAssetInfo{" +
                    ", authed='" + authed + '\'' +
                    ", tags='" + tags + '\'' +
                    ", category=" + category +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", version=" + version +
                    ", minAppVersion='" + minAppVersion + '\'' +
                    ", packageUrl='" + packageUrl + '\'' +
                    ", packageSize=" + packageSize +
                    ", coverUrl='" + coverUrl + '\'' +
                    ", supportedAspectRatio=" + supportedAspectRatio +
                    ", customDisplayName='" + customDisplayName + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", displayNameZhCn='" + displayNameZhCn + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AssetOptList{" +
                "type=" + type +
                ", list=" + list +
                ", total=" + total +
                ", hasNext=" + hasNext +
                ", realAssetList=" + realAssetList +
                ", elements=" + elements +
                '}';
    }
}
