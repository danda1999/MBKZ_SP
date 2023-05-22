package com.example.nakupni_kosik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var email_ed: EditText
    private lateinit var password_ed: EditText
    private lateinit var loginBTN: Button
    private lateinit var registrationBTN: Button

    private lateinit var SQLiteHelper: SQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        SQLiteHelper = SQLiteHelper(this)

        registrationBTN.setOnClickListener {
            val i = Intent(this, Registration::class.java)
            email_ed.setText("")
            password_ed.setText("")
            startActivity(i)
        }

        loginBTN.setOnClickListener { Login_User() }


    }

    private fun initView() {
        email_ed = findViewById(R.id.login_email)
        password_ed = findViewById(R.id.login_password)
        loginBTN = findViewById(R.id.login_loginBTN)
        registrationBTN = findViewById(R.id.login_registrationBTN)
    }

    private fun Login_User() {
        val email = email_ed.text.toString()
        val password = password_ed.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nejsou zadany všechny potřebné údaje.", Toast.LENGTH_LONG).show()
        } else {
            val user = UserModel(name = "", LastName = "", Email = email, Phone = "", Password = password)

            val back_user: UserModel? = SQLiteHelper.SelectLogin(user = user)

            if(back_user == null || back_user.name.isEmpty() || back_user.LastName.isEmpty()) {
                Toast.makeText(this, "Nebyla nalezena shoda s registrovanými uživateli.", Toast.LENGTH_LONG).show()
            } else {
                email_ed.setText("")
                password_ed.setText("")
                val i = Intent(this, MainPage::class.java)
                i.putExtra("NAME", (back_user.name + " " + back_user.LastName))
                startActivity(i)
            }
        }
    }


}