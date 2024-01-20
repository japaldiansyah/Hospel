package com.example.hospel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class AdapterListPesan (val context: Context, val homeList: ArrayList<DataListPesan?>): RecyclerView.Adapter<AdapterListPesan.MyViewHolder>() {
    private val auth = FirebaseAuth.getInstance()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nama_nota = view.findViewById<TextView>(R.id.nama_nota)
        val email_nota = view.findViewById<TextView>(R.id.email_nota)
        val alamat_nota = view.findViewById<TextView>(R.id.alamat_nota)
        val nomor_nota = view.findViewById<TextView>(R.id.nomor_nota)
        val kelas_nota = view.findViewById<TextView>(R.id.kelas_nota)
        val kamar_nota = view.findViewById<TextView>(R.id.kamar_nota)
        val jumlah_hari_nota = view.findViewById<TextView>(R.id.jumlah_hari_nota)
        val total_nota = view.findViewById<TextView>(R.id.total_nota)
        val status_nota = view.findViewById<TextView>(R.id.status_nota)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fetch_list_pesan, parent, false)
        return AdapterListPesan.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = homeList[position]
        val userId = auth.currentUser?.uid

        if (userId != null) {
            holder.nama_nota.text = currentItem?.nama_nota
            holder.email_nota.text = currentItem?.email_nota
            holder.alamat_nota.text = currentItem?.alamat_nota
            holder.nomor_nota.text = currentItem?.nomor_nota
            holder.kelas_nota.text = currentItem?.kelas_nota
            holder.kamar_nota.text = currentItem?.kamar_nota
            holder.jumlah_hari_nota.text = currentItem?.jumlah_hari_nota
            holder.total_nota.text = currentItem?.total_nota
            holder.status_nota.text = currentItem?.status_nota

            // Set warna teks berdasarkan status
            when (currentItem?.status_nota) {
                "Sudah dibayar" -> holder.status_nota.setTextColor(ContextCompat.getColor(context, R.color.green)) // Sesuaikan dengan ID warna hijau
                "Menunggu pembayaran" -> holder.status_nota.setTextColor(ContextCompat.getColor(context, R.color.red)) // Sesuaikan dengan ID warna merah
            }
        }
    }

    override fun getItemCount(): Int {
        return homeList.size
    }
}