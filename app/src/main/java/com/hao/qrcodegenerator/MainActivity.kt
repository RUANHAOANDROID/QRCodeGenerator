package com.hao.qrcodegenerator

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private val btnWiFi: Button by lazy { findViewById(R.id.btnWIFI) }
    private val btnBlueTools: Button by lazy { findViewById(R.id.btnBlueTools) }
    private val btnVolumeUp: Button by lazy { findViewById(R.id.btnVolumeUp) }
    private val btnVolumeDown: Button by lazy { findViewById(R.id.btnVolumeDown) }
    private val imageQrCode: ImageView by lazy { findViewById(R.id.imageQRCode) }
    private val btnServer: Button by lazy { findViewById(R.id.btnServer) }
    private val viewModel: MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnWiFi.setOnClickListener {
            val ssid = findViewById<TextView>(R.id.tvSSID).text.toString()
            val password = findViewById<TextView>(R.id.tvPassword).text.toString()
            viewModel.qrCodeGenerator("{\"SET_WIFI\":{\"SSID\":\"${ssid}\",\"PASSWORD\":\"${password}\"}}")
        }

        btnBlueTools.setOnClickListener {
            val mac = findViewById<TextView>(R.id.tvBlueToolsMac).text.toString()
            viewModel.qrCodeGenerator("{\"SET_BLUETOOTH\":{\"BLUETOOTH_MAC\":\"${mac}\"}}")
        }

        btnServer.setOnClickListener {
            val url = findViewById<TextView>(R.id.tvIP).text.toString()
            val port = findViewById<TextView>(R.id.tvPort).text.toString()
            val api = findViewById<TextView>(R.id.tvPostDIR).text.toString()
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
                imageQrCode.setImageBitmap(it)
            }
        }

    }
}