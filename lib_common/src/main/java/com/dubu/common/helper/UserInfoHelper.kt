package com.dubu.common.helper

import com.dubu.common.beans.UserBean
import com.dubu.common.api.CommonClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

object UserInfoHelper {
    // 协程作用域
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // 需要刷新的用户ID集合 (线程安全)
    private val userIdSet = ConcurrentHashMap.newKeySet<String>()

    // 用户信息缓存 (线程安全)
    private val userCache = ConcurrentHashMap<String, UserBean>()

    // 合并请求的Flow
    private val refreshFlow = MutableSharedFlow<Unit>(replay = 0)

    init {
        // 启动定时刷新任务
        startPeriodicRefresh()

        // 设置批量请求处理管道
        setupRefreshPipeline()
    }

    /**
     * 添加需要刷新的用户ID
     */
    fun addUserIds(ids: Collection<String>) {
        userIdSet.addAll(ids)
    }

    /**
     * 移除用户ID
     */
    fun removeUserIds(ids: Collection<String>) {
        userIdSet.removeAll(ids)
    }

    /**
     * 获取用户信息 (优先从缓存读取)
     */
    suspend fun getUserInfo(userId: String): UserBean? {
        // 1. 先尝试从缓存获取
        userCache[userId]?.let { return it }

        // 2. 加入刷新队列
        userIdSet.add(userId)

        // 3. 触发刷新并等待结果
        return refreshFlow.first() // 等待下一次刷新完成
            .let { userCache[userId] } // 返回最新数据 (可能仍为null)
    }

    /**
     * 批量获取用户信息
     */
    suspend fun getUsersInfo(userIds: List<String>): Map<String, UserBean> {
        val result = mutableMapOf<String, UserBean>()
        val missingIds = mutableListOf<String>()

        // 1. 先获取已有缓存
        userIds.forEach { id ->
            userCache[id]?.let { result[id] = it } ?: run { missingIds.add(id) }
        }

        // 2. 如果有缺失ID，加入刷新队列并触发刷新
        if (missingIds.isNotEmpty()) {
            userIdSet.addAll(missingIds)
            refreshFlow.emit(Unit) // 立即触发刷新

            // 等待刷新完成后再检查一次
            refreshFlow.first()
            missingIds.forEach { id ->
                userCache[id]?.let { result[id] = it }
            }
        }

        return result
    }

    // ================= 内部实现 =================
    private fun startPeriodicRefresh() {
        scope.launch {
            while (isActive) {
                delay(10 * 60 * 1000) // 10分钟
                if (userIdSet.isNotEmpty()) {
                    refreshFlow.emit(Unit)
                }
            }
        }
    }

    private fun setupRefreshPipeline() {
        refreshFlow
            .conflate() // 合并连续请求
            .debounce(300) // 防抖处理 (300ms)
            .onEach {
                if (userIdSet.isNotEmpty()) {
                    processBatchRequests()
                }
            }
            .launchIn(scope)
    }

    private suspend fun processBatchRequests() {
        val currentIds = userIdSet.toList()
        if (currentIds.isEmpty()) return

        // 分批处理 (每批100个)
        val batchSize = 100
        val batches = currentIds.chunked(batchSize)

        // 并行执行所有批次
        val results = batches.map { batch ->
            scope.async {
                fetchUserBatch(batch)
            }
        }.awaitAll()

        // 更新缓存
        results.forEach { batchResult ->
            batchResult?.forEach { user ->
                user?.let { userCache[it.userSn.toString()] = it }
            }
        }

        // 移除已处理的ID
        userIdSet.removeAll(currentIds)
    }

    private suspend fun fetchUserBatch(ids: List<String>): List<UserBean?>? {
        if (ids.isEmpty()) return null

        return try {
            val client = CommonClient()
            val userList = client.getUserList(ids.toMutableList()) { code, msg ->
                // 错误处理 (可根据需求添加重试逻辑)
                println("Batch request failed: $code - $msg")
            }
            userList
        } catch (e: Exception) {
            // 异常处理
            println("Batch request error: ${e.message}")
            null
        }
    }

    /**
     * 清理资源
     */
    fun destroy() {
        scope.cancel()
    }
}