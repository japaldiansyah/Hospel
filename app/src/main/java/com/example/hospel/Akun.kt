package com.example.hospel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hospel.databinding.ActivityAkunBinding

class Akun : AppCompatActivity() {
    private lateinit var binding: ActivityAkunBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backAkun.setOnClickListener {
            onBackPressed()
        }

        binding.ubahPassword.setOnClickListener {
            val intent = Intent(this@Akun, UbahPassword::class.java)
            startActivity(intent)
        }

        binding.ubahEmail.setOnClickListener {
            val intent = Intent(this@Akun, UbahEmail::class.java)
            startActivity(intent)
        }
    }
}