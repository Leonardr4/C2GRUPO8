package com.example.a6new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PersonaViewModel(private val dao: PersonaDao): ViewModel() {

    val lista = dao.obtenerTodos()

    fun insertar(nombre: String,apellido: String)=viewModelScope.launch {
        dao.insertar(Persona(nombre = nombre, apellido = apellido))
    }

    fun actualizar(p: Persona)=viewModelScope.launch { dao.actualizar(p) }

    fun eliminar(p: Persona)=viewModelScope.launch {
        dao.eliminar(p)
    }

}