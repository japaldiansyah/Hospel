package com.example.hospel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hospel.databinding.ActivityReservasiBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Reservasi : AppCompatActivity() {
    private lateinit var binding: ActivityReservasiBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var total: String = ""
    private var kelasKamar: String = ""

    private var isProcessing = false // Variable untuk memastikan tidak ada proses ganda

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val fullname = document.getString("nama")
                        val email = document.getString("email")
                        val nomor = document.getString("nomor")
                        val alamat = document.getString("alamat")

                        // Isi formulir dengan data saat ini
                        binding.namaReservasi.setText(fullname)
                        binding.emailReservasi.setText(email)
                        binding.etNomorReservasi.setText(nomor)
                        binding.etAlamatReservasi.setText(alamat)
                    }
                }
        }

        binding.btnSimpanReservasi.setOnClickListener {
            if (isProcessing) {
                // Jika sedang dalam proses, keluar dari fungsi
                return@setOnClickListener
            }

            // Menampilkan kotak dialog konfirmasi
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        val title =
            "Transfer ke BCA no rek : 1234567890 Atas Nama : Aditya Yuda Pamungkas. Transfer sesuai total yang harus dibayarkan yang tampil di menu Pesan"
        val message = "Apakah anda ingin melanjutkan pembayaran?"

        builder.setTitle("Perhatian")
        builder.setMessage("$title\n\n$message")
        builder.setPositiveButton("Lanjutkan") { dialog, which ->
            // Eksekusi prosesnya
            executeReservationProcess()
            executeReservationProcess2()
        }
        builder.setNegativeButton("Tidak") { dialog, which ->
            // Tindakan jika memilih "Tidak"
            // Di sini Anda dapat menambahkan tindakan yang sesuai, jika diperlukan
            Toast.makeText(this, "Pemesanan dibatalkan", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    private fun executeReservationProcess() {
        // Mengunci proses
        isProcessing = true

        val jumlahHari = binding.teksJumlahHari.text.toString()
        val nomorKamar = binding.etNomorKamarReservasi.text.toString()

        // Memastikan tidak ada nilai yang kosong
        if (jumlahHari.isEmpty() || nomorKamar.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show()
            // Membuka kunci setelah proses selesai
            isProcessing = false
            return
        }

        // Simpan perubahan ke Firestore
        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid

            val userMap = hashMapOf(
                "jumlah_hari" to jumlahHari,
                "kamar_hotel" to nomorKamar,
                "status" to "Menunggu pembayaran"
            )

            // Update data in the "users" collection
            firestore.collection("users")
                .document(uid)
                .set(userMap as Map<String, Any>, SetOptions.merge())
                .addOnSuccessListener {
                    // Continue to save data to the "nota" subcollection
                    firestore.collection("users")
                        .document(uid)
                        .collection("nota")
                        .add(userMap)
                        .addOnSuccessListener {
                            // Jika sukses, pindah ke MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(
                                this,
                                "Pemesanan berhasil! Lakukan pembayaran, dan mohon tunggu sedang diverifikasi oleh admin",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Pemesanan gagal!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnCompleteListener {
                            // Membuka kunci setelah proses selesai
                            isProcessing = false
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Pemesanan gagal!", Toast.LENGTH_SHORT).show()
                    // Membuka kunci setelah proses selesai (termasuk jika ada kegagalan)
                    isProcessing = false
                }
        }
    }
    private fun executeReservationProcess2() {
        // Mengunci proses
        isProcessing = true

        val documentId = intent.getStringExtra("documentId")

        if (documentId != null) {
            loadKamar(documentId)
        }

        // Ambil nilai dari binding dan simpan di dalam variabel
        total = binding.total.text.toString()
        kelasKamar = binding.kelasKamar.text.toString()

        // Simpan perubahan ke Firestore
        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid

            val userMap = hashMapOf(
                "kelas_hotel" to total,
                "total" to kelasKamar
            )

            // Update data in the "users" collection
            firestore.collection("users")
                .document(uid)
                .set(userMap as Map<String, Any>, SetOptions.merge())
                .addOnSuccessListener {
                    // Continue to save data to the "nota" subcollection
                    firestore.collection("users")
                        .document(uid)
                        .collection("nota")
                        .add(userMap)
                        .addOnSuccessListener {
                            // Jika sukses, pindah ke MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(
                                this,
                                "Pemesanan berhasil! Lakukan pembayaran, dan mohon tunggu sedang diverifikasi oleh admin",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Pemesanan gagal!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnCompleteListener {
                            // Membuka kunci setelah proses selesai
                            isProcessing = false
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Pemesanan gagal!", Toast.LENGTH_SHORT).show()
                    // Membuka kunci setelah proses selesai (termasuk jika ada kegagalan)
                    isProcessing = false
                }
        }
    }

    private fun loadKamar(documentId: String) {
        firestore.collection("kamar_hotel")
            .document(documentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(
                        this,
                        "Terjadi kesalahan saat memuat nomor kamar",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    total = snapshot.getString("harga_kamar") ?: ""
                    kelasKamar = snapshot.getString("nama_kamar") ?: ""

                    binding.total.setText(total)
                    binding.kelasKamar.setText(kelasKamar)
                }
            }
    }
}
