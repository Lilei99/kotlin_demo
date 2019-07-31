package com.sixiangtianxia.home.fragment

import android.os.Bundle
import android.view.View
import com.sixiangtianxia.R
import com.sixiangtianxia.commonlib.base.BaseFragment

class ClassFragment : BaseFragment() {

    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): ClassFragment {
            val fragment = ClassFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_class;
    }

    override fun initView() {
    }

    override fun lazyLoad() {
    }

    override fun onClick(v: View?) {

    }

    override fun useEventBus(): Boolean {
        return false
    }
}