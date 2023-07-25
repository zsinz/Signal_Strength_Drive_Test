package com.example.ssdt

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.ssdt.data.DataDB
import com.example.ssdt.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val reqcod = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //val db = Room.databaseBuilder(this, DataDB::class.java,"DATADB").allowMainThreadQueries().build()
        //val datsum = db.SumDao().getAllSum()

        Log.v("MainActivity","oncreate main")
        if (cekIzin()){
            binding.btn1a1.isEnabled = true
            binding.btn1a2.isEnabled = true
            binding.btn1a3.isEnabled = false
            binding.btn1a3.text = getString(R.string.btn1a3_1)
        } else {
            mintaIzin()
        }
        binding.btn1a1.setOnClickListener {
            val intent1 = Intent(this, AmbilData::class.java)
            startActivity(intent1)
        }
        binding.btn1a2.setOnClickListener {
            val intent2 = Intent(this, LihatData::class.java)
            startActivity(intent2)
        }
        binding.btn1a3.setOnClickListener {
            mintaIzin()
        }
    }

    private fun cekIzin(): Boolean{
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun mintaIzin(){
       ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),reqcod)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == reqcod){
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                binding.txt1a1.text = getString(R.string.txt1a1)
                binding.btn1a1.isEnabled = true
                binding.btn1a2.isEnabled = true
                binding.btn1a3.isEnabled = false
                binding.btn1a3.text = getString(R.string.btn1a3_1)
            } else {
                binding.txt1a1.text = getString(R.string.txt1a1_1)
            }
        }
    }
}