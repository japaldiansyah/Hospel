package com.example.hospel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.hospel.databinding.ActivityDaftarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class Daftar : AppCompatActivity() {
    private lateinit var binding : ActivityDaftarBinding
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.namaDaftar.background = null
        binding.emailDaftar.background = null
        binding.passwordDaftar.background = null

        binding.syaratDanKetentuan.setOnClickListener {
            val intent = Intent(this@Daftar, SyaratDanKetentuanActivity::class.java)
            startActivity(intent)
        }
        binding.login.setOnClickListener {
            val intent = Intent(this@Daftar, Login::class.java)
            startActivity(intent)
        }

        val checkboxStatus = intent.getBooleanExtra("CHECKBOX_STATUS", false)
        binding.cbDaftar.isChecked = checkboxStatus

        binding.buttonDaftar.setOnClickListener {
            val nama = binding.namaDaftar.text.toString()
            val email = binding.emailDaftar.text.toString()
            val password = binding.passwordDaftar.text.toString()
            val checkBoxChecked: Boolean = binding.cbDaftar.isChecked


            clearErrors()

            if (validateForm(nama, email, password, checkBoxChecked)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            if (user != null) {
                                val uid = user.uid
                                val userMap = HashMap<String, Any>()
                                userMap["nama"] = nama
                                userMap["email"] = email

                                firestore.collection("users")
                                    .document(uid)
                                    .set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Selamat Daftar Berhasil!", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@Daftar, Login::class.java).apply {
                                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        })
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Gagal menyimpan email ke Firestore", Toast.LENGTH_SHORT).show()                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registrasi Gagal! ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun validateForm(fullname: String, email: String, password: String, checkBoxChecked: Boolean): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(fullname)) {
            binding.pkNama?.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.pkNama?.visibility = View.INVISIBLE
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.pkEmail?.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.pkEmail?.visibility = View.INVISIBLE
        }

        if (TextUtils.isEmpty(password) || password.length < 8) {
            binding.pkPassword?.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.pkPassword?.visibility = View.INVISIBLE
        }

        if (!checkBoxChecked) {
            Toast.makeText(applicationContext, "Baca Syarat dan Ketentuan jika setuju centang checkbox untuk mendaftar!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun clearErrors() {
        binding.pkNama?.visibility = View.INVISIBLE
        binding.pkEmail?.visibility = View.INVISIBLE
        binding.pkPassword?.visibility = View.INVISIBLE
    }
}