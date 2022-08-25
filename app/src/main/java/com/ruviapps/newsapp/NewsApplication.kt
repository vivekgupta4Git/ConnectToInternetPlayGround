package com.ruviapps.newsapp

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class NewsApplication: Application() {

    fun provideRealmConfig() : RealmConfiguration =
        RealmConfiguration.Builder().schemaVersion(1L).build()

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

}