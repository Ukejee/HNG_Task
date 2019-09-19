package com.ukejee.hngtask.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 9/18/19
 */
@Entity
data class User(@ColumnInfo(name = "first_name")var firstName: String,
                @ColumnInfo(name = "last_name")var lastName: String,
                @ColumnInfo(name = "phone_no")var phoneNumber: String,
                @ColumnInfo(name = "email")var email: String,
                @ColumnInfo(name = "password")var password: String,
                @PrimaryKey(autoGenerate = true) val uid: Int = 0): Serializable{}