package com.nakyoung.androidclientdevelopment.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nakyoung.androidclientdevelopment.db.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insert(vararg users: UserEntity)

    @Update
    suspend fun update(vararg users: UserEntity)

    @Delete
    suspend fun delete(vararg userEntity: UserEntity)

    @Query("SELECT * FROM user WHERE id=:uid")
    suspend fun get(uid: String): UserEntity?
}