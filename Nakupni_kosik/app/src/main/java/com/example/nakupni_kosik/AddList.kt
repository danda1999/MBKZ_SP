package com.example.nakupni_kosik

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddList : AppCompatActivity() {

    private lateinit var SelectGroupe: Spinner
    private lateinit var Message: EditText
    private lateinit var SendBTN: Button
    private lateinit var BackBTN: Button

    private lateinit var SQLiteHelper: SQLiteHelper
    private lateinit var Groups: ArrayList<String>
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var users_text : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_list)

        initView()

        SQLiteHelper = SQLiteHelper(this)

        BackBTN.setOnClickListener { finish() }

        Groups = SQLiteHelper.getAllGroupes()

        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Groups)
        SelectGroupe.adapter = adapter

        SelectGroupe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                users_text = SQLiteHelper.SelectGroupe(Groups[p2]).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        SendBTN.setOnClickListener { SendMessage(users_text = users_text) }
    }

    private fun initView() {
        SelectGroupe = findViewById(R.id.selectGrupe)
        Message = findViewById(R.id.Message)
        SendBTN = findViewById(R.id.SendBTN)
        BackBTN = findViewById(R.id.BackBTN)
        users_text = ""
    }

    private fun SendMessage(users_text: String) {

        val i = intent
        val name_text = i.getStringExtra("NAME")
        if(Message.text.isEmpty()) {
            Toast.makeText(this, "Zpráva je prázdná", Toast.LENGTH_LONG).show()
        } else {

            val users = users_text.split(";")
            for (i in users){
                val parse_user = i.split(" ")
                val name = parse_user[0]
                val lastname = parse_user[1]
                if(name_text.equals("$name $lastname"))
                {
                    Toast.makeText(this, "To jsem já!!", Toast.LENGTH_LONG).show()
                    continue
                }else {
                    val a = SQLiteHelper.SelectNumber(Name = name, LastName = lastname)
                    val sms: SmsManager = SmsManager.getDefault()
                    sms.sendTextMessage(a, null, Message.text.toString(), null, null)

                    Toast.makeText(this, "Message Sent successfully!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}