package com.example.moisoftqr

import android.content.Context


class SessionManager(val context: Context) {

    companion object {
        val IsLogin: String = "IsLogin"
        val QRcode: String = "qrcode"
    }

    val sharedPreferences = context.getSharedPreferences("MoisoftQR", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    init {
        editor.apply()
    }

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(IsLogin, false)
    }

    fun getQRcode(): String? {
        return sharedPreferences.getString(QRcode, "")
    }
    fun logoutUser() {
        editor.putString(QRcode, "")
        editor.putBoolean(IsLogin, false)
        editor.commit()
    }

    fun setLogin() {
        editor.putBoolean(IsLogin, true)
        editor.commit()
    }

    fun setQRcode(qrcode: String) {
        editor.putString(QRcode, qrcode)
        editor.commit()
    }
}