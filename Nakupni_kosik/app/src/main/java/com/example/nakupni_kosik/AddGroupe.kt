package com.example.nakupni_kosik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class AddGroupe : AppCompatActivity() {

    private lateinit var Name_Grupe: EditText
    private lateinit var addUsers: Spinner
    private lateinit var saveGroupe: Button
    private lateinit var addUser: Button
    private lateinit var deleteUser: Button
    private lateinit var UserList: TextView
    private lateinit var adapter : ArrayAdapter<String>

    private lateinit var SQLiteHelper:SQLiteHelper
    private lateinit var select: String
    private lateinit var usersList: String
    private lateinit var AddArray: ArrayList<String>
    private lateinit var Users: ArrayList<String>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_groupe)
        initView()

        SQLiteHelper = SQLiteHelper(this)

        saveGroupe.setOnClickListener { addGroupe() }



        Users = SQLiteHelper.getAllUsers()

        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Users)
        addUsers.adapter = adapter

        addUsers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                select = Users[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@AddGroupe, "Nebylo nic zvoleno.", Toast.LENGTH_LONG).show()
            }

        }

        addUser.setOnClickListener {
            val i = AddArray.indexOf(select)
            if(i == -1) {
                AddArray.add(select)
                usersList = ""
                UserList.text = ""
                for (i in AddArray) {
                    usersList += "$i;"
                }

                UserList.text = usersList

            } else {
                Toast.makeText(this, "Uživatl se již nachází v seznamu.", Toast.LENGTH_LONG).show()
            }


        }

        deleteUser.setOnClickListener {
            if(AddArray.isEmpty()) {
                Toast.makeText(this@AddGroupe, "Nelze odebrat uživatele když je seznam prázdný.", Toast.LENGTH_LONG).show()
            }else {
                val i = AddArray.indexOf(select)
                if(i == -1) {
                    Toast.makeText(this@AddGroupe, "Nelze odebrat uživatele který v seznamu neni.", Toast.LENGTH_LONG).show()
                } else {
                    AddArray.remove(select)
                    usersList = usersList.replace("$select;", "")
                    UserList.text = usersList
                }
            }
        }

    }

    private fun initView() {
        saveGroupe = findViewById(R.id.SaveGroupe)
        Name_Grupe = findViewById(R.id.NameGrupe)
        addUsers = findViewById(R.id.selectGrupe)
        addUser = findViewById(R.id.AddUser)
        deleteUser = findViewById(R.id.DeleteUser)
        UserList = findViewById(R.id.UsersList)
        AddArray = ArrayList()
        usersList = ""
    }

    private fun addGroupe() {

        val nameGrupe = Name_Grupe.text.toString()

        if(nameGrupe.isEmpty() || usersList.isEmpty()) {
            Toast.makeText(this, "Je potřeba uvést všechny potřebné informace o tvorbě skupiny", Toast.LENGTH_LONG)
        } else {

            val users = usersList.dropLast(1)

            val groupe: GroupeModel = GroupeModel(name = nameGrupe, users = users)

            val status = SQLiteHelper.InsertGroupe(groupe = groupe)

            if(status > -1) {
                Toast.makeText(this, "Skupina byla zaregistrována.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Nastala chyba skupina nebyla zaregistrována.", Toast.LENGTH_LONG).show()
                Name_Grupe.setText("")
            }
        }

    }

}