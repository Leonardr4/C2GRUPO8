package com.example.a6new

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update


@Dao
interface LibroDao {
    @Insert suspend fun insertar(libro: Libro)
    @Update suspend fun  actualizar(libro: Libro)
    @Delete suspend fun  actualizar(libro: Libro)
}