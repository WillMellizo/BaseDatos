package com.example.basedatos.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val edad: Int


)
