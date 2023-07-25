package com.example.ssdt

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Bundle
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellSignalStrengthGsm
import android.telephony.CellSignalStrengthLte
import android.telephony.CellSignalStrengthNr
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.ssdt.data.DataDB
import com.example.ssdt.data.DataDetail
import com.example.ssdt.data.DataSum
import com.example.ssdt.data.TmpDataDetail
import com.example.ssdt.databinding.AmbilDataBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AmbilData: AppCompatActivity()  {
    private lateinit var binding: AmbilDataBinding
    var dD = mutableListOf<TmpDataDetail>()
    var btnstate = false
    var sinyal = mutableListOf<Int>()
    var datarekam = mutableListOf<Int>(0,0,0,0,0,0,0,0,0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AmbilDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Room.databaseBuilder(this, DataDB::class.java,"DATADB").allowMainThreadQueries().build()
        val lm : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val ll: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            if(isLocationEnabled()){
                lm.requestLocationUpdates(GPS_PROVIDER,500,0F,ll)
            }
        Log.v("AmbilData","oncreate ambil data")

        tugasberulang()
        binding.btn2a1.setOnClickListener {
            btnstate =! btnstate
            if (btnstate){
                Log.v("AmbilData", "Mejalankan tugas")
                binding.btn2a1.text = "Berhenti"
                //binding.txt2a1.text = "Mengambil Data"
            } else{
                Log.v("AmbilData", "Menghentikan tugas")
                binding.btn2a1.isEnabled = false
                binding.txt2a1.text = "Memproses Data"
                db.SumDao().inSum(DataSum(0,"Data",sinyal.min(),sinyal.max(),sinyal.average()))
                val liatsum = db.SumDao().getAllSum().last().dsid
                dD.forEach{
                    sinyal.add(it.sinyal)
                    db.DetailDao().inDetail(DataDetail(0,liatsum, it.waktu,it.siop,it.jar,it.sinyal,it.lat,it.lon))
                }
                dD.clear()
                sinyal.clear()
                binding.btn2a1.text = "Mulai"
                binding.btn2a1.isEnabled = true
                binding.txt2a1.text = "Siap"
                datarekam = mutableListOf<Int>(0,0,0,0,0,0,0,0,0)
            }

        }
    }

    private fun isLocationEnabled(): Boolean {
        val lm : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun dataAmbil(): TmpDataDetail {
        if(
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){if (isLocationEnabled()){
            val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val lm : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val locend = lm.getLastKnownLocation(GPS_PROVIDER)
            val d5 = locend?.latitude
            val d6 = locend?.longitude
            //locend?.accuracy

            //Log.d("Ambil Data", d5.toString()+"|||"+ d6.toString() +"|||"+ acu + "|||" + txt1)
            Log.d("Ambil Data", "hasil gps"+ lm.getLastKnownLocation(GPS_PROVIDER).toString())
            //if (acu<10F){
                if (d5 == null || d6 == null){
                    datarekam[5]++
                    return TmpDataDetail("","","",0,0.0,0.0)
                }
                val d1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                val d2 = tm.networkOperatorName
                var d3 : String

                var d4 :Int
                val infosel = tm.allCellInfo[0].cellSignalStrength
                when (infosel){
                    is CellSignalStrengthGsm -> {
                        d3 = "2G"
                        d4 = infosel.dbm
                        sinyal.add(infosel.dbm)
                    }
                    is CellSignalStrengthLte ->{
                        d3 = "4G"
                        d4 = infosel.dbm
                        sinyal.add(infosel.dbm)
                    }
                    is CellSignalStrengthNr ->{
                        d3 = "5G"
                        d4 = infosel.dbm
                        sinyal.add(infosel.dbm)
                    }
                    else ->{
                        datarekam[6]++
                        return TmpDataDetail("","","",0,0.0,0.0)
                    }
                }
                val hasil = TmpDataDetail(d1,d2,d3,d4,d5,d6)
                datarekam[1]++
                return  hasil
            //}

        }else{
            datarekam[4]++
        }
        }else{
            binding.txt2a1.text = getString(R.string.txt2a1_1)
            datarekam[3]++
        }
        return TmpDataDetail("","","",0,0.0,0.0)
    }


    fun tugasberulang(){
        lifecycleScope.launch{
            val datnul = TmpDataDetail("","","",0,0.0,0.0)
            var datlama = datnul
            val startPoint = Location("locationA")

            val endPoint = Location("locationA")
            endPoint.latitude = -6.0
            endPoint.longitude = 107.0
            while (true){
                delay(500)
                if (btnstate){
                    datarekam[0]++
                    val datbaru = dataAmbil()
                    startPoint.latitude = datbaru.lat
                    startPoint.longitude = datbaru.lon
                    val jarak = startPoint.distanceTo(endPoint)
                    if (datbaru != datnul ){
                        //if (jarak >= 0.1F){
                            datarekam[2]++
                            dD.add(datbaru)
                            datlama = datbaru
                            endPoint.latitude = datlama.lat
                            endPoint.longitude = datlama.lon

                            //binding.txt2a1.text = dD.toString()
                            Log.d("AmbilData", "Tugas Ambil Data")
                        //}

                    }else {
                        datarekam[7]++
                    }
                    //binding.txt2a1.text = "data lama :" + datlama.toString() + "\n\ndata baru :" + datbaru.toString() +"\n\nisi list :" + dD.toString()
                    binding.txt2a1.text = "MengambilData\nMengulang : " + datarekam[0] +
                            "\nTerambil: " + datarekam[1] +
                            "\nMasuk Db : " + datarekam[2] +
                            "\nmasalah Izin : " + datarekam[3] +
                            "\nmasalah gps aktif : " + datarekam[4] +
                            "\nmasalah gps null : " + datarekam[5] +
                            "\nmasalah signal type : " + datarekam[6] +
                            "\ndata sama persis : " + datarekam[7]

                }
                Log.v("AmbilData", "Tugas Bekerja")

            }

        }
    }


}