package com.bed.android.bedrock

import android.app.Application
import com.bed.android.bedrock.di.Injector

class BedRockApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Injector.application = this
    }
}