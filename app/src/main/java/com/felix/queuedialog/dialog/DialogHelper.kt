package com.felix.queuedialog.dialog

import android.app.Activity
import android.content.Context
import com.felix.queuedialog.dialog.utils.DialogQueue
import com.felix.queuedialog.dialog.view.QueueDialog
import java.lang.RuntimeException

/**
 * Created by felix
 * on 2022/6/13
 * Description：
 */
object DialogHelper {

    /** 维护dialog的队列 */
    private val mDialogQueue: DialogQueue by lazy { DialogQueue() }

    /** 当前正在显示的dialog */
    private var mCurrentDialog: QueueDialog? = null

    /****************************************
     * ********** Dialog相关操作方法 **********
     ***************************************/

    /**
     * 添加一个dialog到队列
     */
    fun addDialog(dialog: QueueDialog?) {
        dialog?.let {
            mDialogQueue.offer(it)
        }
    }

    /**
     * 根据dialogId移除一个dialog
     */
    fun removeDialog(dialogId: String?) {
        if (dialogId.isNullOrEmpty()) {
            return
        }
        /** 判断是否是当前正在显示的Dialog 如果是 直接dismiss掉当前正在显示的dialog */
        mCurrentDialog?.let {
            if (it.isShowing && it.getDialogId() == dialogId) {
                it.dismiss()
                return
            }
        }
        /** 不是正在显示的dialog 在队列中移除 */
        val dialog = mDialogQueue.getDialog(dialogId)
        dialog?.let {
            mDialogQueue.remove(it)
        }
    }

    /**
     * 清除所有dialog
     */
    fun clearAll() {
        mDialogQueue.clear()
        mCurrentDialog?.dismiss()
    }

    /**
     * 显示dialog
     */
    fun showDialog() {
        /** 当前没有dialog正在显示, 直接取第一个dialog进行显示 */
        if (mCurrentDialog == null) {
            mCurrentDialog = mDialogQueue.poll()
            mCurrentDialog?.run {
                show()
                setOnDismissListener {
                    mCurrentDialog = null
                    showDialog()
                }
            }
        }
        /** 当前有dialog正在显示，需要判断显示的dialog的优先级
         * 是否比本次需要显示的dialog的优先级低 如果低 取消
         * 当前dialog的显示 将当前的dialog重新入队列 然后显
         * 示优先级较高的dialog */
        else {
            val headDialog = mDialogQueue.getHeadDialog()
            headDialog?.let { head ->
                mCurrentDialog?.let { current ->
                    if (head.getPriority().level > current.getPriority().level) {
                        mDialogQueue.offer(current)
                        current.dismiss()
                    }
                }
            }
        }
    }

    /****************************************
     * ********** 创建Dialog相关方法 **********
     ***************************************/

    fun createDialog(context: Context, message: String): QueueDialog? {
        return createDialog(context, message, null)
    }

    fun createDialog(context: Context, message: String, title: String?): QueueDialog? {
        return createDialog(context, message, title, "OK")
    }

    fun createDialog(
        context: Context, message: String, title: String?,
        button: String
    ): QueueDialog? {
        return createDialog(context, message, title, button, null)
    }

    fun createDialog(
        context: Context, message: String, title: String?,
        button: String, buttonColor: String?
    ): QueueDialog? {
        return createDialog(
            context, message, title, arrayOf(button),
            if (buttonColor == null) null else arrayOf(buttonColor)
        )
    }

    fun createDialog(
        context: Context, message: String, title: String?,
        buttons: Array<String>, buttonColors: Array<String>?
    ): QueueDialog? {
        checkContext(context)
        return QueueDialog(context)
            .setTitle(title)
            .setMessage(message)
            .setDialogId(message)
            .setButton(buttons.asList())
            .setButtonColor(buttonColors?.asList())
    }

    /****************************************
     * **********   私有方法区域   **********
     ***************************************/
    private fun checkContext(context: Context) {
        val result = context is Activity
        if (!result) throw RuntimeException("不能使用application的context")
    }
}