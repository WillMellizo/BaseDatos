package com.example.basedatos.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.basedatos.Model.User
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (user: User)

    @Query("SELECT * FROM users")

    suspend fun getAllUsers(): List<User>

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteById (userId: Int): Int

    @Delete
    suspend fun delete(user: User)

    // Método para actualizar el usuario
    @Update
    suspend fun update(user: User)

}