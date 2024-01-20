package com.example.hospel

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hospel.databinding.ActivityBookingKamarBinding
import com.google.firebase.firestore.FirebaseFirestore

class BookingKamar : AppCompatActivity() {
    private lateinit var binding: ActivityBookingKamarBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingKamarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBookingKamar.setOnClickListener {
            onBackPressed()
        }

        binding.btnBookingKamar.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")
            if (documentId != null) {
                load(documentId)
            }
        }

        val documentId = intent.getStringExtra("documentId")

        if (documentId != null) {
            loadNomorKamar(documentId)
        }
    }

    private fun loadNomorKamar(documentId: String) {
        firestore.collection("kamar_hotel")
            .document(documentId)
            .collection("nomer_kamar")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(this, "Terjadi kesalahan saat memuat nomor kamar", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val statusKamar1 = document.getString("status_kamar1") ?: ""
                        val statusKamar2 = document.getString("status_kamar2") ?: ""
                        val statusKamar3 = document.getString("status_kamar3") ?: ""
                        val statusKamar4 = document.getString("status_kamar4") ?: ""
                        val statusKamar5 = document.getString("status_kamar5") ?: ""
                        val statusKamar6 = document.getString("status_kamar6") ?: ""
                        val statusKamar7 = document.getString("status_kamar7") ?: ""
                        val statusKamar8 = document.getString("status_kamar8") ?: ""
                        val statusKamar9 = document.getString("status_kamar9") ?: ""
                        val statusKamar10 = document.getString("status_kamar10") ?: ""

                        setBackgroundCompat(binding.btnNo1, statusKamar1)
                        setBackgroundCompat(binding.btnNo2, statusKamar2)
                        setBackgroundCompat(binding.btnNo3, statusKamar3)
                        setBackgroundCompat(binding.btnNo4, statusKamar4)
                        setBackgroundCompat(binding.btnNo5, statusKamar5)
                        setBackgroundCompat(binding.btnNo6, statusKamar6)
                        setBackgroundCompat(binding.btnNo7, statusKamar7)
                        setBackgroundCompat(binding.btnNo8, statusKamar8)
                        setBackgroundCompat(binding.btnNo9, statusKamar9)
                        setBackgroundCompat(binding.btnNo10, statusKamar10)

                    }
                }
            }
    }
    private fun setBackgroundCompat(textView: TextView, status: String) {

        val colorResource = when (status) {
            "Kosong" -> R.color.bg_rounded_green
            "Penuh" -> R.color.bg_rounded_red
            else -> {
                R.color.bg_rounded_white // Default color for unexpected status
            }
        }

        val color = ContextCompat.getColor(this, colorResource)
        textView.setBackgroundColor(color)
    }

    private fun load(documentId: String) {
        val intent = Intent(this@BookingKamar, Reservasi::class.java)
        intent.putExtra("documentId", documentId)
        startActivity(intent)
    }
}
