package com.hao.qrcodegenerator.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import com.hao.qrcodegenerator.R
import com.lxj.xpopup.core.CenterPopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 *@date 2022/6/13
 *@author 锅得铁
 *@constructor default constructor
 */
class QRCodePopup(context: Context) : CenterPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.layout_qrcode
    }

    private val imageView: ImageView by lazy { findViewById(R.id.imageView) }
    override fun onCreate() {
        super.onCreate()
        popupImplView.background = XPopupUtils.createDrawable(Color.WHITE, 5f, 5f, 5f, 5f)
    }

    fun setQRCodeBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }
}