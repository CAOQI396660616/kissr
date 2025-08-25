# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



# 指定不去忽略非公共的库的类-
# 默认跳过，有些情况下编写的代码与类库中的类在同一个包下，并且持有包中内容的引用，此时就需要加入此条声明
-dontskipnonpubliclibraryclasses

# 指定不去忽略非公共的库的类的成员-
-dontskipnonpubliclibraryclassmembers


#将文件来源重命名为“SourceFile”字符串
#-renamesourcefileattribute SourceFile
# 抛出异常时保留代码行号-
-keepattributes SourceFile,LineNumberTable

#方法数量超过64K
-keepattributes EnclosingMethod

#四大组件相关
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider






-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

-keep public class cn.jzvd.JZMediaSystem {*; }
-keep public class cn.jzvd.demo.CustomMedia.CustomMedia {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaIjk {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder {*; }


-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
-keep class tv.danmaku.ijk.media.player.** {*; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *; }


-dontwarn com.google.devtools.ksp.processing.SymbolProcessorProvider
-dontwarn javax.lang.model.element.Element
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE



-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**



# KochavaCore: Google Instant Apps Collection.
-keep class com.google.android.gms.common.wrappers.InstantApps {
    boolean isInstantApp(android.content.Context);
}

# KochavaTracker: Google ADID Collection.
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}

# KochavaTracker: Google App Set ID Collection
-keep class com.google.android.gms.appset.AppSet {
    com.google.android.gms.appset.AppSetIdClient getClient(android.content.Context);
}
-keep class com.google.android.gms.appset.AppSetIdClient {
    com.google.android.gms.tasks.Task getAppSetIdInfo();
}
-keep class com.google.android.gms.appset.AppSetIdInfo {
    java.lang.String getId();
    int getScope();
}
-keep class com.google.android.gms.tasks.Tasks {
    await(com.google.android.gms.tasks.Task, long, java.util.concurrent.TimeUnit);
}

# KochavaTracker: Notifications Enabled Collection.
-keep class androidx.core.app.NotificationManagerCompat {
    static androidx.core.app.NotificationManagerCompat from(android.content.Context);
    boolean areNotificationsEnabled();
}

-keep class com.appsflyer.** { *; }
-keep public class com.android.installreferrer.** { *; }
-keep public class com.google.firebase.messaging.FirebaseMessagingService {
    public *;
}

###### 公共部分 #####
#WebView不能混淆-
-keep class com.**WebViewActivity{
*;
}

-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}

-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}

#避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


###### 第三方 #####
# Google-
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Facebook-
-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}
-keep enum com.facebook.** {*;}

# Glide-
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Glide webp-
-keep public class com.bumptech.glide.integration.webp.WebpImage { *; }
-keep public class com.bumptech.glide.integration.webp.WebpFrame { *; }
-keep public class com.bumptech.glide.integration.webp.WebpBitmapFactory { *; }


# EventBus-
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Okhttp3-
-dontwarn okhttp3.**
-keep class okhttp3.** {*;}
-dontwarn okio.**
-keep class okio.** {*;}

# Immersionbar
#-keep class com.gyf.immersionbar.* {*;}
# -dontwarn com.gyf.immersionbar.**

 # Gson相关
 -keep class com.google.gson.**{*;}

 -keep class com.dubu.shorts.bean.** { <fields>; }
  #-keep class com.weiyun.square.bean.**.event.** {<fields>;}
 -keep class com.dubu.common.** {<fields>;}
 #PictureSelector
 -keep class com.luck.picture.lib.** { *; }
 #SVGAPlayer 第三方sdk需要的混淆规则
 -keep class com.squareup.wire.** { *; }
 -keep class com.opensource.svgaplayer.proto.** { *; }

-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx
-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }

-keep class * implements java.io.Serializable { *;}
-keepclassmembers class * implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}
-keep class * implements android.os.Parcelable {
  *;
}
#自定义组件不混淆
-keep public class * extends android.view.View{
  *** get*();
  void set*(***);
  public <init>(android.content.Context);
  public <init>(android.content.Context, android.util.AttributeSet);
  public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
  public <init>(android.content.Context, android.util.AttributeSet);
  public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {
  public <init>(org.json.JSONObject);
}
-keepattributes *JavascriptInterface*

-keep class **.R$* {
  *;
}
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
#如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

-keepclassmembers class * {
  void *(**On*Event);
}

#火山im
-keep class com.bytedance.**{*;}



#https://github.com/LuckSiege/PictureSelector/blob/version_component/README_CN.md
-keep class com.luck.picture.lib.** { *; }
#如果引入了Camerax库请添加混淆
-keep class com.luck.lib.camerax.** { *; }
#如果引入了Ucrop库请添加混淆
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#https://github.com/LuckSiege/PictureSelector
-keep class com.luck.picture.lib.** { *; }
-keep class com.luck.lib.camerax.** { *; }
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#https://bugly.qq.com/docs/user-guide/instruction-manual-android/?v=1.0.0
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#火山美颜
#https://www.volcengine.com/docs/6705/101959#%E9%99%84%E5%BD%95%EF%BC%9A%E7%B4%A0%E6%9D%90%E6%8B%B7%E8%B4%9D%E8%AF%B4%E6%98%8E
-keep class com.effectsar.labcv.effectsdk.** {*;}
-keep class com.bef.effectsdk.** {*;}
-keep class com.effectsar.labcv.licenselibrary.** {*;}
-keep class com.amazing.** {*;}