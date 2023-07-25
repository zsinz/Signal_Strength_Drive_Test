package com.example.ssdt

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.androidplot.xy.LineAndPointFormatter
import com.androidplot.xy.PanZoom
import com.androidplot.xy.SimpleXYSeries
import com.androidplot.xy.XYGraphWidget
import com.androidplot.xy.XYSeries
import com.example.ssdt.data.DataDB
import com.example.ssdt.databinding.LihatGrafikBinding
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LihatGrafik: AppCompatActivity()  {
    private lateinit var binding:LihatGrafikBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("grafik","oncreate grafik")
        binding = LihatGrafikBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val idsum = getIntent().getIntegerArrayListExtra("key")
        if (idsum!!.size > 0) {
            val db =
                Room.databaseBuilder(this, DataDB::class.java, "DATADB").allowMainThreadQueries()
                    .build()
            val isidetail = db.DetailDao().getDetail(idsum.toIntArray())
            if (isidetail.size > 0) {
                val sinyal = mutableListOf<Int>()
                val waktu = mutableListOf<LocalDateTime>()
                isidetail.forEach {
                    sinyal.add(it.sinyal)
                    waktu.add(LocalDateTime.parse(it.waktu, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                }
                val seri1: XYSeries = SimpleXYSeries(sinyal, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"Kuat Sinyal")
                val seri1f = LineAndPointFormatter(Color.BLUE,Color.BLACK,null,null)
                binding.plot.addSeries(seri1,seri1f)
                binding.plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format(){
                    override fun format(
                        obj: Any?,
                        toAppendTo: StringBuffer,
                        pos: FieldPosition?
                    ): StringBuffer {
                        val i = Math.round((obj as Number).toFloat())
                        return toAppendTo.append(waktu[i])
                    }

                    override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                        return null
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.v("grafik","onpause grafik")
    }


    override fun onPause() {
        super.onPause()
        Log.v("grafik","onpause grafik")
    }

    override fun onStop() {
        super.onStop()
        Log.v("grafik","onstop grafik")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("grafik","ondestroy grafik")
    }
}