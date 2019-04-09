package com.yetland.dtbinderdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import me.drakeet.multitype.MultiTypeAdapter

class MainActivity : AppCompatActivity() {

    private val multiTypeAdapter = MultiTypeAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binder1 = Binder1()
        val binder2 = Binder2()
        multiTypeAdapter.register(Bean1::class.java, binder1)
        multiTypeAdapter.register(Bean2::class.java, binder2)
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = multiTypeAdapter
        }
        multiTypeAdapter.items = listOf(
            Bean1("第一层"),
            Bean1("第一层"),
            Bean2()
        )

        binder1.getOnItemClick().addObserver(object : Observer1 {
            override fun onClick1() {
                Toast.makeText(this@MainActivity, "binder1 onClick1", Toast.LENGTH_SHORT).show()
            }
        })
        binder2.getOnItemClick().addObserver(object : Observer1 {
            override fun onClick1() {
                Toast.makeText(this@MainActivity, "binder2 onClick1", Toast.LENGTH_SHORT).show()
            }
        })
        binder2.getOnItemClick().addObserver(object : Observer2 {
            override fun onClick2() {
                Toast.makeText(this@MainActivity, "binder2 onClick2", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
