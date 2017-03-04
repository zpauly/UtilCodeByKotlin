package com.zpauly.androidutilcodewithkotlin

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference

/**
 * Created by zpauly on 2017/2/25.
 */
public class HandlerHolder constructor(mListener: OnReceiveMessageListener) : Handler() {
    val mListenerWeakReference : WeakReference<OnReceiveMessageListener> = WeakReference(mListener)

    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
            mListenerWeakReference.get().handleMessage(msg)
        }
    }
}

public interface OnReceiveMessageListener {
    fun handleMessage(msg: Message?)
}
