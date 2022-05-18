package com.example.android.dessertclicker

import android.app.Application
import timber.log.Timber

/** Application Class 전체 app에서 한번만 초기화하면 되는 것들을 여기에 작성한다.
 * app내의 모든 다른 Activity보다 먼저 실행된다. 라이브러리 초기화가 주로 이루어 진다.*/
class ClickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber logging 라이브러리 초기화
        Timber.plant(Timber.DebugTree())
    }
}