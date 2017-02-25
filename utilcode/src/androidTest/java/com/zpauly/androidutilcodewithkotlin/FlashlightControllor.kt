package com.zpauly.androidutilcodewithkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by zpauly on 2017/2/24.
 */
@SuppressLint("NewApi")
class FlashlightControllor {
    private val TAG = "FlashlightControllor"

    private val DISPATCH_ERROR = 0
    private val DISPATCH_CHANGED = 1
    private val DISPATCH_AVAILABILITY_CHANGED = 2

    private val mCameraManager: CameraManager
    private var mHandler: Handler? = null
    private val mListeners = ArrayList<WeakReference<FlashlightListener>>(1)
    private var mFlashlightEnabled = false

    private var mCameraId: String? = null
    private var mTorchAvailable = false

    constructor(mContext: Context) {
        mCameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try {
            mCameraId = getCameraId()
        } catch (e: Throwable) {
            return
        }

        if (mCameraId != null) {
            ensureHandler()
            mCameraManager.registerTorchCallback(mTorchCallback, mHandler)
        }
    }

    private fun ensureHandler() {
        synchronized(this) {
            if (mHandler != null) {
                val thread = HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND)
                thread.start()
                mHandler = Handler(thread.looper)
            }
        }
    }

    private fun getCameraId(): String? {
        val ids = mCameraManager.cameraIdList
        var c: CameraCharacteristics
        var flashAvailable: Boolean
        var lensFacing: Int
        ids.map {
            c = mCameraManager.getCameraCharacteristics(it)
            flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            lensFacing = c.get(CameraCharacteristics.LENS_FACING)
            if (flashAvailable != null
                    && flashAvailable
                    && lensFacing != null
                    && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                return it
            }
        }
        return null
    }

    private fun dispatchModeChanged(enabled: Boolean) =
        dispatchListeners(DISPATCH_CHANGED, enabled)

    private fun dispatchModeError() =
        dispatchListeners(DISPATCH_ERROR)

    private fun dispatchModeAvailablityChanged(available: Boolean) =
        dispatchListeners(DISPATCH_AVAILABILITY_CHANGED, available)

    private fun dispatchListeners(message: Int, arguement: Boolean = false) {
        synchronized(mListeners) {
            var cleanup = false
            mListeners.map {
                var l = it.get()
                if (l != null) {
                    when (message) {
                        DISPATCH_ERROR -> l.onFlashlightError()
                        DISPATCH_CHANGED -> l.onFlashlightChanged(arguement)
                        DISPATCH_AVAILABILITY_CHANGED -> l.onFlashlightAvailablityChanged(arguement)
                    }
                } else {
                    cleanup = true
                }
            }

            if (cleanup) {
                cleanUpListenersLocked(null)
            }
        }
    }

    private fun cleanUpListenersLocked(listener: FlashlightListener?) {
        mListeners.map {
            val found = it.get()
            if (found == null || found == listener) {
                mListeners.remove(it)
            }
        }
    }

    private val mTorchCallback = object: CameraManager.TorchCallback() {
        override fun onTorchModeChanged(cameraId: String?, enabled: Boolean) {
            super.onTorchModeChanged(cameraId, enabled)
        }

        override fun onTorchModeUnavailable(cameraId: String?) {
            super.onTorchModeUnavailable(cameraId)
        }

        fun setCameraAvailable(available: Boolean) {
            var changed: Boolean = false
            synchronized(this@FlashlightControllor) {
                changed = mTorchAvailable != available
                mTorchAvailable = available
            }

            if (changed) {
                dispatchModeAvailablityChanged(available)
            }
        }

        fun setTorchMode(enabled: Boolean) {
            var changed: Boolean = false
            synchronized(this@FlashlightControllor) {
                changed = mTorchAvailable != enabled
                mTorchAvailable = enabled
            }

            if (changed) {
                dispatchModeChanged(enabled)
            }
        }
    }

    interface FlashlightListener {
        fun onFlashlightChanged(enable: Boolean)

        fun onFlashlightError()

        fun onFlashlightAvailablityChanged(available: Boolean)
    }
}
