package com.hao.qrcodegenerator.ui

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.hao.qrcodegenerator.HistoryEntity
import com.hao.qrcodegenerator.R
import com.hao.qrcodegenerator.utils.checkPermissions
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private val btnWiFi: Button by lazy { findViewById(R.id.btnWIFI) }
    private val btnBlueTools: Button by lazy { findViewById(R.id.btnBlueTools) }
    private val btnVolumeUp: Button by lazy { findViewById(R.id.btnVolumeUp) }
    private val btnVolumeDown: Button by lazy { findViewById(R.id.btnVolumeDown) }
    private val btnServer: Button by lazy { findViewById(R.id.btnServer) }
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private val tvSSID: TextView by lazy { findViewById(R.id.tvSSID) }
    private val tvPassword: TextView by lazy { findViewById(R.id.tvPassword) }
    private val tvBlueToolsMac: TextView by lazy { findViewById(R.id.tvBlueToolsMac) }
    private val tvIP: TextView by lazy { findViewById(R.id.tvIP) }
    private val tvPort: TextView by lazy { findViewById(R.id.tvPort) }
    private val tvPostDIR: TextView by lazy { findViewById(R.id.tvPostDIR) }
    private val btnScanner: TextView by lazy { findViewById(R.id.btnScannaer) }

    private val qrCodePopup: QRCodePopup by lazy {
        XPopup.Builder(this)
            .popupWidth(600)
            .popupHeight(600)
            .dismissOnTouchOutside(true)
            .asCustom(QRCodePopup(this)) as QRCodePopup
    }
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions(this) { allGranted, grantedList, deniedList ->
            Toast.makeText(this, "如果拒绝", Toast.LENGTH_LONG).show()
            Log.d("CheckPermissions", "$allGranted,$grantedList,$deniedList")
        }

        btnScanner.setOnClickListener {

            Toast.makeText(this, "正在扫描", Toast.LENGTH_LONG).show()
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.let { devices ->
                val showData = Array(devices.size) { "" }
                devices.forEachIndexed { index, bluetoothDevice ->
                    val deviceName = bluetoothDevice.name
                    val deviceHardwareAddress = bluetoothDevice.address // MAC address
                    showData[index] = "${deviceName} ${deviceHardwareAddress}"
                }
                XPopup.Builder(this).autoDismiss(true).asCenterList(
                    "周边蓝牙设备", showData
                ) { position, text ->
                    val macAddress = text.substring(text.lastIndexOf(" "))
                    tvBlueToolsMac.text = macAddress.replace(":", "", true)
                }.show()
            }
        }
        btnWiFi.setOnClickListener {
            val ssid = tvSSID.text.toString()
            val password = tvPassword.text.toString()
            viewModel.qrCodeGenerator("{\"SET_WIFI\":{\"SSID\":\"${ssid}\",\"PASSWORD\":\"${password}\"}}")
        }

        btnBlueTools.setOnClickListener {
            val mac = tvBlueToolsMac.text.toString()
            viewModel.qrCodeGenerator("{\"SET_BLUETOOTH\":{\"BLUETOOTH_MAC\":\"${mac}\"}}")
        }

        btnServer.setOnClickListener {
            val url = tvIP.text.toString()
            val port = tvPort.text.toString()
            val api = tvPostDIR.text.toString()
            viewModel.qrCodeGenerator("{\"SET_SERVICE\":{\"URL\":\"${url}\",\"PORT\":\"${port}\",\"POST_DIR\":\"${api}\"}}")
        }

        btnVolumeUp.setOnClickListener {
            viewModel.qrCodeGenerator("{\"SET_VOICE\":{\"VOICE\":\"+\"}}")
        }

        btnVolumeDown.setOnClickListener {
            viewModel.qrCodeGenerator("{\"SET_VOICE\":{\"VOICE\":\"-\"}}")
        }

        viewModel.getQRCodeImage().observe(this) {
            if (null != it) {
                qrCodePopup.show()
                qrCodePopup.setQRCodeBitmap(it)
                viewModel.saveHistoryInput(
                    HistoryEntity(
                        wifi_name = tvSSID.text.toString(),
                        wifi_password = tvPassword.text.toString(),
                        bluetooth_mac = tvBlueToolsMac.text.toString(),
                        server_url = tvIP.text.toString(),
                        server_port = tvPort.text.toString(),
                        server_api = tvPostDIR.text.toString()
                    )
                )
            }
        }
        lifecycle.coroutineScope.launch {
            viewModel.getHistoryInput().collect {
                it?.let {
                    if (it == "null") return@let
                    var history = HistoryEntity()
                    tvSSID.text = JSONObject(it).optString(history::wifi_name.toString())
                    tvPassword.text = JSONObject(it).optString(history::wifi_password.toString())
                    tvBlueToolsMac.text =
                        JSONObject(it).optString(history::bluetooth_mac.toString())
                    tvIP.text = JSONObject(it).optString(history::server_url.toString())
                    tvPort.text = JSONObject(it).optString(history::server_port.toString())
                    tvPostDIR.text = JSONObject(it).optString(history::server_api.toString())
                }

            }
        }


    }
}