package com.sixiangtianxia.home.fragment

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import com.sixiangtianxia.R
import com.sixiangtianxia.commonlib.base.BaseFragment
import com.sixiangtianxia.commonlib.utils.ScreenUtils
import com.sixiangtianxia.commonlib.utils.ToastUtils
import com.sixiangtianxia.home.HomeActivity
import com.sixiangtianxia.login.contract.LoginContract
import com.sixiangtianxia.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment(), LoginContract.View {

    private var mTitle: String? = null
    var presenter: LoginContract.Presenter? = null

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home;
    }


    override fun initView() {
        val activity = activity as HomeActivity
        val statusBarHeight = activity.getStatusBarHeight(context!!)
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(40f)
        )
        params.topMargin = statusBarHeight + 22
        params.leftMargin = ScreenUtils.dp2px(22f)
        params.rightMargin = ScreenUtils.dp2px(60f)

        presenter = LoginPresenter()
        presenter!!.setView(this)

        aaa!!.setOnClickListener(this)
    }

    override fun lazyLoad() {

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.aaa -> {
//                showLoading()
//                presenter!!.login()
//                Log.i("aaaaa","android".lastchar().toString())
                ToastUtils.show("啊啊啊")
            }
        }
    }

    fun String.lastchar(): Char = this.get(this.length - 1)

    override fun useEventBus(): Boolean {
        return false
    }

    override fun loginSuccess() {

    }

    override fun loginFaild() {
    }

    override fun jumpToLoginPage() {
    }

    suspend fun awdasd() {

        var job = GlobalScope.launch {
            println("world")
        }
        job.cancelAndJoin()
    }

}