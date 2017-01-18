package com.zpauly.androidutilcodewithkotlin

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout

/**
 * Created by zpauly on 2017/1/16.
 */
class StatusBarView : View {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet)
}

/**
 * 为DrawerLayout设置颜色
 *
 * @param activity Activity
 * @param drawerLayout DrawerLayout
 * @param color 颜色
 * @param statusBarAlpha 透明度
 */
fun setColorForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, color: Int, statusBarAlpha: Int) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.statusBarColor = Color.TRANSPARENT
    } else {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
    if (contentLayout.childCount > 0 && (contentLayout.getChildAt(0) is StatusBarView)) {
        contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, statusBarAlpha))
    } else {
        contentLayout.addView(createTranslucentStatusBarView(activity, color = color))
    }

    if ((contentLayout.childCount > 0 && contentLayout.getChildAt(1) != null)) {
        contentLayout.getChildAt(1).setPadding(contentLayout.paddingLeft,
                getStatusbarHeight(activity) + contentLayout.paddingTop,
                contentLayout.paddingRight,
                contentLayout.paddingBottom)
    }

    val drawer = drawerLayout.getChildAt(1)
    drawerLayout.fitsSystemWindows = false
    contentLayout.fitsSystemWindows = false
    contentLayout.clipToPadding = true
    drawer.fitsSystemWindows = false

    addTranslucentView(activity, statusBarAlpha)
}

/**
 * 为DrawerLayout布局设置状态栏透明度
 *
 * @param activity Activity
 * @param drawerLayout DrawerLayout
 */
fun setTranslucentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout, statusBarAlpha: Int = Constants.DEFAULT_STATUS_BAR_ALPHA) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    setTranslucentForDrawerLayout(activity, drawerLayout)
    addTranslucentView(activity, statusBarAlpha)
}

/**
 * 为DrawerLayout布局设置状态栏透明(5.0以上半透明效果,不建议使用)
 *
 * @param activity Activity
 * @param drawerLayout DrawerLayout
 */
fun setTranslucentForDrawerLayout(activity: Activity, drawerLayout: DrawerLayout) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.statusBarColor = Color.TRANSPARENT
    } else {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    val contentLayout = drawerLayout.getChildAt(0) as ViewGroup
    if ((contentLayout is LinearLayout).not() && contentLayout.getChildAt(1) != null) {
        contentLayout.getChildAt(1).setPadding(0, getStatusbarHeight(activity), 0, 0)
    }

    contentLayout.fitsSystemWindows = true
    contentLayout.clipToPadding = true
    drawerLayout.getChildAt(1).fitsSystemWindows = false
}

/**
 * 设置状态栏颜色
 *
 * @param activity Activity
 * @param color 颜色
 * @param statusBarAlpha 透明度
 */
fun setColor(activity: Activity, color: Int, statusBarAlpha: Int = Constants.DEFAULT_STATUS_BAR_ALPHA) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.statusBarColor = calculateStatusColor(color, statusBarAlpha)
    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val decorView = activity.window.decorView as ViewGroup
        val count = decorView.childCount
        if (count > 0 && decorView.getChildAt(count - 1) is StatusBarView) {
            decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color, statusBarAlpha))
        } else {
            decorView.addView(createTranslucentStatusBarView(activity, statusBarAlpha))
        }
        setRootView(activity)
    }
}

/**
 * 为头部是ImageView的界面设置状态栏透明
 *
 * @param activity Activity
 * @param statusBarAlpha 状态栏透明度
 * @param needOffsetView 需要向下便宜的View
 */
fun setTranslucentForImageView(activity: Activity, statusBarAlpha: Int = Constants.DEFAULT_STATUS_BAR_ALPHA, needOffsetView: View?) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    setTransparentForWindow(activity)
    addTranslucentView(activity, statusBarAlpha)
    if (needOffsetView != null) {
        val layoutParams = needOffsetView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, getStatusbarHeight(activity), 0, 0)
    }
}

/**
 * 为fragment头部是ImageView的设置状态栏透明
 *
 * @param activity Activity
 * @param statusBarAlpha 状态栏透明度
 * @param needOffsetView 需要向下偏移的View
 */
fun setTranslucentForImageViewInFragment(activity: Activity, statusBarAlpha: Int = Constants.DEFAULT_STATUS_BAR_ALPHA, needOffsetView: View?) {
    setTranslucentForImageView(activity, statusBarAlpha, needOffsetView)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        clearPreviousSetting(activity)
    }
}

/**
 * 获取状态栏的高度
 *
 * @param context 上下文
 * @return 高度
 */
