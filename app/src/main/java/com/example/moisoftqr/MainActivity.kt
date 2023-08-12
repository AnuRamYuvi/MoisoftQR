package com.example.moisoftqr

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moisoftqr.dbhandler.DataBaseHandler
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    lateinit var session: SessionManager
    lateinit var ed_name: EditText
    lateinit var ed_city: EditText
    lateinit var ed_initial: EditText
    lateinit var ed_mobile: EditText
    lateinit var ed_work: EditText
    lateinit var btn_genrate: Button
    lateinit var dbhandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManager(this@MainActivity)
        dbhandler = DataBaseHandler(this@MainActivity)


        ed_name = findViewById(R.id.ed_name)
        ed_city = findViewById(R.id.ed_city)
        ed_initial = findViewById(R.id.ed_initial)
        ed_mobile = findViewById(R.id.ed_mobile)
        ed_work = findViewById(R.id.ed_work)
        btn_genrate = findViewById(R.id.btn_generate)

        val font1 = Typeface.createFromAsset(assets, "Bamini.ttf")
        ed_name.setTypeface(font1)
        ed_initial.setTypeface(font1)
        ed_city.setTypeface(font1)
        ed_mobile.setTypeface(font1)
        ed_work.setTypeface(font1)

        btn_genrate.setOnClickListener {
            if (validation()) {
                val Str = String.format(
                    "%s&%s %s %s&%s",
                    ed_city.text,
                    ed_initial.text,
                    ed_name.text,
                    ed_work.text,
                    ed_mobile.text
                )
                session.setLogin()
                session.setQRcode(Str)
                val exequery: Boolean = dbhandler.insertQRDetails(Str)
                if (exequery) {
                    val intent = Intent(this@MainActivity, QRActivity::class.java)
                    startActivity(intent)
                    finish()
                }


            }
        }
    }


    private fun validation(): Boolean {
        let {
            if (ed_city.text.isEmpty()) {
                Toast.makeText(this, "Enter City", Toast.LENGTH_SHORT).show()
                return false
            } else if (ed_initial.text.isEmpty()) {
                Toast.makeText(this, "Enter Intial", Toast.LENGTH_SHORT).show()
                return false
            } else if (ed_name.text.isEmpty()) {
                Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show()
                return false
            } /*else if (ed_work.text.isEmpty()) {
                Toast.makeText(this, "Enter Work", Toast.LENGTH_SHORT).show()
                return false
            }*/ else if (ed_mobile.text.isEmpty() || ed_mobile.text.length != 10) {
                Toast.makeText(this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true
    }
}