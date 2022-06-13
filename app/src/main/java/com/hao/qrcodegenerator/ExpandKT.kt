package com.hao.qrcodegenerator

import android.app.Activity
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


/**
 * Kotlin 顶级扩展函数
 *@date 2022/3/1
 *@author 锅得铁
 *@constructor default constructor
 */
/**
 * 扩展 Context 以 dataStore
 * @receiver Context
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
