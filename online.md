# 实时在线状态同步生命周期文档

## 概述

本文档详细描述了基于火山引擎RTS的实时在线状态同步系统的完整生命周期，包括用户上线、状态维护、通信交互、下线处理等各个环节。

## 系统架构

```
用户端(Web) ←→ 火山引擎RTS ←→ 主播端(Android)
     ↓              ↓              ↓
火山引擎RTC ←→ 实时音视频通话 ←→ 火山引擎RTC
     ↓              ↓              ↓
您的后端API ←→ 业务逻辑处理 ←→ 您的后端API
```

## 生命周期阶段

### 1. 用户上线阶段

#### 1.1 主播端（Android）上线流程

**详细步骤：**

1. **应用启动**
   - Android应用启动
   - 初始化火山引擎SDK
   - 检查网络连接状态

2. **用户登录**
   - 主播输入账号密码
   - 调用后端登录API
   - 获取用户ID和认证信息

3. **获取RTS Token**
   ```javascript
   // 调用后端API获取Token
   POST /api/online-status/rts-token
   {
     "user_id": "kol_123",
     "room_id": "room_456",
     "expire_time": 3600
   }
   ```

4. **建立连接**
   - 连接到火山引擎RTS服务器
   - 使用Token进行身份验证
   - 建立WebSocket连接

5. **发送上线消息**
   ```json
   {
     "type": "user_online",
     "user_id": "kol_123",
     "user_type": "kol",
     "timestamp": 1640995200
   }
   ```

6. **状态广播**
   - RTS服务器接收上线消息
   - 广播主播上线状态给所有在线用户
   - 更新全局在线状态

#### 1.2 用户端（Web）上线流程

**详细步骤：**

1. **页面加载**
   - Web页面加载完成
   - 初始化火山引擎RTS客户端
   - 检查浏览器兼容性

2. **用户登录**
   - 用户输入账号密码
   - 调用后端登录API
   - 获取用户认证信息

3. **获取RTS Token**
   ```javascript
   // 获取Token
   const response = await fetch('/api/online-status/rts-token', {
     method: 'POST',
     headers: { 'Authorization': `Bearer ${token}` },
     body: JSON.stringify({
       user_id: 'user_789',
       expire_time: 3600
     })
   });
   ```

4. **连接RTS**
   ```javascript
   // 建立连接
   const rtsClient = new VolcRtsClient({
     appId: 'your_app_id',
     userId: 'user_789',
     token: tokenData.signature
   });
   ```

5. **接收状态更新**
   ```javascript
   // 监听主播状态变化
   rtsClient.on('userOnlineStatus', (data) => {
     data.user_status.forEach(status => {
       updateKolStatus(status.user_id, status.online);
     });
   });
   ```

6. **更新UI显示**
   - 实时更新主播在线状态
   - 显示在线/离线指示器
   - 更新主播列表

### 2. 在线状态维护阶段

#### 2.1 心跳机制

**心跳频率配置：**
- **主播端**：每30秒发送一次心跳
- **用户端**：每30秒发送一次心跳
- **超时时间**：60秒无心跳则标记为离线

**心跳消息格式：**
```json
{
  "type": "heartbeat",
  "user_id": "kol_123",
  "user_type": "kol",
  "timestamp": 1640995200
}
```

**心跳确认响应：**
```json
{
  "type": "heartbeat_ack",
  "timestamp": 1640995200
}
```

#### 2.2 状态变更处理

**状态类型定义：**
- `online`：在线（可接收通话）
- `busy`：忙碌（通话中）
- `away`：离开（暂时不可用）
- `offline`：离线

**状态变更流程：**
```
主播状态变更 → 发送状态消息 → 火山引擎RTS → 广播给所有用户 → 更新UI
```

**状态变更消息：**
```json
{
  "type": "status_change",
  "user_id": "kol_123",
  "user_type": "kol",
  "status": "busy",
  "timestamp": 1640995200
}
```

#### 2.3 状态存储策略

