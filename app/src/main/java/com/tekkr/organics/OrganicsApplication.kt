package com.tekkr.organics

import android.app.Application
import com.google.firebase.FirebaseApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class OrganicsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/WorkSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build())
                ).build())
        FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var application: Application
    }
}