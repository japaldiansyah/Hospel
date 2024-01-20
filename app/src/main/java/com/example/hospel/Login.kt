package com.example.hospel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hospel.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private var loadingProgressBar: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LoginActivity", "onCreate called")
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingProgressBar = findViewById(R.id.loadingProgressBar)


        auth = Firebase.auth

        binding.emailLogin.background = null
        binding.passwordLogin.background = null

        binding.daftarLogin.setOnClickListener {
            val intent = Intent(this@Login, Daftar::class.java)
            startActivity(intent)
        }

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            val webClientId = getString(R.string.default_web_client_id)

            if (webClientId.isNotEmpty()) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(webClientId)
                    .requestEmail()
                    .build()

                googleSignInClient = GoogleSignIn.getClient(this, gso)
            } else {
                Toast.makeText(this, "Web client ID is empty or null", Toast.LENGTH_SHORT).show()
                loadingProgressBar?.visibility = View.VISIBLE
            }

            binding.buttonLogin.setOnClickListener {
                signInUser()
            }

            binding.loginGoogle.setOnClickListener {
                signInWithGoogle()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (auth.currentUser != null && !isTaskRoot) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private var loginAttempts = 0

    private fun signInUser() {
        val email = binding.emailLogin.text.toString()
        val password = binding.passwordLogin.text.toString()

        clearErrors()

        if (validateForm(email, password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginAttempts = 0
                        startActivity(Intent(this, MainActivity::class.java))
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        finish()
                    } else {
                        loginAttempts++

                        if (loginAttempts >= 6) {
                            Toast.makeText(
                                this,
                                "Percobaan login gagal terlalu banyak. Akun diblokir.",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadingProgressBar?.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(this, "Login gagal. Silakan coba lagi.", Toast.LENGTH_SHORT)
                                .show()
                            loadingProgressBar?.visibility = View.VISIBLE
                        }
                    }
                }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResult(task)
            }
        }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            account?.let {
                Log.d("GoogleSignIn", "Google Sign-In Successful. Account: ${it.displayName}")
                updateUI(it)
                loadingProgressBar?.visibility = View.VISIBLE
            }
        } else {
            Log.e("GoogleSignIn", "Google Sign-In Failed", task.exception)
            Toast.makeText(this, "Login gagal, silakan coba lagi", Toast.LENGTH_SHORT).show()
            loadingProgressBar?.visibility = View.VISIBLE
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("GoogleSignIn", "Firebase Sign-In Successful")
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                loadingProgressBar?.visibility = View.VISIBLE
            } else {
                Log.e("GoogleSignIn", "Firebase Sign-In Failed", it.exception)
                Toast.makeText(this, "Login Gagal!", Toast.LENGTH_SHORT).show()
                loadingProgressBar?.visibility = View.VISIBLE

            }
        }
    }


    private fun validateForm(email: String, password: String): Boolean {
        var isValid = true

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.pkEmailLogin.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.pkEmailLogin.visibility = View.INVISIBLE
        }

        if (TextUtils.isEmpty(password) || password.length < 8) {
            binding.pkPasswordLogin.visibility = View.VISIBLE
            isValid = false
        } else {
            binding.pkPasswordLogin.visibility = View.INVISIBLE
        }

        return isValid
    }

    private fun clearErrors() {
        binding.pkEmailLogin.visibility = View.INVISIBLE
        binding.pkPasswordLogin.visibility = View.INVISIBLE
    }
}