**Redis存储：**
- 键名格式：`kol_status:{user_id}` 或 `user_status:{user_id}`
- 过期时间：5分钟
- 数据结构：
```json
{
  "status": "online",
  "last_seen": 1640995200,
  "timestamp": 1640995200,
  "user_type": "kol"
}
```

**数据库同步：**
- 实时更新数据库中的在线状态
- 记录最后在线时间
- 记录最后离线时间

### 3. 实时通信阶段

#### 3.1 音视频通话流程

**通话建立：**
```
用户发起通话 → 主播接受 → 建立RTC连接 → 状态变更为busy → 广播状态
```

**详细步骤：**

1. **发起通话**
   ```javascript
   // 用户端发起通话
   rtcClient.call(kolId, {
     video: true,
     audio: true
   });
   ```

2. **主播接受**
   ```javascript
   // 主播端接受通话
   rtcClient.acceptCall(callId);
   ```

3. **状态变更**
   ```javascript
   // 自动变更状态为busy
   rtsClient.changeStatus('busy');
   ```

4. **状态广播**
   - RTS服务器广播busy状态
   - 所有用户收到状态更新
   - UI显示主播忙碌状态

#### 3.2 消息传递

**消息格式：**
```json
{
  "type": "message",
  "from_user_id": "user_789",
  "to_user_id": "kol_123",
  "message": "Hello, can we talk?",
  "timestamp": 1640995200
}
```

**消息流程：**
```
用户发送消息 → 火山引擎RTS → 主播接收消息 → 主播回复 → 实时显示
```

### 4. 用户下线阶段

#### 4.1 主动下线

**下线流程：**
```
用户点击退出 → 发送下线消息 → 火山引擎RTS → 广播下线状态 → 清理连接
```

**下线消息：**
```json
{
  "type": "user_offline",
  "user_id": "kol_123",
  "user_type": "kol",
  "timestamp": 1640995200
}
```

#### 4.2 异常下线

**异常检测：**
- 网络异常检测
- 心跳超时检测
- 连接断开检测

**异常处理流程：**
```
网络异常 → 心跳超时 → 火山引擎RTS检测 → 自动标记离线 → 广播下线状态
```

**超时配置：**
- 心跳超时：60秒
- 连接超时：30秒
- 重连间隔：3秒、6秒、12秒、24秒

### 5. 重连机制

#### 5.1 网络恢复重连

**重连流程：**
```
网络恢复 → 自动重连 → 重新认证 → 恢复状态同步 → 继续心跳
```

**重连策略：**
| 重连次数 | 延迟时间 | 说明 |
|---------|---------|------|
| 第1次 | 立即 | 网络恢复立即重连 |
| 第2次 | 3秒 | 指数退避 |
| 第3次 | 6秒 | 指数退避 |
| 第4次 | 12秒 | 指数退避 |
| 第5次 | 24秒 | 最后一次尝试 |
| 最大次数 | 5次 | 超过则停止重连 |

#### 5.2 重连状态管理

**重连状态：**
- `connecting`：正在重连
- `connected`：连接成功
- `disconnected`：连接断开
- `failed`：重连失败

## 技术实现细节

### 后端状态管理

#### OnlineStatusService 核心方法

```php
class OnlineStatusService
{
    /**
     * 用户上线处理
     */
    public function handleUserOnline($userId, $userType = 'user')
    
    /**
     * 用户下线处理
     */
    public function handleUserOffline($userId, $userType = 'user')
    
    /**
     * 状态变更处理
     */
    public function handleStatusChange($userId, $status, $userType = 'user')
    
    /**
     * 心跳处理
     */
    public function handleHeartbeat($userId, $userType = 'user')
    
    /**
     * 获取用户状态
     */
    public function getUserStatus($userId, $userType = 'user')
    
    /**
     * 清理过期状态
     */
    public function cleanupExpiredStatus()
}
```

### 客户端实现

#### Web端客户端

