package com.cathay.exammvvmdemo.base


import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.NavHostFragment
import com.cathay.exammvvmdemo.databinding.ViewToolbarBinding


abstract class ToolbarFragment : BaseFragmnet() {
    private lateinit var rootBinding: ViewToolbarBinding
    private var backClick : (() -> Unit)? = null

    fun setBackPress( click:() -> Unit){
        this.backClick = click
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = initContentView(inflater, container, savedInstanceState)
        rootBinding = ViewToolbarBinding.inflate(inflater, container, false)
        rootBinding.content.addView(view)
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (backClick == null){
                    nav().navigateUp()
                }else{
                    backClick?.invoke()
                }

                return@OnKeyListener true
            }
            false
        })
        return rootBinding.root
    }

    abstract fun initContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    @CallSuper
    override fun initView() {
        val activity = context as AppCompatActivity
        activity.setSupportActionBar(rootBinding.toolbar)
    }

    @CallSuper
    override fun listen() {
        rootBinding.ivBack.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }
    }

    fun setNavigationHomeClickListen(click: (View) -> Unit) {
        rootBinding.ivBack.setOnClickListener {
            click.invoke(it)
        }
    }

    fun hideBackArrow() {
        rootBinding.ivBack.visibility = View.GONE
    }

    fun setToolbarTitle(title: String?) {
        rootBinding.title.text = title
    }

    fun getToolbar():Toolbar = rootBinding.toolbar



}