package com.sixiangtianxia.splash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sixiangtianxia.R
import com.sixiangtianxia.commonlib.base.BaseActivity
import com.sixiangtianxia.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_splash

    override fun initData() {
    }

    override fun initView() {

        toHome.setOnClickListener (this)
    }

    override fun start() {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toHome -> {
                intent2Activity(HomeActivity::class.java)
                finish()
            }
        }
    }
}
