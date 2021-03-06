package com.example.ysuselfstudy

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import cn.bmob.v3.Bmob
import com.example.ysuselfstudy.data.BiyingPic
import com.example.ysuselfstudy.data.InformCollect
import com.example.ysuselfstudy.data.Information
import com.example.ysuselfstudy.logic.Dao
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.sdk.QbSdk
import com.tencent.tauth.Tencent
import org.litepal.LitePal

class YsuSelfStudyApplication : Application() {
    private val TAG = "YsuSelfStudyApplication"

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var myinform: MutableSet<InformCollect>
        lateinit var tencent: Tencent
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        tencent = Tencent.createInstance("101560830", context)
        QbSdk.initX5Environment(context, null)
        LitePal.initialize(context)
        myinform = mutableSetOf()
        Bugly.init(context, "ec45c74684", false)
        Bmob.initialize(this, "95472b5edd3fe00a7bc245de053edb71")
    }
}