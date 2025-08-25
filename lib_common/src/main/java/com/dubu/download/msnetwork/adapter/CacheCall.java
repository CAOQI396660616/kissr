/*
 * Copyright 2016 jeasonlzy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dubu.download.msnetwork.adapter;

import com.dubu.download.msnetwork.cache.CacheEntity;
import com.dubu.download.msnetwork.cache.CacheMode;
import com.dubu.download.msnetwork.cache.policy.CachePolicy;
import com.dubu.download.msnetwork.cache.policy.DefaultCachePolicy;
import com.dubu.download.msnetwork.cache.policy.FirstCacheRequestPolicy;
import com.dubu.download.msnetwork.cache.policy.NoCachePolicy;
import com.dubu.download.msnetwork.cache.policy.NoneCacheRequestPolicy;
import com.dubu.download.msnetwork.cache.policy.RequestFailedCachePolicy;
import com.dubu.download.msnetwork.callback.Callback;
import com.dubu.download.msnetwork.model.Response;
import com.dubu.download.msnetwork.request.base.Request;
import com.dubu.download.msnetwork.utils.HttpUtils;

/**
 * ================================================
 * 作    者：jeasonlzy.Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/9/11
 * 描    述：带缓存的请求
 * 修订历史：
 * ================================================
 *
 * @param <T> the type parameter
 * 缓存调用类
 * Cache calling class
 *
 */
public class CacheCall<T> implements Call<T> {

    private CachePolicy<T> policy = null;
    private Request<T, ? extends Request> request;

    public CacheCall(Request<T, ? extends Request> request) {
        this.request = request;
        this.policy = preparePolicy();
    }

    @Override
    public Response<T> execute() {
        CacheEntity<T> cacheEntity = policy.prepareCache();
        return policy.requestSync(cacheEntity);
    }

    @Override
    public void execute(Callback<T> callback) {
        HttpUtils.checkNotNull(callback, "callback == null");

        CacheEntity<T> cacheEntity = policy.prepareCache();
        policy.requestAsync(cacheEntity, callback);
    }

    private CachePolicy<T> preparePolicy() {
        if (request.getCacheMode() == CacheMode.DEFAULT) {
            policy = new DefaultCachePolicy<>(request);
        } else if (request.getCacheMode() == CacheMode.NO_CACHE) {
            policy = new NoCachePolicy<>(request);
        } else if (request.getCacheMode() == CacheMode.IF_NONE_CACHE_REQUEST) {
            policy = new NoneCacheRequestPolicy<>(request);
        } else if (request.getCacheMode() == CacheMode.FIRST_CACHE_THEN_REQUEST) {
            policy = new FirstCacheRequestPolicy<>(request);
        } else if (request.getCacheMode() == CacheMode.REQUEST_FAILED_READ_CACHE) {
            policy = new RequestFailedCachePolicy<>(request);
        }
        if (request.getCachePolicy() != null) {
            policy = request.getCachePolicy();
        }
        HttpUtils.checkNotNull(policy, "policy == null");
        return policy;
    }

    @Override
    public boolean isExecuted() {
        return policy.isExecuted();
    }

    @Override
    public void cancel() {
        policy.cancel();
    }

    @Override
    public boolean isCanceled() {
        return policy.isCanceled();
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Call<T> clone() {
        return new CacheCall<>(request);
    }

    public Request getRequest() {
        return request;
    }
}
