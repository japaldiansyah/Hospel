package com.example.hospel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.hospel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    val fragHome : Fragment = HomeFragment()
    val fragProfil : Fragment = ProfilFragment()
    val fragPesan : Fragment = PesanFragment()
    var hideHomeFragment : Fragment = fragHome
    var hideProfilFragment : Fragment = fragProfil
    var hidePesanFragment : Fragment = fragPesan
    val fm : FragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fm.beginTransaction().add(R.id.fragmentContainerView, fragHome).show(fragHome).commit()
        fm.beginTransaction().add(R.id.fragmentContainerView, fragProfil).hide(fragProfil).commit()
        fm.beginTransaction().add(R.id.fragmentContainerView, fragPesan).hide(fragPesan).commit()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){

                R.id.homeFragment -> replaceFragment(HomeFragment())
                R.id.settingsFragment -> replaceFragment(ProfilFragment())
                R.id.pesanFragment -> replaceFragment(PesanFragment())

                else ->{

                }
            }
            true
        }
    }
    private fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}