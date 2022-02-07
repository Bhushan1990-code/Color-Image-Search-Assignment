package com.example.colorimagesearch.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritelist")
data class FavoriteList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "colorsId")
    var colorsId: String = "",

    @ColumnInfo(name = "likeunlike")
    var likeunlike: String = ""

)