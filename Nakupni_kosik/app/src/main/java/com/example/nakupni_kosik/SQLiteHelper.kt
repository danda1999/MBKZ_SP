package com.example.nakupni_kosik

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.wifi.p2p.WifiP2pManager.UpnpServiceResponseListener
import java.lang.Exception

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VESION) {

    companion object {
        private const val DATABASE_NAME = "List.db"
        private const val DATABASE_VESION = 1

        //Tables
        private const val TABLE_1 = "Users_tbl"
        private const val TABLE_2 = "Groups_tbl"
        private const val TABLE_3 = "List_tbl"

        //General Colum
        private const val ID = "ID"
        private const val NAME = "NAME"

        //Users_tbl
        private const val LASTNAME = "LASTNAME"
        private const val EMAIL = "EMAIL"
        private const val PHONE = "PHONE"
        private const val PASSWOD = "PASSWORD"

        //Groups_tbl
        private const val USERS = "USERS"

        //List_tbl
        private const val GROUP_ID = "GROUP_ID"
        private const val TEXT = "TEXT"
    }
    override fun onCreate(db: SQLiteDatabase?) {

        val CreateUsersTable = ("CREATE TABLE $TABLE_1 ($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME TEXT, $LASTNAME TEXT, $EMAIL TEXT UNIQUE, $PHONE TEXT UNIQUE, $PASSWOD TEXT)")

        db?.execSQL(CreateUsersTable)

        val CreateGroupsTable = ("CREATE TABLE $TABLE_2 ($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME TEXT UNIQUE, $USERS TEXT)")

        db?.execSQL(CreateGroupsTable)

        val CreateListTable = ("CREATE TABLE $TABLE_3 ($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$GROUP_ID INTEGER not null, $TEXT TEXT, FOREIGN KEY ($GROUP_ID) REFERENCES $TABLE_2 ($ID))")

        db?.execSQL(CreateListTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_1")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_2")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_3")
        onCreate(db)

    }

    fun InsertUser(user: UserModel): Long {

        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME, user.name)
        contentValues.put(LASTNAME, user.LastName)
        contentValues.put(EMAIL, user.Email)
        contentValues.put(PHONE, user.Phone)
        contentValues.put(PASSWOD, user.Password)

        val succes = db.insert(TABLE_1, null, contentValues)
        db.close()
        return succes
    }

    fun InsertGroupe(groupe: GroupeModel) : Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(NAME, groupe.name)
        contentValues.put(USERS, groupe.users)

        val succes = db.insert(TABLE_2, null, contentValues)
        db.close()
        return succes
    }

    fun SelectLogin(user: UserModel): UserModel? {

        val login_user = ("SELECT $NAME, $LASTNAME FROM $TABLE_1 WHERE $EMAIL = '${user.Email}' AND" +
                " $PASSWOD = '${user.Password}'")

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(login_user, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(login_user)
            return null
        }

        val back_user = UserModel(name = "", LastName = "", Email = "", Phone = "", Password = "")

        if(cursor.moveToFirst()) {
            back_user.name = cursor.getString(0)
            back_user.LastName = cursor.getString(1)
        }

        cursor.close()

        return back_user
    }


    fun getAllUsers() : ArrayList<String> {

        val Users: ArrayList<String> = ArrayList()

        val query = ("SELECT $NAME, $LASTNAME FROM $TABLE_1")

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(query)
            return Users
        }

        var name = ""
        var lastname = ""

        if(cursor.moveToFirst()) {
            do {
                name = cursor.getString(0)
                lastname = cursor.getString(1)

                val allName = "$name $lastname"
                Users.add(allName)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return Users
    }

    fun getAllGroupes() : ArrayList<String> {

        val Groups: ArrayList<String> = ArrayList()

        val query = ("SELECT $NAME FROM $TABLE_2")

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(query)
            return Groups
        }

        var name = ""

        if(cursor.moveToFirst()) {
            do {
                name = cursor.getString(0)

                val allName = "$name"
                Groups.add(allName)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return Groups
    }

    fun SelectGroupe(nameGroupe: String): String? {

        val groupeUsers = ("SELECT $USERS FROM $TABLE_2 WHERE NAME = '$nameGroupe'")

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(groupeUsers, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(groupeUsers)
            return null
        }

        var back_users: String = ""

        if(cursor.moveToFirst()) {
            back_users = cursor.getString(0)
        }

        cursor.close()

        return back_users
    }

    fun SelectNumber(Name: String, LastName: String): String? {

        val query = ("SELECT $PHONE FROM $TABLE_1 WHERE $NAME = '$Name' AND $LASTNAME = '$LastName'")

        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(query)
            return null
        }

        var phone = ""

        if(cursor.moveToFirst()) {
            phone = cursor.getString(0)
        }

        cursor.close()

        return phone
    }
}