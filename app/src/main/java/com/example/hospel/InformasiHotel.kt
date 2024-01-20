package com.example.hospel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hospel.databinding.ActivityInformasiHotelBinding

class InformasiHotel : AppCompatActivity() {
    private lateinit var binding : ActivityInformasiHotelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformasiHotelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backInformasiHotel.setOnClickListener {
            onBackPressed()
        }
    }
}