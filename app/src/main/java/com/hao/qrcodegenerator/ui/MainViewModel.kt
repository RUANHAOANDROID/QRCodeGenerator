package com.hao.qrcodegenerator.ui

import android.graphics.Bitmap
import android.util.JsonWriter
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hao.qrcodegenerator.App
import com.hao.qrcodegenerator.HistoryEntity
import com.hao.qrcodegenerator.PreferencesKey
import com.hao.qrcodegenerator.dataStore
import com.hao.qrcodegenerator.utils.QRCodeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * 生成Bitmap
 * @date 2022/6/13
 * @author 锅得铁
 * @since v1.0
 */
class MainViewModel : ViewModel() {
    private val _bitmapLivaData = MutableLiveData<Bitmap>()


    fun qrCodeGenerator(text: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val bitmap = QRCodeUtils().generateImage(text)
            _bitmapLivaData.postValue(bitmap)
        }
    }

    fun getQRCodeImage(): LiveData<Bitmap> {
        return _bitmapLivaData
    }

    fun getHistoryInput(): Flow<String> {
        return App.context.dataStore.data.map {
            it[PreferencesKey.INPUT].toString()
        }
    }

    fun saveHistoryInput(inputHistory: HistoryEntity) {
        viewModelScope.launch(context = Dispatchers.IO) {
            App.context.dataStore.edit {
                it[PreferencesKey.INPUT] = StringBuffer()
                    .append("{")

                    .append("\"${inputHistory::wifi_name}\"")
                    .append(":")
                    .append("\"${inputHistory.wifi_name}\"")
                    .append(",")

                    .append("\"${inputHistory::wifi_password}\"")
                    .append(":")
                    .append("\"${inputHistory.wifi_password}\"")
                    .append(",")

                    .append("\"${inputHistory::bluetooth_mac}\"")
                    .append(":")
                    .append("\"${inputHistory.bluetooth_mac}\"")
                    .append(",")

                    .append("\"${inputHistory::server_api}\"")
                    .append(":")
                    .append("\"${inputHistory.server_api}\"")
                    .append(",")

                    .append("\"${inputHistory::server_port}\"")
                    .append(":")
                    .append("\"${inputHistory.server_port}\"")
                    .append(",")

                    .append("\"${inputHistory::server_url}\"")
                    .append(":")
                    .append("\"${inputHistory.server_url}\"")

                    .append("}")
                    .toString()
            }
        }
    }
}