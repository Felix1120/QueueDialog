package com.felix.queuedialog.dialog.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDialog
import com.felix.queuedialog.R
import com.felix.queuedialog.databinding.DialogBaseBinding
import com.felix.queuedialog.dialog.enum.DialogPriority
import com.felix.queuedialog.dialog.enum.DialogStyle
import com.felix.queuedialog.dialog.listener.DialogOnClickListener
import com.felix.queuedialog.dialog.listener.DialogOnDismissListener

/**
 * Created by felix
 * on 2022/6/13
 * Description：
 */
class QueueDialog : AppCompatDialog{

    private var rootView = DialogBaseBinding.inflate(layoutInflater)

    /**
     * title
     * 标题
     */
    private var mTitle: String ?= null

    /**
     * message
     * 内容
     */
    private var mMessage: String ?= null

    /**
     * button array
     * 按钮数组
     */
    private var mButtonArray: List<String> ?= null

    /**
     * button color array
     * 按钮颜色数组
     */
    private var mButtonColorArray: List<String> ?= null

    /**
     * button click listener
     * 按钮点击事件回调
     */
    private var mOnClickListener: DialogOnClickListener ?= null

    /**
     * dialog dismiss listener
     * Dialog Dismiss回调
     */
    private var mOnDismissClickListener : DialogOnDismissListener ?= null

    /**
     * dialog priority
     * 优先级
     */
    private var mPriority: DialogPriority = DialogPriority.NORMAL

    /**
     * dialog id
     * 唯一Id
     */
    private var mDialogId: String = ""

    /**
     * 是否允许按返回键关闭Dialog
     */
    private var mIsBackPressedClose: Boolean = false

    constructor(context: Context): this(context, R.style.QueueDialog)

    constructor(context: Context, themeResId: Int):super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView.root)
        setFullScreen()
        initData()
    }


    /******************************
     ********    私有方法区   *******
     *****************************/

    /**
     * 初始化入口
     */
    private fun initData() {
        setListener()
    }

    /**
     * 处理全屏
     */
    private fun setFullScreen() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val attributes = window?.attributes
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.gravity = Gravity.CENTER
        attributes?.windowAnimations = R.style.DialogAnimNormal

        // 兼容刘海屏
        if (Build.VERSION.SDK_INT >= 28) {
            attributes?.layoutInDisplayCutoutMode = WindowManager
                .LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        window?.attributes = attributes
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window?.decorView?.systemUiVisibility = options
    }

    /**
     * 设置监听
     */
    private fun setListener() {
        rootView.tvLeft.setOnClickListener {
            mOnClickListener?.onClick(0)
            dismiss()
        }
        rootView.tvRight.setOnClickListener {
            mOnClickListener?.onClick(1)
            dismiss()
        }
    }

    /**
     * 刷新View
     */
    private fun refreshView() {
        mTitle?.let{
            rootView.tvTitle.visibility = View.VISIBLE
            rootView.tvTitle.text = it
        }
        mMessage?.let { rootView.tvMessage.text = it }
        mButtonArray?.let {
            when (it.size) {
                1 -> {
                    rootView.tvLeft.visibility = View.VISIBLE
                    rootView.tvRight.visibility = View.GONE
                    rootView.line.visibility = View.GONE
                    rootView.tvLeft.text = it[0]
                }
                2 -> {
                    rootView.tvLeft.visibility = View.VISIBLE
                    rootView.tvRight.visibility = View.VISIBLE
                    rootView.tvLeft.text = it[0]
                    rootView.tvRight.text = it[1]
                }
                else -> {
                    rootView.tvLeft.visibility = View.GONE
                    rootView.tvRight.visibility = View.GONE
                }
            }
        }
        mButtonColorArray?.let {
            when (it.size) {
                1 -> {
                    rootView.tvLeft.setTextColor(Color.parseColor(it[0]))
                }
                2 -> {
                    rootView.tvLeft.setTextColor(Color.parseColor(it[0]))
                    rootView.tvRight.setTextColor(Color.parseColor(it[1]))
                }
            }
        }
    }

    /******************************
     ********    公共方法区   *******
     *****************************/

    /**
     * 设置标题
     */
    fun setTitle(title: String?): QueueDialog {
        mTitle = title
        return this
    }

    /**
     * 设置内容
     */
    fun setMessage(message: String): QueueDialog {
        mMessage = message
        return this
    }

    /**
     * 设置按钮数组
     */
    fun setButton(buttons: List<String>): QueueDialog {
        mButtonArray = buttons
        return this
    }

    /**
     * 设置按钮颜色
     */
    fun setButtonColor(colors: List<String>?): QueueDialog {
        mButtonColorArray = colors
        return this
    }

    /**
     * 设置点击事件监听
     */
    fun setOnClickListener(listener: DialogOnClickListener?): QueueDialog {
        mOnClickListener = listener
        return this
    }

    /**
     * 设置dismiss监听
     */
    fun setOnDismissListener(listener: DialogOnDismissListener?): QueueDialog {
        mOnDismissClickListener = listener
        return this
    }

    /**
     * 设置View
     */
    fun setView(childView: View?): QueueDialog {
        childView?.let{
            rootView.llDialog.visibility = View.GONE
            rootView.flView.visibility = View.VISIBLE
            rootView.flView.removeAllViews()
            rootView.flView.addView(it)
        }
        return this
    }

    /**
     * 设置弹窗优先级
     */
    fun setPriority(priority: DialogPriority) : QueueDialog {
        this.mPriority = priority
        return this
    }

    /**
     * 获取弹窗优先级
     */
    fun getPriority() : DialogPriority {
        return mPriority
    }

    /**
     * 设置唯一标识Id
     */
    fun setDialogId(dialogId: String) : QueueDialog {
        this.mDialogId = dialogId
        return this
    }

    /**
     * 获取唯一标识Id
     */
    fun getDialogId() : String {
        return mDialogId
    }

    /**
     * 弹窗动画
     */
    fun setDialogStyle(style: DialogStyle) : QueueDialog {
        when(style) {
            DialogStyle.NORMAL -> {}
            DialogStyle.BOTTOM -> {}
            DialogStyle.LEFT -> {}
            DialogStyle.RIGHT -> {}
        }
        return this
    }

    /**
     * 设置是否屏蔽返回键
     */
    fun setBackPressedClose(isBackClose: Boolean) : QueueDialog{
        this.mIsBackPressedClose = isBackClose
        return this
    }

    /******************************
     ********  重写父类方法区 *******
     *****************************/

    override fun show() {
        super.show()
        refreshView()
    }

    override fun dismiss() {
        super.dismiss()
        mOnDismissClickListener?.onDismiss()
    }

    override fun equals(other: Any?): Boolean {
        val queueDialog = other as QueueDialog
        return queueDialog.mDialogId == this.mDialogId
    }

    override fun onBackPressed() {
        if (mIsBackPressedClose) {
            super.onBackPressed()
        }
    }
}