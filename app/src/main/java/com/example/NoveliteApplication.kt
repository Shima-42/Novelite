package com.example

import android.app.Application
import android.os.StrictMode
import android.util.Log

class NoveliteApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    
    // Enable StrictMode to detect main thread disk/network I/O
    StrictMode.setThreadPolicy(
      StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .penaltyLog()
        .build()
    )

    StrictMode.setVmPolicy(
      StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .build()
    )

    Log.d("NoveliteApp", "StrictMode initialized successfully in NoveliteApplication.onCreate()")
  }
}
