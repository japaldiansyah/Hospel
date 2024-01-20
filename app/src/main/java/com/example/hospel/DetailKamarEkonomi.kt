package com.example.hospel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hospel.databinding.ActivityDetailKamarEkonomiBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class DetailKamarEkonomi : AppCompatActivity() {
    private lateinit var binding : ActivityDetailKamarEkonomiBinding
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKamarEkonomiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backDetailKamar.setOnClickListener {
            onBackPressed()
        }

        binding.buttonDetail.setOnClickListener {
            val documentId = intent.getStringExtra("documentId")
            if (documentId != null) {
                loadBookingKamar(documentId)
            }
        }

        val documentId = intent.getStringExtra("documentId")

        if (documentId != null) {
            loadKamarDetail(documentId)
        }
    }

    private fun loadKamarDetail(documentId: String) {
        firestore.collection("kamar_hotel")
            .document(documentId)
            .addSnapshotListener { dokumen, error ->
                if (error != null) {
                    // Handle error, misalnya tampilkan pesan kesalahan
                    return@addSnapshotListener
                }

                if (dokumen != null && dokumen.exists()) {
                    val namaHotel = dokumen.getString("nama_kamar")
                    val gambarHotel = dokumen.getString("hotel_image")
                    val deskripsi = dokumen.getString("deskripsi")
                    val fasilitas = dokumen.getString("fasilitas")

                    binding.teksKelasDetailKamar.text = namaHotel
                    binding.teksDeskripsiDetail.text = deskripsi
                    binding.fasilitas.text = fasilitas

                    // Gunakan Picasso atau library pemuatan gambar lainnya untuk memuat gambar ke dalam ImageView
                    if (gambarHotel != null && gambarHotel.isNotEmpty()) {
                        Picasso.get().load(gambarHotel).into(binding.gambarDetailKamar)
                    }
                }
            }
    }

    private fun loadBookingKamar(documentId: String) {
        val intent = Intent(this@DetailKamarEkonomi, BookingKamar::class.java)
        intent.putExtra("documentId", documentId)
        startActivity(intent)
    }
}