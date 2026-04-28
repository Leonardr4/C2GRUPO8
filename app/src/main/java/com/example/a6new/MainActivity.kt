package com.example.a6new

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {

    private val vmLibro: LibroViewModel by viewModels {
        viewModelFactory {
            initializer {
                val db = BaseDeDatosLibros.BasedeDatosLibros.getDB(this@MainActivity)
                LibroViewModel(db.dao())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var pantallaActual by remember { mutableStateOf("inicio") }

            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                if (pantallaActual != "inicio") {
                    Button(
                        onClick = { pantallaActual = "inicio" },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Volver")
                    }
                }

                when (pantallaActual) {
                    "inicio" -> PantallaPrincipal(
                        onAgregar = { pantallaActual = "agregar" },
                        onListar = { pantallaActual = "listar" }
                    )
                    "agregar" -> PantallaLibros(vmLibro)
                    "listar" -> PantallaListaLibros(vmLibro)
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal(onAgregar: () -> Unit, onListar: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Logo",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row {
            Button(onClick = onAgregar) {
                Text(text = "Agregar libros")
            }
            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = onListar) {
                Text(text = "Listar libros")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { (context as? Activity)?.finish() }) {
            Text(text = "Salir")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLibros(vm: LibroViewModel) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    val generos = listOf("Ficción", "Ciencia", "Historia", "Fantasía", "Romance", "Terror")
    var expandido by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp)) {
        Text("Nuevo Libro", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = autor, onValueChange = { autor = it }, label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())

        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = genero,
                onValueChange = {},
                readOnly = true,
                label = { Text("Género") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                generos.forEach { opcion ->
                    DropdownMenuItem(text = { Text(opcion) }, onClick = { genero = opcion; expandido = false })
                }
            }
        }

        if (mostrarError) {
            Text("Completa todos los campos", color = Color.Red, fontSize = 12.sp)
        }

        Button(
            onClick = {
                if (titulo.isNotBlank() && autor.isNotBlank() && genero.isNotBlank()) {
                    vm.insertar(titulo, autor, genero)
                    titulo = ""; autor = ""; genero = ""; mostrarError = false
                } else {
                    mostrarError = true
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Guardar Libro")
        }
    }
}

@Composable
fun PantallaListaLibros(vm: LibroViewModel) {
    val lista by vm.lista.collectAsState(initial = emptyList())

    Column(Modifier.padding(16.dp)) {
        Text("Libros Registrados", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(lista) { libro ->
                ItemLibro(libro, vm)
            }
        }
    }
}

@Composable
fun ItemLibro(libro: Libro, vm: LibroViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(libro.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Autor: ${libro.autor}", fontSize = 14.sp, color = Color.DarkGray)
                Text("Género: ${libro.genero}", fontSize = 13.sp, color = Color.Gray)
            }
            IconButton(onClick = { vm.eliminar(libro) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}
