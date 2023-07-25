package com.example.ssdt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.ssdt.data.DataDB
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ssdt.databinding.LihatMapBinding
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory



class LihatMap : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: LihatMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LihatMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.)
        val idsum = getIntent().getIntegerArrayListExtra("key")
        if (idsum!!.size > 0) {
            val db =
                Room.databaseBuilder(this, DataDB::class.java, "DATADB").allowMainThreadQueries()
                    .build()
            val biru = getBitmapDescriptorFromVector(this,R.drawable.blue_circle)
            val hijau = getBitmapDescriptorFromVector(this,R.drawable.green_circle)
            val kuning = getBitmapDescriptorFromVector(this,R.drawable.yellow_circle)
            val merah = getBitmapDescriptorFromVector(this,R.drawable.red_circle)
            val hitam = getBitmapDescriptorFromVector(this,R.drawable.black_circle)
            val isidetail = db.DetailDao().getDetail(idsum.toIntArray())
            if (isidetail.size > 0) {
                isidetail.forEach {
                    var ico1 : BitmapDescriptor?
                    when {
                        it.sinyal > -80 -> ico1 = biru
                        it.sinyal > -90 -> ico1 = hijau
                        it.sinyal > -100 -> ico1 = kuning
                        it.sinyal > -120 -> ico1 = merah
                        else -> ico1 = hitam
                    }
                    mMap.addMarker(MarkerOptions().position(LatLng(it.lat,it.lon)).icon(ico1))
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(isidetail[0].lat,isidetail[0].lon),16.5F))

            }
        }
    }
    fun getBitmapDescriptorFromVector(context: Context, @DrawableRes vectorDrawableResourceId: Int): BitmapDescriptor? {

        val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId)
        val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}
