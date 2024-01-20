package com.example.hospel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.example.hospel.databinding.ActivityKelasHotelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class KelasHotel : AppCompatActivity() {
    private lateinit var binding: ActivityKelasHotelBinding
    private var auth = FirebaseAuth.getInstance()
    private var loadingProgressBar: ProgressBar? = null
    private val firestore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKelasHotelBinding.inflate(layoutInflater)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        auth = FirebaseAuth.getInstance()

        setContentView(binding.root)

        binding.backKelasHotel.setOnClickListener {
            onBackPressed()
        }
        binding.kelasEkonomi.setOnClickListener {
            loadDetailKamar("LSdCMTBu4paaSG7iM1kd")
        }
        binding.kelasMenengah.setOnClickListener {
            loadDetailKamar("s8aHBMm2duKrtxWTcSBo")
        }
        binding.kelasPremium.setOnClickListener {
            loadDetailKamar("3y4YsQAaSS1YARn94Ugp")
        }
        binding.kelasEksklusif.setOnClickListener {
            loadDetailKamar("jTev6AGwKLzTmS8cZ6uj")
        }

        firestore.collection("kamar_hotel")
            .document("LSdCMTBu4paaSG7iM1kd") // Ganti dengan ID dokumen yang sesuai
            .addSnapshotListener { dokumen, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (dokumen != null && dokumen.exists()) {
                    val namaHotel = dokumen.getString("nama_kamar")
                    val hargaHotel = dokumen.getString("harga_kamar")
                    val gambarHotel = dokumen.getString("hotel_image")

                    binding.teksKelasEkonomi.text = namaHotel
                    binding.hargaEkonomi.text = hargaHotel

                    // Gunakan Picasso atau library pemuatan gambar lainnya untuk memuat gambar ke dalam ImageView
                    if (gambarHotel != null && gambarHotel.isNotEmpty()) {
                        Picasso.get().load(gambarHotel).into(binding.gambarEkonomi)
                    }
                    loadingProgressBar?.visibility = View.VISIBLE
                }
            }
        firestore.collection("kamar_hotel")
            .document("s8aHBMm2duKrtxWTcSBo") // Ganti dengan ID dokumen yang sesuai
            .addSnapshotListener { dokumen, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (dokumen != null && dokumen.exists()) {
                    val namaHotel = dokumen.getString("nama_kamar")
                    val hargaHotel = dokumen.getString("harga_kamar")
                    val gambarHotel = dokumen.getString("hotel_image")

                    binding.teksKelasMenengah.text = namaHotel
                    binding.hargaMenengah.text = hargaHotel

                    // Gunakan Picasso atau library pemuatan gambar lainnya untuk memuat gambar ke dalam ImageView
                    if (gambarHotel != null && gambarHotel.isNotEmpty()) {
                        Picasso.get().load(gambarHotel).into(binding.gambarMenengah)
                    }
                    loadingProgressBar?.visibility = View.VISIBLE

                }
            }
        firestore.collection("kamar_hotel")
            .document("3y4YsQAaSS1YARn94Ugp") // Ganti dengan ID dokumen yang sesuai
            .addSnapshotListener { dokumen, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (dokumen != null && dokumen.exists()) {
                    val namaHotel = dokumen.getString("nama_kamar")
                    val hargaHotel = dokumen.getString("harga_kamar")
                    val gambarHotel = dokumen.getString("hotel_image")

                    binding.teksKelasPremium.text = namaHotel
                    binding.hargaPremium.text = hargaHotel

                    // Gunakan Picasso atau library pemuatan gambar lainnya untuk memuat gambar ke dalam ImageView
                    if (gambarHotel != null && gambarHotel.isNotEmpty()) {
                        Picasso.get().load(gambarHotel).into(binding.gambarPremium)
                        loadingProgressBar?.visibility = View.VISIBLE
                    }
                }
            }
        firestore.collection("kamar_hotel")
            .document("jTev6AGwKLzTmS8cZ6uj") // Ganti dengan ID dokumen yang sesuai
            .addSnapshotListener { dokumen, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (dokumen != null && dokumen.exists()) {
                    val namaHotel = dokumen.getString("nama_kamar")
                    val hargaHotel = dokumen.getString("harga_kamar")
                    val gambarHotel = dokumen.getString("hotel_image")

                    binding.teksKelasEksklusif.text = namaHotel
                    binding.hargaEksklusif.text = hargaHotel

                    // Gunakan Picasso atau library pemuatan gambar lainnya untuk memuat gambar ke dalam ImageView
                    if (gambarHotel != null && gambarHotel.isNotEmpty()) {
                        Picasso.get().load(gambarHotel).into(binding.gambarEksklusif)
                        loadingProgressBar?.visibility = View.VISIBLE
                    }
                }
            }
    }

    private fun loadDetailKamar(documentId: String) {
        val intent = Intent(this@KelasHotel, DetailKamarEkonomi::class.java)
        intent.putExtra("documentId", documentId)
        startActivity(intent)
    }
}