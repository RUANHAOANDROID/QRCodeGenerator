package com.hao.qrcodegenerator

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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
            val bitmap = generateImage(text)
            _bitmapLivaData.postValue(bitmap)
        }
    }

    fun getQRCodeImage(): LiveData<Bitmap> {
        return _bitmapLivaData
    }

    private fun generateImage(text: String): Bitmap? {
        var qrImage: Bitmap? = null
        if (text.trim { it <= ' ' }.isEmpty()) {
            return null
        }
        val size = 600
        val hintMap: MutableMap<EncodeHintType, Any?> = EnumMap(
            EncodeHintType::class.java
        )
        hintMap[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hintMap[EncodeHintType.MARGIN] = 1
        val qrCodeWriter = QRCodeWriter()
        try {
            val byteMatrix = qrCodeWriter.encode(
                text, BarcodeFormat.QR_CODE, size,
                size, hintMap
            )
            val height = byteMatrix.height
            val width = byteMatrix.width
            qrImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    qrImage.setPixel(x, y, if (byteMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return qrImage
    }
}