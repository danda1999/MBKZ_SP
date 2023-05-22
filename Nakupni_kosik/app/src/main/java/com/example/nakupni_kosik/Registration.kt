package com.example.nakupni_kosik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Registration : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var lastname: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var password: EditText
    private lateinit var registrationBTN: Button
    private lateinit var backBTN: Button

    private lateinit var SQLiteHelper: SQLiteHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        initView()

        SQLiteHelper = SQLiteHelper(this)

        backBTN.setOnClickListener { finish() }

        registrationBTN.setOnClickListener { addUser() }
    }



    private fun initView() {
        name = findViewById(R.id.registration_name)
        lastname = findViewById(R.id.registration_lastname)
        email = findViewById(R.id.registration_email)
        phone = findViewById(R.id.registration_phone)
        password = findViewById(R.id.registration_password)
        backBTN = findViewById(R.id.registration_backVTN)
        registrationBTN = findViewById(R.id.registration_registrationBTN)
    }

    private fun addUser() {
        val name_text = name.text.toString()
        val lastname_text = lastname.text.toString()
        val email_text = email.text.toString()
        val phone_text = phone.text.toString()
        val password_text = password.text.toString()

        if(name_text.isEmpty() || lastname_text.isEmpty() || email_text.isEmpty() || phone_text.isEmpty() || password_text.isEmpty()) {
            Toast.makeText(this, "Nejsou zadany všechny potřebné údaje.", Toast.LENGTH_LONG).show()
        } else {
            val user = UserModel(name = name_text, LastName = lastname_text, Email = email_text, Phone = phone_text, Password = password_text)
            val status = SQLiteHelper.InsertUser(user)

            if(status > -1) {
                Toast.makeText(this, "Uživatel byl zaregistrován.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Nastala chyba nebyl uživatel zaregistrován.", Toast.LENGTH_LONG).show()
                clearEditText()
            }
        }
    }

    private fun clearEditText() {
        name.setText("")
        lastname.setText("")
        email.setText("")
        phone.setText("")
        password.setText("")
    }


}