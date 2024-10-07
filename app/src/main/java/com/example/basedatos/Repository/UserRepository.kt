package com.example.basedatos.Repository

import com.example.basedatos.DAO.UserDao
import com.example.basedatos.Model.User


class UserRepository (private val userDao:UserDao) {
    suspend fun insert(user: User) {
        userDao.insert(user)


    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()

    }

    suspend fun deleteById(userId: Int): Int {
        return userDao.deleteById(userId)
    }
        suspend fun delete(user: User) {
            userDao.delete(user)
        }


    suspend fun update(user: User) {
        userDao.update(user)
    }
}
