package com.example.a6new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LibroViewModel(private val dao: LibroDao): ViewModel() {

    val lista = dao.ObtenerTodos()

    fun insertar(titlulo: String,autor: String,genero: String)=viewModelScope.launch {
        dao.insertar(Libro(titulo =titlulo, autor = autor, genero = genero))
    }

    fun actualizar(p: Libro)=viewModelScope.launch { dao.actualizar(p) }

    fun eliminar(p: Libro)=viewModelScope.launch {
        dao.eliminar(p)
    }

}