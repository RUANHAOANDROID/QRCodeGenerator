package com.hao.qrcodegenerator

/**
 * 历史输入
 * @date 2022/6/13
 * @author 锅得铁
 * @since v1.0
 */
data class HistoryEntity(
    val wifi_name: String = "wifi-name",
    val wifi_password: String = "wifi-password",
    val bluetooth_mac: String = "bluetooth-mac",
    val server_url: String = "server-url",
    val server_port: String = "server-port",
    val server_api: String = "server-api"
)
