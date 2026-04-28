package com.example.a6new

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonaDao {
    @Insert
    suspend fun insertar(persona: Persona)

    @Update
    suspend fun actualizar(persona: Persona)

    @Delete
    suspend fun eliminar(persona: Persona)

    @Query(value = "Select * from personas")
    fun obtenerTodos(): Flow<List<Persona>>
}