package com.example.hospel

data class DataListPesan(
    val nama_nota: String,
    val email_nota: String,
    val alamat_nota: String,
    var nomor_nota: String?,
    var kelas_nota: String?,
    var kamar_nota: String?,
    var jumlah_hari_nota: String?,
    var total_nota: String?,
    var status_nota: String?
)
