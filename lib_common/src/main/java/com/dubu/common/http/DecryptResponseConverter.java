package com.dubu.common.http;


import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.dubu.common.beans.base.BaseResponseBean;
import com.dubu.common.beans.base.BaseResponseEncryptBean;
import com.dubu.common.constant.Tag2Common;
import com.dubu.common.utils.AppFactoryTool;
import com.dubu.common.utils.HiLog;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class DecryptResponseConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;

    DecryptResponseConverter(JsonAdapter<T> adapter, Type type) {
        this.type = type;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public T convert(@NonNull ResponseBody value) {
        final Moshi moshi = AppFactoryTool.Companion.getInstance().provideMoshi();

        final BaseResponseEncryptBean er;
        try {
            final String json = value.string();
            HiLog.e(Tag2Common.TAG_HTTP_JSON, "111json = " + json);
            er = moshi.adapter(BaseResponseEncryptBean.class).fromJson(json);
            HiLog.e(Tag2Common.TAG_HTTP_JSON, "222json = " + GsonUtils.toJson(er));
        } catch (Exception e) {
            value.close();
            HiLog.e(Tag2Common.TAG_HTTP_JSON, "convert Error = " + e.getMessage());
            return null;
        }

        if (er == null) {
            value.close();
            HiLog.e(Tag2Common.TAG_HTTP_JSON, "Encrypt bean is null !");
            return null;
        }

        if (er.getData() != null) {
            //            final String rs = er.getData().toString();
            final String rs = GsonUtils.toJson(er.getData());
            HiLog.l(Tag2Common.TAG_HTTP_JSON, "解密前数据 rs = " + rs);
            //            final String dj = EncryptTool.decrypt(rs);
            final String dj = rs;
            HiLog.l(Tag2Common.TAG_HTTP_JSON, "解密以后数据 dj = " + dj);
            if (!TextUtils.isEmpty(dj)) {
                try {
                    ParameterizedType pt = (ParameterizedType) type;
                    Type[] types = pt.getActualTypeArguments();
                    Type clazz = types[0];
                    Object obj = moshi.adapter(clazz).fromJson(dj);
                    return (T) new BaseResponseBean(er.getCode(), er.getMessage(), obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    HiLog.e(Tag2Common.TAG_HTTP_JSON, "Decode json failed! " + e.getMessage());
                    return (T) new BaseResponseBean(er.getCode(), er.getMessage(), null);
                }
            }
        }
        value.close();
        return (T) new BaseResponseBean(er.getCode(), er.getMessage(), null);
    }

}
