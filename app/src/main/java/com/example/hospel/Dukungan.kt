package com.example.hospel

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hospel.databinding.ActivityDukunganBinding

class Dukungan : AppCompatActivity() {
    private lateinit var binding: ActivityDukunganBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDukunganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backDukungan.setOnClickListener {
            onBackPressed()
        }

        binding.buttonDukungan.setOnClickListener {
            val url = "https://docs.google.com/forms/d/e/1FAIpQLSfTrclf5wxcU9EbAtAswVp_VFPyiYwP6K1W3NCQcMpK13eIVg/viewform?vc=0&c=0&w=1&flr=0"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            startActivity(intent)
        }

    }
}