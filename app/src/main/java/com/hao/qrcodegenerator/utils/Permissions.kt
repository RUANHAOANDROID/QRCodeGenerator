package com.hao.qrcodegenerator.utils

import android.Manifest
import android.Manifest.permission.BLUETOOTH_CONNECT
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import com.permissionx.guolindev.request.ExplainScope
import com.permissionx.guolindev.request.ForwardScope

/**
 *@date 2022/2/25
 *@author 锅得铁
 *@constructor default constructor
 */
fun AppCompatActivity.checkPermissions(
    activity: AppCompatActivity,
    requestCallback: RequestCallback
) {
    PermissionX.init(activity).permissions(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        BLUETOOTH_CONNECT
    ).explainReasonBeforeRequest()
        .onExplainRequestReason { scope: ExplainScope, deniedList: List<String> ->
            scope.showRequestReasonDialog(
                deniedList,
                "为了正常使用你必须同意以下权限:",
                "我已明白"
            )
        }
        .onForwardToSettings { scope: ForwardScope, deniedList: List<String> ->
            scope.showForwardToSettingsDialog(
                deniedList,
                "您需要去应用程序设置当中手动开启权限",
                "我已明白"
            )
        }
        .request { allGranted: Boolean, grantedList: List<String>, deniedList: List<String> ->
            requestCallback.onResult(
                allGranted,
                grantedList,
                deniedList
            )
        }
}
