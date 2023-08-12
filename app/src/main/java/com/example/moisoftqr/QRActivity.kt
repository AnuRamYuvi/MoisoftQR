package com.example.moisoftqr

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moisoftqr.dbhandler.DataBaseHandler
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.util.*


class QRActivity : AppCompatActivity() {
    lateinit var iv_qr: ImageView
    lateinit var btn_logout: Button
    lateinit var btn_add: Button
    lateinit var txt_str: TextView
    lateinit var txt_search: TextView
    lateinit var ed_search: EditText
    lateinit var rv_qrstr: RecyclerView
    lateinit var session: SessionManager
    lateinit var dbhandler: DataBaseHandler
    lateinit var qrlist: ArrayList<HashMap<String, String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        session = SessionManager(this@QRActivity)
        dbhandler = DataBaseHandler(this@QRActivity)
        iv_qr = findViewById(R.id.iv_qr)
        btn_logout = findViewById(R.id.btn_logout)
        btn_add = findViewById(R.id.btn_add)
        txt_str = findViewById(R.id.txt_str)
        txt_search = findViewById(R.id.txt_str)
        rv_qrstr = findViewById(R.id.rv_qrstr)
        ed_search = findViewById(R.id.ed_search)


        val qrstr = session.getQRcode()
        txt_str.text = qrstr
        iv_qr.setImageBitmap(qrstr?.let { getQrCodeBitmap(it) })
        //  iv_qr.setImageBitmap(qrstr?.let { encodeAsBitmap(it) })


        displayQr()

        btn_add.setOnClickListener {
            val intent = Intent(this@QRActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        ed_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString());
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        btn_logout.setOnClickListener {
            session.logoutUser()
            val exequery: Boolean = dbhandler.delete();
            if (exequery) {
                val intent = Intent(this@QRActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
    }

    private fun getQrCodeBitmap(qrstring: String): Bitmap {
        val size = 512 //pixels

        var hints: Hashtable<EncodeHintType?, Any?>? = null
        val encoding: String = guessAppropriateEncoding(qrstring).toString()
        if (encoding != null) {
            hints = Hashtable(2)
            hints[EncodeHintType.CHARACTER_SET] = encoding
        } // Make the QR code buffer border narrower
        val bits = QRCodeWriter().encode(qrstring, BarcodeFormat.QR_CODE, size, size, hints)

        return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
    }


    private fun guessAppropriateEncoding(contents: CharSequence): String? {
        // Very crude at the moment
        for (element in contents) {
            if (element.code > 0xFF) {
                return "UTF-8"
            }
        }
        return null
    }

    fun displayQr() {
        qrlist = dbhandler.getQRDetails()
        rv_qrstr?.isNestedScrollingEnabled = false
        rv_qrstr?.setHasFixedSize(true)
        rv_qrstr?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        rv_qrstr?.adapter =
            QRAdapter(qrlist) { item ->
                Log.e("print qr value", item.toString())
                val qrstr = item?.get("QRvalue").toString()
                txt_str.text = qrstr
                iv_qr.setImageBitmap(qrstr?.let { getQrCodeBitmap(it) })
            }
    }

    fun filter(text: String?) {
        val temp = java.util.ArrayList<java.util.HashMap<String, String>>()
        for (d in qrlist) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (text?.let { d.get("QRvalue")?.contains(it) } == true) {
                temp.add(d)
            }
        }

        rv_qrstr?.adapter =
            QRAdapter(temp) { item ->
                Log.e("print qr value", item.toString())
                val qrstr = item?.get("QRvalue").toString()
                txt_str.text = qrstr
                iv_qr.setImageBitmap(qrstr?.let { getQrCodeBitmap(it) })
            }
        //update recyclerview
        // rv_qrstr?.adapter.updateList(temp)
    }
}