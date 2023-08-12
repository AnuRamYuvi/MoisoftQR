package com.example.moisoftqr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        gotoNext()
    }

    fun gotoNext() {
        val sessionManager = SessionManager(this@Splash)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (sessionManager.isLogin()) {
                    startActivity(Intent(this@Splash, QRActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@Splash, MainActivity::class.java))
                    finish()
                }

            },
            600
        )
    }

}