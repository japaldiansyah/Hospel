package com.example.hospel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hospel.databinding.ActivityUbahPasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UbahPassword : AppCompatActivity() {
    private lateinit var binding: ActivityUbahPasswordBinding
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backUbahPassword.setOnClickListener {
            onBackPressed()
        }

        binding.buttonSimpanUbahPassword.setOnClickListener {
            ubahPassword()
        }

    }

    private fun ubahPassword() {
        val passwordSaatIni = binding.etPasswordSaatIni.text.toString()
        val passwordBaru = binding.etPasswordBaru.text.toString()
        val konfirmasiPasswordBaru = binding.etKonfirmasiPasswordBaru.text.toString()

        val user: FirebaseUser? = auth.currentUser
        if (user != null && user.email != null) {
            // Memeriksa apakah password saat ini sesuai
            val credential = EmailAuthProvider.getCredential(user.email!!, passwordSaatIni)
            user.reauthenticate(credential)
                .addOnSuccessListener {
                    // Password saat ini sesuai, lanjutkan dengan memeriksa kondisi lainnya
                    if (passwordBaru == passwordSaatIni) {
                        // Password baru tidak boleh sama dengan password saat ini
                        Toast.makeText(this, "Password baru tidak boleh sama dengan password saat ini", Toast.LENGTH_SHORT).show()
                    } else if (passwordBaru != konfirmasiPasswordBaru) {
                        // Konfirmasi password harus sama dengan password baru
                        Toast.makeText(this, "Konfirmasi password harus sama dengan password baru", Toast.LENGTH_SHORT).show()
                    } else {
                        // Lakukan perubahan password
                        user.updatePassword(passwordBaru)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                                onBackPressed()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Gagal mengubah password: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Password saat ini tidak sesuai
                    Toast.makeText(this, "Password saat ini salah", Toast.LENGTH_SHORT).show()
                }
        }
    }
}