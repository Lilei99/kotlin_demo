package com.sixiangtianxia.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sixiangtianxia.R
import com.sixiangtianxia.login.contract.LoginContract

@Route(path = "/login/LoginActivity")
class LoginActivity : AppCompatActivity(), LoginContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun loginSuccess() {

    }

    override fun loginFaild() {
    }

    override fun jumpToLoginPage() {

    }
}
