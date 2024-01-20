package com.example.hospel

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.hospel.databinding.ActivityEditProfilBinding
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class EditProfil : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfilBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var currentProfileImageUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backEditProfil.setOnClickListener {
            onBackPressed()
        }

        binding.buttonSimpan.setOnClickListener {
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val fullname = document.getString("nama")
                        val nomor = document.getString("nomor")
                        val alamat = document.getString("alamat")

                        // Tambahkan ini untuk menyimpan URL gambar profil saat ini
                        currentProfileImageUrl = document.getString("profile_image").toString()

                        // Isi formulir dengan data saat ini
                        binding.etNama.setText(fullname)
                        binding.etNomor.setText(nomor)
                        binding.etAlamat.setText(alamat)
                    }
                }
        }
        binding.buttonSimpan.setOnClickListener {
            val updatedFullname = binding.etNama.text.toString()
            val updatedNomor = binding.etNomor.text.toString()
            val updatedAlamat = binding.etAlamat.text.toString()

            // Simpan perubahan ke Firestore
            val user = auth.currentUser
            if (user != null) {
                val uid = user.uid

                if (user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }) {
                    // Pengguna login menggunakan Google, buat data baru di Firestore
                    val userMap = hashMapOf(
                        "nama" to updatedFullname,
                        "nomor" to updatedNomor,
                        "alamat" to updatedAlamat
                    )

                    firestore.collection("users")
                        .document(uid)
                        .set(userMap as Map<String, Any>, SetOptions.merge())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Data gagal disimpan, silakan coba lagi.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    val userMap = hashMapOf(
                        "nama" to updatedFullname,
                        "nomor" to updatedNomor,
                        "alamat" to updatedAlamat
                    )

                    firestore.collection("users")
                        .document(uid)
                        .update(userMap as Map<String, Any>)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data berhasil disimpan.", Toast.LENGTH_SHORT).show()
                            onBackPressed()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Data gagal disimpan, silakan coba lagi.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}
