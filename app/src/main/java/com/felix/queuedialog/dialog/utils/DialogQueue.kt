package com.felix.queuedialog.dialog.utils

import com.felix.queuedialog.dialog.view.QueueDialog
import java.util.*

/**
 * Created by felix
 * on 2022/6/13
 * Description：
 */
class DialogQueue {

    /**
     * Dialog未显示的队列
     */
    private val dialogQueue = LinkedList<QueueDialog>()

    /**
     * 添加dialog
     */
    fun offer(dialog: QueueDialog) {
        // 不允许重复添加相同dialogId的dialog
        if(dialogQueue.contains(dialog)) {
            return
        }
        dialogQueue.offer(dialog)
        Collections.sort(dialogQueue, DialogComparator())
    }

    /**
     * 获取并移除头部的dialog
     */
    fun poll() : QueueDialog? {
        return dialogQueue.poll()
    }

    /**
     * 获取头部dialog
     */
    fun getHeadDialog(): QueueDialog? {
        if (dialogQueue.size > 0) {
            return dialogQueue.first
        }
        return null
    }

    /**
     * 移除指定的dialog
     */
    fun remove(dialog: QueueDialog): Boolean {
        return dialogQueue.remove(dialog)
    }

    /**
     * 根据dialog id查询dialog对象
     */
    fun getDialog(dialogId: String?) : QueueDialog? {
        if (dialogId.isNullOrEmpty()) {
            return null
        }
        val iterator = dialogQueue.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.getDialogId() == dialogId) {
                return next
            }
        }
        return null
    }

    /**
     * 清除队列
     */
    fun clear() {
        dialogQueue.clear()
    }

    /**
     * 获取队列长度
     */
    fun size() : Int {
        return dialogQueue.size
    }

    /**
     * 获取队列
     */
    fun getQueue() : LinkedList<QueueDialog>? {
        return dialogQueue
    }

    /**
     * 按照优先级重新排序
     */
    class DialogComparator : Comparator<QueueDialog> {
        override fun compare(o1: QueueDialog?, o2: QueueDialog?): Int {
            if (o1 != null && o2 != null) {
                return o2.getPriority().level - o1.getPriority().level
            }
            return 0
        }
    }
}