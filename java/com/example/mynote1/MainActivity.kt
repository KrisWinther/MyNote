package com.example.mynote1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val addbut: TextView = findViewById(R.id.addbut)
        addbut.setOnClickListener{
            startActivity(Intent(this@MainActivity, addreport::class.java))
        }
        val info: TextView = findViewById(R.id.info)
        info.setOnClickListener {
            startActivity(Intent(this@MainActivity, information::class.java))
        }
        val h = Handler()
        val db = Firebase.firestore
        db.addSnapshotsInSyncListener {
            updateBD()
        }
        h.postDelayed({updateBD()}, 200)
    }

    private fun updateBD() {
        val recycleview: RecyclerView = findViewById(R.id.recycleview)
        val db = Firebase.firestore
        db.collection("notes").get().addOnSuccessListener {
            val reportList = mutableListOf<CustomModel>()
            for (doc in it) {
                val report = CustomModel(
                    doc.id, doc.getString("name").toString(),
                    doc.getString("desctext").toString(),
                    doc.getTimestamp("time")!!.toDate())
                reportList.add(report)
            }
            reportList.sortBy { it.date }
            reportList.reverse()
            val reportAdapter = CustomAdapter(this@MainActivity, reportList)
            recycleview.layoutManager = LinearLayoutManager(this)
            recycleview.adapter = reportAdapter
        }.addOnFailureListener {
            Toast.makeText(this, "DataBase Error.", Toast.LENGTH_LONG).show()
        }
    }
}
