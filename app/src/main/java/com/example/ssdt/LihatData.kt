package com.example.ssdt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.room.Room
import com.example.ssdt.adapter.AdapterSum
import com.example.ssdt.data.DataDB
import com.example.ssdt.data.DataSum
import com.example.ssdt.databinding.LihatSumBinding
import java.io.File


class LihatData: AppCompatActivity()  {
    private lateinit var binding: LihatSumBinding
    private var datsum = mutableListOf<DataSum>()
    private var dapsum = mutableListOf<DataSum>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LihatSumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.v("LihatData","oncreate lihat data")
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menua,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if(id== R.id.menren){
            if (dapsum.size > 0) {
                val ceklisid = getCheckedData()
                if (ceklisid.size == 1){
                    renamebox(ceklisid)
                } else {
                    Toast.makeText(this, "Hanya Pilih Satu", Toast.LENGTH_SHORT).show()
                }
            } else{
                Toast.makeText(this, "Tolong Ceklist Salah Satu", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        if(id== R.id.menhit){
            if (dapsum.size > 0) {
                val ceklisid = getCheckedData()
                val inten1 = Intent(this,Hitung::class.java)
                inten1.putExtra("key",ceklisid)
                startActivity(inten1)
            } else{
                Toast.makeText(this, "Tolong Ceklist Salah Satu", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        if(id== R.id.menlih){
            if (dapsum.size > 0) {
                val ceklisid = getCheckedData()
                val inten2 = Intent(this,LihatGrafik::class.java)
                inten2.putExtra("key",ceklisid)
                startActivity(inten2)
            } else{
                Toast.makeText(this, "Tolong Ceklist Salah Satu", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        if(id== R.id.menmap){
            if (dapsum.size > 0) {
                val ceklisid = getCheckedData()
                val inten3 = Intent(this,LihatMap::class.java)
                inten3.putExtra("key",ceklisid)
                startActivity(inten3)
            } else{
                Toast.makeText(this, "Tolong Ceklist Salah Satu", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        if(id== R.id.menshare){
            if (dapsum.size > 0) {
                val ceklisid = getCheckedData()
                bagikan(ceklisid)
            } else{
                Toast.makeText(this, "Tolong Ceklist Salah Satu", Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onStart() {
        super.onStart()
        Log.v("LihatData","onstart lihat data")
        val db = Room.databaseBuilder(this, DataDB::class.java,"DATADB").allowMainThreadQueries().build()
        datsum = db.SumDao().getAllSum() as MutableList<DataSum>
        val isiadapter = AdapterSum(this,datsum){
            dapsum = it as MutableList<DataSum>
        }
        binding.recyclesum.adapter = isiadapter
        binding.recyclesum.setHasFixedSize(true)

    }


    private fun getCheckedData():ArrayList<Int>{
        val sumchecked = ArrayList<Int>()
        dapsum.forEach {
            sumchecked.add(it.dsid)
        }
        return sumchecked
    }

    private fun renamebox(id:ArrayList<Int>){
        val poprename =AlertDialog.Builder(this)
        val renameview = layoutInflater.inflate(R.layout.rename,null)
        val btn3a11:Button = renameview.findViewById(R.id.btn3a11)
        val btn3a12:Button = renameview.findViewById(R.id.btn3a12)
        val txt3a11:TextView = renameview.findViewById(R.id.txt3a11)
        poprename.setView(renameview)
        poprename.setCancelable(false)
        val poprename2 = poprename.show()
        btn3a11.setOnClickListener {
            poprename2.dismiss()
        }
        btn3a12.setOnClickListener {
            val db = Room.databaseBuilder(this, DataDB::class.java,"DATADB").allowMainThreadQueries().build()
            db.SumDao().rename(id,txt3a11.text.toString())
            poprename2.dismiss()
        }

    }
    private fun bagikan(id: ArrayList<Int>){
        val db =
            Room.databaseBuilder(this, DataDB::class.java, "DATADB").allowMainThreadQueries()
                .build()
        val isidetail = db.DetailDao().getDetail(id.toIntArray())
        val csv = csvOf(
            listOf("TanggalWaktu","OperatorName","NetworkType","Kuat Sinyal","Latitude","Logintude"),
            isidetail
        ){
            listOf(it.waktu,it.siop,it.jar,it.sinyal.toString(),it.lat.toString(),it.lon.toString())
        }
        val path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val filename = "export.csv"
        var fileout = File(path,filename)
        fileout.delete()
        fileout.createNewFile()
        fileout.appendText(csv)
        Log.d("lokasi file",fileout.path)
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", fileout))
        sendIntent.type = "text/csv"
        startActivity(Intent.createChooser(sendIntent,"SHARE"))
    }
    fun <T> csvOf(
        headers: List<String>,
        data: List<T>,
        itemBuilder:(T)->List<String>
    ) = buildString{
        append(headers.joinToString(","){"\"$it\""})
        append("\n")
        data.forEach{item->
            append(itemBuilder(item).joinToString(","){"\"$it\""})
            append("\n")
        }
    }
    override fun onResume() {
        super.onResume()
        Log.v("LihatData","onresume lihat data")
    }

    override fun onPause() {
        super.onPause()
        Log.v("LihatData","onpause lihat data")
    }

    override fun onStop() {
        super.onStop()
        Log.v("LihatData","onstop lihat data")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("LihatData","ondestroy lihat data")
    }
}