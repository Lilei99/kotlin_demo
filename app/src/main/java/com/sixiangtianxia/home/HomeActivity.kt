package com.sixiangtianxia.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.sixiangtianxia.R
import com.sixiangtianxia.bean.TabEntity
import com.sixiangtianxia.commonlib.base.BaseActivity
import com.sixiangtianxia.home.fragment.ClassFragment
import com.sixiangtianxia.home.fragment.HomeFragment
import com.sixiangtianxia.home.fragment.MineFragment
import com.sixiangtianxia.home.fragment.StudyFragment
import kotlinx.android.synthetic.main.activity_home.*
import java.util.ArrayList

class HomeActivity : BaseActivity() {

    private val mTitles = arrayOf("首页", "精品课", "学习", "我的")

    // 未被选中的图标
    private val mIconUnSelectIds =
        intArrayOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher)
    // 被选中的图标
    private val mIconSelectIds =
        intArrayOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher)

    private val mTabEntities = ArrayList<CustomTabEntity>()

    private var mHomeFragment: HomeFragment? = null
    private var mStudyFragment: StudyFragment? = null
    private var mClassFragment: ClassFragment? = null
    private var mMineFragment: MineFragment? = null

    //默认为0
    private var mIndex = 0
    var aaa: TextView? = null
//    var tab_layout: CommonTabLayout? = null
//    var fl_container: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)

        var list = ArrayList<String>()
        list.forEach(::println)
    }

    override fun layoutId(): Int {
        return R.layout.activity_home
    }

    override fun initData() {

    }

    fun get(): String {
        return ""
    }

    override fun initView() {

        aaa = findViewById(R.id.aaa)
//        tab_layout = findViewById(R.id.tab_layout)
//        fl_container = findViewById(R.id.fl_container)
        aaa!!.setOnClickListener(this)
    }

    override fun start() {

    }

    override fun doubleBackExitApp(): Boolean {
        return true
    }

    //初始化底部菜单
    private fun initTab() {
        (0 until mTitles.size)
            .mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        //为Tab赋值
        tab_layout!!.setTabData(mTabEntities)
        tab_layout!!.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换Fragment
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    /**
     * 切换Fragment
     * @param position 下标
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 // 首页
            -> mHomeFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mHomeFragment = it
                transaction.add(R.id.fl_container, it, "home")
            }
            1  //学习
            -> mStudyFragment?.let {
                transaction.show(it)
            } ?: StudyFragment.getInstance(mTitles[position]).let {
                mStudyFragment = it
                transaction.add(R.id.fl_container, it, "discovery")
            }
            2  //精品课
            -> mClassFragment?.let {
                transaction.show(it)
            } ?: ClassFragment.getInstance(mTitles[position]).let {
                mClassFragment = it
                transaction.add(R.id.fl_container, it, "hot")
            }
            3 //我的
            -> mMineFragment?.let {
                transaction.show(it)
            } ?: MineFragment.getInstance(mTitles[position]).let {
                mMineFragment = it
                transaction.add(R.id.fl_container, it, "mine")
            }

            else -> {

            }
        }

        mIndex = position
        tab_layout!!.currentTab = mIndex
        transaction.commitAllowingStateLoss()
    }

    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mClassFragment?.let { transaction.hide(it) }
        mStudyFragment?.let { transaction.hide(it) }
        mMineFragment?.let { transaction.hide(it) }
    }


    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
//        showToast("onSaveInstanceState->"+mIndex)
//        super.onSaveInstanceState(outState)
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        if (tab_layout != null) {
            outState.putInt("currTabIndex", mIndex)
        }
    }

    override fun onClick(v: View) {
        when (v!!.id) {
            R.id.aaa -> {
//                Toast.makeText(this@HomeActivity, "aaaa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
