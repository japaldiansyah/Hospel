package com.example.hospel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SyaratDanKetentuanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.syarat_dan_ketentuan)
        val back = findViewById<ImageView>(R.id.back_sdk)
        back.setOnClickListener {
            val intent = Intent(this@SyaratDanKetentuanActivity, Daftar::class.java)
            startActivity(intent)
        }
        val buttonSyarat = findViewById<Button>(R.id.button_syarat)
        buttonSyarat.setOnClickListener {
            val intent = Intent(this@SyaratDanKetentuanActivity, Daftar::class.java)
            intent.putExtra("CHECKBOX_STATUS", true)
            startActivity(intent)
        }
    }
}