```javascript
class OnlineStatusManager {
    constructor(options = {}) {
        this.rtsClient = null;
        this.userStatusCache = new Map();
        this.statusCallbacks = new Map();
    }
    
    async initialize() {
        // 获取RTS Token并建立连接
    }
    
    requestOnlineStatus(userIds) {
        // 请求在线状态
    }
    
    onUserStatusChange(userId, callback) {
        // 监听状态变化
    }
}
```

#### Android端客户端

```kotlin
class OnlineStatusManager(
    wsUrl: String,
    userId: String,
    userType: String = "kol"
) {
    private val wsClient = WebSocketClient(wsUrl, userId, userType)
    
    fun connect() {
        // 建立连接
    }
    
    fun changeStatus(status: String) {
        // 变更状态
    }
    
    fun requestKolStatus(kolIds: List<String>) {
        // 请求主播状态
    }
}
```

## 生命周期时序图

```
时间轴 → 主播端(Android)    火山引擎RTS    用户端(Web)      后端API
  0s   → 应用启动
  1s   → 用户登录
  2s   → 获取RTS Token → 验证Token
  3s   → 连接RTS → 建立连接
  4s   → 发送上线消息 → 广播上线状态 → 接收状态更新
  5s   → 更新UI显示
 30s   → 发送心跳 → 确认心跳
 60s   → 发送心跳 → 确认心跳
 90s   → 发送心跳 → 确认心跳
120s   → 状态变更为busy → 广播状态变更 → 接收状态更新
150s   → 发送心跳 → 确认心跳
180s   → 发送心跳 → 确认心跳
210s   → 发送心跳 → 确认心跳
240s   → 发送心跳 → 确认心跳
270s   → 发送心跳 → 确认心跳
300s   → 发送心跳 → 确认心跳
330s   → 网络异常 → 连接断开
360s   → 自动重连 → 重新建立连接
390s   → 重新认证 → 验证Token
420s   → 恢复心跳 → 确认心跳
450s   → 发送心跳 → 确认心跳
480s   → 用户退出 → 发送下线消息 → 广播下线状态 → 接收状态更新
```

## 监控和日志

### 关键指标

1. **连接成功率**：> 99.9%
2. **消息延迟**：< 100ms
3. **状态同步延迟**：< 50ms
4. **心跳成功率**：> 99.5%
5. **重连成功率**：> 95%

### 日志记录

```php
// 用户上线日志
Log::info("User {$userId} ({$userType}) went online");

// 用户下线日志
Log::info("User {$userId} ({$userType}) went offline");

// 状态变更日志
Log::info("User {$userId} ({$userType}) status changed to {$status}");

// 心跳日志
Log::debug("Heartbeat from user {$userId}");

// 重连日志
Log::warning("Reconnecting user {$userId}, attempt {$attempt}");
```

## 错误处理

### 常见错误及处理

1. **Token过期**
   - 自动重新获取Token
   - 重新建立连接

2. **网络异常**
   - 自动重连机制
   - 指数退避策略

3. **服务器异常**
   - 降级到HTTP轮询
   - 显示离线状态

4. **客户端异常**
   - 清理连接状态
   - 重新初始化

## 性能优化

### 优化策略

1. **连接复用**：复用WebSocket连接
2. **消息批处理**：批量处理状态更新
3. **缓存策略**：Redis缓存热点数据
4. **压缩传输**：启用消息压缩
5. **CDN加速**：使用全球CDN节点

### 监控告警

1. **连接数监控**：实时监控连接数
2. **延迟监控**：监控消息延迟
3. **错误率监控**：监控错误率
4. **资源监控**：监控CPU、内存使用

## 总结

这个实时在线状态同步系统通过火山引擎RTS提供了：

- ✅ **即时性**：毫秒级状态同步
- ✅ **可靠性**：自动重连和心跳机制
- ✅ **一致性**：Redis + 数据库双重保障
- ✅ **扩展性**：支持百万级并发
- ✅ **监控性**：完整的日志记录和状态追踪

确保了用户获得流畅的实时通信体验，同时保证了系统的稳定性和可维护性。 