package com.sixiangtianxia.commonlib

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.squareup.leakcanary.RefWatcher
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

abstract class MyApplication : Application() {

    private var refWatcher: RefWatcher? = null

    companion object {
        val TAG = "MyApplication"
        private var application: MyApplication? = null
        fun getInstance(): MyApplication? {
            return application
        }

        var context: Context by Delegates.notNull()
            private set

        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }
    }


    override fun onCreate() {
        super.onCreate()
        application = this
        // mRefWatcher = LeakCanary.install(this@MyApplication)
        init()
    }

    fun init() {

    }

    var disposable: Disposable? = null


    fun clearMemory() {
        Glide.get(this).clearMemory()
        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(data: ObservableEmitter<String>) {
                try {
                    Glide.get(this@MyApplication).clearDiskCache()
                    data.onNext("成功")
                    data.onComplete()
                } catch (e: Exception) {
                    data.onError(e)
                }

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                    disposable?.dispose()
                    disposable = null
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: String) {
                }

                override fun onError(e: Throwable) {
                    disposable?.dispose()
                    disposable = null
                }

            })

    }

}