package com.example.tapptic

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugCodeUrlTree("TappticApp"))
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}

class TimberDebugCodeUrlTree(
    private val tag: String
) : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "$tag:(${element.fileName}:${element.lineNumber})#${element.methodName}"
    }
}

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR || priority == Log.WARN) {
            //SEND ERROR REPORTS TO YOUR CRASHLYTICS.
        }
    }
}