package com.dubu.common.beans.home;


import java.util.ArrayList;
import java.util.List;

/**
 * 暂时使用这个数据类 转化 首页banner
 *
 *  因为之前编写的时候 用的 demo 的代码测试类 后面对接数据的时候 就直接转化数据了
 */
public class HomeBannerDataBean {
    public Integer imageRes;
    public String imageUrl;
    public String title; //测试的时候这个是没用字段 现在做为 httpLink 做处理
    public String name; //测试的时候这个是没用字段 现在做为 对接后台数据 name 用于埋点上报
    public int viewType;
    public String appLink; //测试的时候这个是没用字段 现在做为 对接后台数据 appLink 路由
    public HomeBannerDataBean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public HomeBannerDataBean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }
    public HomeBannerDataBean(String imageUrl, String title, int viewType, String name, String appLink) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
        this.name = name;
        this.appLink = appLink;
    }

    public static List<HomeBannerDataBean> convertOptData(List<HomeTopVideoBean> homeBannerBeans) {
        List<HomeBannerDataBean> list = new ArrayList<>();

        //size -1 是因为最后一张 是下面的 背景图 不是banner 去掉  我也不知道业务为何这么做!!!!
        //for (int i = 0; i < homeBannerBeans.size() - 1; i++) {
        for (int i = 0; i < homeBannerBeans.size(); i++) {
            HomeTopVideoBean bannerBean = homeBannerBeans.get(i);
            list.add(new HomeBannerDataBean(bannerBean.getVideoLink(), bannerBean.getVideoLink(), 1,"",""));
        }
        return list;
    }

    @Override
    public String toString() {
        return "HomeBannerDataBean{" +
                "imageRes=" + imageRes +
                ", imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", viewType=" + viewType +
                ", appLink='" + appLink + '\'' +
                '}';
    }
}
