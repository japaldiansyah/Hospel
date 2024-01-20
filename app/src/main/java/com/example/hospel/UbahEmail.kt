package com.example.hospel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hospel.databinding.ActivityUbahEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UbahEmail : AppCompatActivity() {
    private lateinit var binding: ActivityUbahEmailBinding
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUbahEmailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backUbahEmail.setOnClickListener {
            onBackPressed()
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val email = if (isGoogleSignIn()) {
                            currentUser.email
                        } else {
                            document.getString("email") // Gunakan email dari Firestore
                        }

                        binding.etUbahEmail.setText(email)
                    }
                }
        }
        binding.buttonSimpanUbahEmail.setOnClickListener {
            val updatedEmail = if (isGoogleSignIn()) {
                auth.currentUser?.email // Gunakan email dari Google
            } else {
                binding.etUbahEmail.text.toString() // Gunakan email dari formulir jika bukan Google SignIn
            }
            // Simpan perubahan ke Firestore
            val user = auth.currentUser
            if (user != null) {
                val uid = user.uid

                if (user.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }) {
                    // Pengguna login menggunakan Google, buat data baru di Firestore
                    val userMap = hashMapOf(
                        "email" to user.email // Gunakan email dari Google
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
                        "email" to updatedEmail
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
    private fun isGoogleSignIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null && currentUser.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }
    }
}