fun getStatusbarHeight(context: Context): Int {
    var result = -1
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelOffset(resourceId)
    }
    return result
}

/**
 * 计算状态栏颜色
 *
 * @param color color值
 * @param alpha alpha值
 * @return 最终状态栏的颜色
 */
private fun calculateStatusColor(color: Int, alpha: Int): Int {
    val a : Float = 1 - alpha / 255f
    var red : Int = color shr 16 and 0xff
    var green : Int = color shr 8 and 0xff
    var blue : Int = color and 0xff
    red = (red * a + 0.5).toInt()
    green = (green * a + 0.5).toInt()
    blue = (blue * a + 0.5).toInt()
    return (0xff shl 24) or (red shl 16) or (green shl 8) or blue
}

/**
 * 清楚先前的设置
 */
private fun clearPreviousSetting(activity: Activity) {
    val decorView = activity.window.decorView as ViewGroup
    val count = decorView.childCount
    if (count > 0 && decorView.getChildAt(count - 1) is StatusBarView) {
        decorView.removeViewAt(count - 1)
        val rootView = (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.setPadding(0, 0, 0, 0)
    }
}

/**
 * 添加半透明矩形条
 *
 * @param activity Activity
 * @param statusBarAlpha 透明度
 */
private fun addTranslucentView(activity: Activity, statusBarAlpha: Int) {
    val contentView = activity.findViewById(android.R.id.content) as ViewGroup
    if (contentView.childCount > 1) {
        contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0 , 0, 0))
    } else {
        contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha))
    }
}

/**
 * 设置根目录参数
 *
 * @param activity Activity
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
private fun setRootView(activity: Activity) {
    val rootView = (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
    rootView.fitsSystemWindows = true
    rootView.clipToPadding = true
}

/**
 * 创建矩形的View
 *
 * @param activity Activity
 * @param alpha 透明度
 * @return 创建的View
 */
private fun createTranslucentStatusBarView(activity: Activity, alpha: Int = -1, color: Int = -1): StatusBarView {
    val statusBarView = StatusBarView(activity)
    val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusbarHeight(activity))
    val c = if (alpha != -1) { if (color != -1) calculateStatusColor(color, alpha) else Color.argb(alpha, 0, 0, 0) } else color
    statusBarView.setBackgroundColor(c)
    statusBarView.layoutParams = params
    return statusBarView
}

/**
 * 设置状态栏透明
 *
 * @param activity Activiy
 */
fun setTransparentForWindow(activity: Activity) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        activity.window.statusBarColor = Color.TRANSPARENT
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        activity.window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

/**
 * 使状态栏透明
 *
 * @param activity Activity
 */
fun transparentStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity.window.statusBarColor = Color.TRANSPARENT
    } else {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

/**
 * 设置透明状态栏
 *
 * @param activity Activity
 */
fun setTransparentStatusBar(activity: Activity) {
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
}

/**
 * 隐藏状态栏
 *
 * @param activity Activity
 */
fun hideStatusBar(activity: Activity) {
    activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    activity.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

/**
 * 判断状态栏是否存在
 *
 * @param activity Activity
 * @return boolean
 */
fun isStatusBarExists(activity: Activity): Boolean
        = (activity.window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN

/**
 * 获取ActionBar的高度
 *
 * @param activity Activity
 * @return ActionBar的高度
 */
fun getActionBarHeight(activity: Activity): Int {
    val tv = TypedValue()
    if (activity.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelOffset(tv.data, activity.resources.displayMetrics)
    }
    return 0
}

/**
 * 显示通知栏
 *
 * @param context 上下文
 * @param isSettingPanel 打开设置或通知
 */
fun showNotificationBar(context: Context, isSettingPanel: Boolean)
        = invokePanels(context, if (Build.VERSION.SDK_INT <= 16) { "expand" } else { if (isSettingPanel) "expandSettingsPanel" else "expandNotificationsPanel" })

/**
 * 隐藏通知栏
 *
 * @param context 上下文
 */
fun hideNorificationBar(context: Context)
        = invokePanels(context, if (Build.VERSION.SDK_INT <= 16) "collapse" else "collapsePanel")

/**
 * 反射唤醒通知栏
 *
 * @param context 上下文
 * @param methodName 方法名
 */
fun invokePanels(context: Context, methodName: String) {
    try {
        val service = context.getSystemService("statusbar")
        val statusBarManager = Class.forName("android.app.StatusBarManager")
        val expand = statusBarManager.getDeclaredMethod(methodName)
        expand.isAccessible = true
        expand.invoke(service)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}






