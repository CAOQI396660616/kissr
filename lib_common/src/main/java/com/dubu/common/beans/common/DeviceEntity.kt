package com.dubu.common.beans.common

data class DeviceEntity(
    // Android版本号
    val osVersionCode: String,
    // Android版本名
    val osVersionDisplayName: String,
    // 设备厂商
    val brand: String,
    // 设备名称
    val model: String,
    // 手机宽
    val widthPixels: Int,
    // 手机高
    val heightPixels: Int,
    // 网络类型
    val netType: String,
    // 主机
    val host: String,
    // AndroidId
    val androidId: String,
    // 手机系统语言
    val lang: String,
    // IP地址
    val ipAddress: String,
    // Mac地址
    val macAddress: String,
    // 系统SDK
    val sdk: Int,
    // 架构
    val abi: ArrayList<String>,
    // RAM
    val totalRAM: String,
    // RAM
    val availRAM: String,
    // 内部总存储
    val totalInternalMemorySize: String,
    // 内部可用存储
    val availableInternalMemorySize: String,
    // 外部总存储
    val totalExternalMemorySize: String,
    // 外部可用存储
    val availableExternalMemorySize: String
)
