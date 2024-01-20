package com.example.hospel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hospel.databinding.ActivityEditProfilBinding
import com.example.hospel.databinding.ActivityTentangKamiBinding

class TentangKami : AppCompatActivity() {
    private lateinit var binding: ActivityTentangKamiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangKamiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backTentangKami.setOnClickListener {
            onBackPressed()
        }
    }
}