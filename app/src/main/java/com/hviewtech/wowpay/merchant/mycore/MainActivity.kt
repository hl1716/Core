package com.hviewtech.wowpay.merchant.mycore

import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.hviewtech.wowpay.merchant.core.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        val adapter =
            object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_recycler) {
                override fun convert(holder: BaseViewHolder, item: String) {
                    holder.setText(R.id.item_text, item)
                }
            }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.animationEnable = true
        adapter.data = arrayListOf("111", "ddd")
        val view =LayoutInflater.from(this).inflate(R.layout.item_recycler,null)
        adapter.addHeaderView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("---------","onCreate")
    }


    override fun onRestart() {
        super.onRestart()
        Log.i("---------","onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.i("---------","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("---------","onResume")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.i("---------","onAttachedToWindow")
    }

    override fun onPause() {
        super.onPause()
        Log.i("---------","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("---------","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("---------","onDestroy")
    }
}