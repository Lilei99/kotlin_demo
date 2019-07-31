package com.sixiangtianxia.home.fragment
import android.os.Bundle
import android.view.View
import com.sixiangtianxia.R
import com.sixiangtianxia.commonlib.base.BaseFragment

class MineFragment : BaseFragment() {
    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_mine;
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