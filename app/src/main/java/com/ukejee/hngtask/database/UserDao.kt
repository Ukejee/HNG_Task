package com.ukejee.hngtask.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 9/18/19
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE email =:email")
    fun findByEmail(email: String): Single<User>

    @Insert
    fun insertAll(vararg users: User): Completable

    @Query("SELECT password FROM user WHERE email =:email")
    fun getPasswordByEmail(email: String): Single<String>

}