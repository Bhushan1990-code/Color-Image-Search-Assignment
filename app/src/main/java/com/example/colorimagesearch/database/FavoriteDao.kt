package com.example.colorimagesearch.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavoriteDao {
    @Insert
    fun addData(favoriteList: FavoriteList?)

    @get:Query("select * from favoritelist")
    val favoriteData: List<FavoriteList?>?

    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE id=:id)")
    fun isFavorite(id: Int): Int

    @Delete
    fun delete(favoriteList: FavoriteList?)
}