package com.example.a6new

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface LibroDao {
    @Insert suspend fun insertar(libro: Libro)
    @Update suspend fun  actualizar(libro: Libro)
    @Delete suspend fun  eliminar(libro: Libro)
    @Query (value = "Select * from libros")
    fun ObtenerTodos(): Flow<List<Libro>>

}