package com.example.nakupni_kosik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainPage : AppCompatActivity() {

    private lateinit var name: TextView
    private lateinit var grupe_add: Button
    private lateinit var grupe_edit: Button
    private lateinit var list_add: Button
    private lateinit var logoutBTN: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        initView()

        grupe_add.setOnClickListener {
            val i = Intent(this, AddGroupe::class.java )
            startActivity(i)
        }
        logoutBTN.setOnClickListener { finish() }

        list_add.setOnClickListener {
            val i = Intent(this, AddList::class.java)
            i.putExtra("NAME", name.text.toString())
            startActivity(i)
        }
    }

    private fun initView() {
        name = findViewById(R.id.Name_Main_page)
        grupe_add = findViewById(R.id.Grupe_Add)
        grupe_edit = findViewById(R.id.Grupe_edit)
        list_add = findViewById(R.id.List_add)
        logoutBTN = findViewById(R.id.logout)

        val i = intent
        val name_text = i.getStringExtra("NAME")
        name.setText(name_text)
    }
}