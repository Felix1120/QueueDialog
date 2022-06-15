package com.felix.queuedialog.dialog.enum

/**
 * Created by felix
 * on 2022/6/13
 * Description：
 */
enum class DialogPriority(val level: Int) {
    /**
     * 低优先级
     */
    LOW(0),

    /**
     * 普通优先级
     */
    NORMAL(1),

    /**
     * 高优先级
     */
    HEIGHT(2),

    /**
     * 严重错误
     */
    ERROR(3),

    /**
     * 系统级别
     */
    SYSTEM(4),
}