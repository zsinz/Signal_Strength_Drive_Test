package com.example.ssdt

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.ssdt.data.DataDB
import com.example.ssdt.databinding.HitungBinding
import kotlin.math.round

class Hitung:AppCompatActivity() {
    private lateinit var binding: HitungBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("hitung","oncreate hitung")
        binding = HitungBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idsum = getIntent().getIntegerArrayListExtra("key")
        Log.d("get intent",idsum.toString())
        if (idsum!!.size > 0){
            val db = Room.databaseBuilder(this, DataDB::class.java,"DATADB").allowMainThreadQueries().build()
            val isidetail = db.DetailDao().getDetail(idsum.toIntArray())
            Log.d("get db",isidetail.toString())
            if (isidetail.size > 0){
                val sinyal = mutableListOf<Int>()
                isidetail.forEach {
                    sinyal.add(it.sinyal)
                }
                Log.d("get sinyal", sinyal.toString())
                binding.txt3b1.text = "Jumlah Data : " + sinyal.size
                binding.txt3b2.text = "Rata-Rata   : " + round(sinyal.average()*1000)/1000 + " dBm"
                binding.txt3b3.text = "Minimal     : " + sinyal.min() + " dBm"
                binding.txt3b4.text = "Maksimal    : " + sinyal.max() + " dBm"
            }else{
                nodata()
            }
        }else   {
            nodata()
        }
    }
    fun nodata (){
        binding.txt3b1.text = "Tak Ada Data, Harap Buka Dari Lihat Data"
        binding.txt3b2.text = "Tak Ada Data, Harap Buka Dari Lihat Data"
        binding.txt3b3.text = "Tak Ada Data, Harap Buka Dari Lihat Data"
        binding.txt3b4.text = "Tak Ada Data, Harap Buka Dari Lihat Data"
    }
    override fun onResume() {
        super.onResume()
        Log.v("Hitung","onresume hitung")



    }

    override fun onPause() {
        super.onPause()
        Log.v("Hitung","onpause Hitung")
    }

    override fun onStop() {
        super.onStop()
        Log.v("Hitung","onstop Hitung")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("Hitung","ondestroy Hitung")
    }
}