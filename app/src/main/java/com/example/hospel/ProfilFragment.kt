package com.example.hospel

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hospel.databinding.FragmentProfilBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private var auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageReference: StorageReference = storage.reference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfilBinding.inflate(layoutInflater, container, false)
        auth = FirebaseAuth.getInstance()
        binding.gantiProfil.setOnClickListener {
            selectImage()
        }

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            val userDocRef = firestore.collection("users").document(uid)

            userDocRef.addSnapshotListener { document, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (document != null && document.exists()) {
                    val displayName = document.getString("nama")
                    val email = document.getString("email")
                    val profileImageURL = document.getString("profile_image")

                    if (profileImageURL != null) {
                        Picasso.get()
                            .load(profileImageURL)
                            .into(binding.gambarProfilPengaturan)
                    }
                    binding.usernamePengaturan.text = displayName
                    binding.emailPengaturan.text = email
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editProfil.setOnClickListener {
            val intent = Intent(requireContext(), EditProfil::class.java)
            startActivity(intent)
        }

        binding.tentangKami.setOnClickListener {
            val intent = Intent(requireContext(), TentangKami::class.java)
            startActivity(intent)
        }

        binding.dukungan.setOnClickListener {
            val intent = Intent(requireContext(), Dukungan::class.java)
            startActivity(intent)
        }

        binding.setting.setOnClickListener {
            val intent = Intent(requireContext(), Akun::class.java)
            startActivity(intent)
        }

        binding.exit.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    // Metode untuk memilih gambar dari penyimpanan perangkat
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    // Metode untuk menangani hasil pemilihan gambar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data

            // Upload gambar ke Firebase Storage
            if (imageUri != null) {
                uploadImage(imageUri)
            }
        }
    }

    // Metode untuk mengunggah gambar ke Firebase Storage
    private fun uploadImage(imageUri: Uri) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            // Hapus foto profil lama dari Firebase Storage
            deleteOldProfileImage(uid)

            // Upload gambar ke Firebase Storage
            val profileImageRef = storageReference.child("profile_images/$uid.jpg")

            profileImageRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Simpan URL gambar baru di Firestore
                        firestore.collection("users")
                            .document(uid)
                            .update("profile_image", imageUrl)
                            .addOnSuccessListener {
                                // Berhasil mengunggah dan menyimpan URL baru
                            }
                            .addOnFailureListener {
                                // Gagal menyimpan URL baru
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Gagal mengunggah gambar baru
                }
        }
    }

    // Metode untuk menghapus foto profil lama dari Firebase Storage
    private fun deleteOldProfileImage(uid: String) {
        val oldProfileImageRef = storageReference.child("profile_images/$uid.jpg")

        oldProfileImageRef.delete()
            .addOnSuccessListener {
                // Foto profil lama berhasil dihapus
                Log.d("StorageDelete", "Foto profil lama berhasil dihapus")
            }
            .addOnFailureListener { exception ->
                // Gagal menghapus foto profil lama
                Log.e("StorageDelete", "Gagal menghapus foto profil lama: ${exception.message}")
            }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi Keluar")
        builder.setMessage("Apakah Anda ingin keluar dari aplikasi?")
        builder.setPositiveButton("Ya") { _, _ ->
            logoutUser()
        }
        builder.setNegativeButton("Tidak", null)
        builder.show()
    }

    private fun logoutUser() {
        if (auth.currentUser != null) {
            // Sign out from Firebase
            auth.signOut()

            // Sign out from Google
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
            googleSignInClient.signOut()

            // Redirect to Login activity
            val intent = Intent(requireContext(), Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}