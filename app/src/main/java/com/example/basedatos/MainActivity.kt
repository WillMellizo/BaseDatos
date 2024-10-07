package com.example.basedatos
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.basedatos.DAO.UserDao
import com.example.basedatos.DataBase.UserDatabase
import com.example.basedatos.Repository.UserRepository
import com.example.basedatos.Screen.UserApp


class MainActivity : ComponentActivity() {
    private lateinit var userDao: UserDao
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = UserDatabase.getDatabase(applicationContext)
        userDao = db.UserDao()
        userRepository = UserRepository(userDao)

        enableEdgeToEdge()
        setContent {
            UserApp(userRepository)
        }
    }
}

