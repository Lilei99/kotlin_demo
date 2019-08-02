package com.sixiangtianxia.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sixiangtianxia.R

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        println("android".lastchar())

    }

    fun String.lastchar(): Char = this.get(this.length - 1)
}
