package com.zpauly.androidutilcodewithkotlin

import android.graphics.drawable.Drawable

/**
 * Created by zpauly on 2017/1/14.
 */
data class AppInfo(var name: String,
                   var icon: Drawable,
                   var packageName: String,
                   var packagePath: String,
                   var versionName: String,
                   var versionCode: Int,
                   var isSystem: Boolean)