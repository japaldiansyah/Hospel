package com.example.hospel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hospel.databinding.FragmentPesanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PesanFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pesanList: ArrayList<DataListPesan?>  // Tetapkan tipe data pesanList menjadi ArrayList<DataListPesan?>
    private lateinit var binding: FragmentPesanBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPesanBinding.inflate(layoutInflater, container, false)
        recyclerView = binding.recyclerViewPesan
        pesanList = ArrayList<DataListPesan?>()



        // Ambil data dari Firestore
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { userDocument ->
                    val nama = userDocument.getString("nama") ?: ""
                    val email = userDocument.getString("email") ?: ""
                    val alamat = userDocument.getString("alamat") ?: ""
                    val nomor = userDocument.getString("nomor") ?: ""

                    // Tambahkan data dari koleksi "users" ke pesanList
                    pesanList.add(DataListPesan(nama, email, alamat, nomor, "", "", "", "", ""))

                    // Ambil data dari subkoleksi "nota" yang berada di dalam koleksi "users"
                    firestore.collection("users")
                        .document(uid)
                        .collection("nota")
                        .get()
                        .addOnSuccessListener { notaQuerySnapshot ->
                            for (notaDocument in notaQuerySnapshot) {
                                val kelas_nota = notaDocument.getString("kelas_hotel") ?: ""
                                val kamar_nota = notaDocument.getString("kamar_hotel") ?: ""
                                val jumlah_hari_nota = notaDocument.getString("jumlah_hari") ?: ""
                                val total_nota = notaDocument.getString("total") ?: ""
                                val status_nota = notaDocument.getString("status") ?: ""


                                // Tambahkan data dari subkoleksi "nota" ke pesanList
                                pesanList.add(DataListPesan(nama, email,alamat,nomor, kelas_nota, kamar_nota,jumlah_hari_nota, total_nota, status_nota))

                                if (isAdded && activity != null) {
                                    populateData()
                                }
                            }
                        }
                        .addOnFailureListener { notaException ->
                            // Handle kesalahan jika terjadi saat mengambil data dari subkoleksi "nota"
                            Log.e("PesanFragment", "Gagal menampilkan nota pembayaran", notaException)
                        }
                }
                .addOnFailureListener { userException ->
                    // Handle kesalahan jika terjadi saat mengambil data dari koleksi "users"
                    Log.e("PesanFragment", "Gagal menampilkan data pengguna", userException)
                }
        }
        return binding.root
    }

    private fun populateData() {
        val linearLayout = LinearLayoutManager(activity)
        linearLayout.stackFromEnd = true
        linearLayout.reverseLayout = true
        recyclerView.layoutManager = linearLayout

        // Gunakan pesanList untuk inisialisasi adapter
        val adp = AdapterListPesan(requireActivity(), pesanList)
        recyclerView.adapter = adp
    }
}
