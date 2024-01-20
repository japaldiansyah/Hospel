package com.example.hospel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hospel.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reservasiHotel.setOnClickListener{
            val intent = Intent(requireContext(), KelasHotel::class.java)
            startActivity(intent)
        }
        binding.informasiHotel.setOnClickListener {
            val intent = Intent(requireContext(), InformasiHotel::class.java)
            startActivity(intent)
        }
    }
}