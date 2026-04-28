package com.example.a6new

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a6new.ui.theme._6newTheme

class MainActivity : ComponentActivity() {


    private val vm: PersonaViewModel by viewModels{
        viewModelFactory {
            initializer {
                val db = BaseDeDatos.BaseDeDatos.getDB(this@MainActivity)
                PersonaViewModel(db.dao())
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaPrincipal(vm = vm)
        }
    }
}





@Composable
fun PantallaPrincipal(vm: PersonaViewModel){
    val lista by vm.lista.collectAsState(initial = emptyList())
    var txt_nombre by remember { mutableStateOf("") }
    var txt_apellido by remember {mutableStateOf("")}

    Column(Modifier.padding(16.dp)) {

        OutlinedTextField(
            txt_nombre,
            onValueChange = { txt_nombre = it },
            label = { Text("Nombre") }
        )

        OutlinedTextField(
            value = txt_apellido,
            onValueChange = { txt_apellido = it },
            label = { Text("Apellido") }
        )

        Button(
            onClick = {
                vm.insertar(txt_nombre, txt_apellido)
                txt_nombre = ""
                txt_apellido = ""
            }
        ) {
            Text("Guardar")
        }

        Spacer(Modifier.height(10.dp))

        LazyColumn() {
            items(items = lista) { p -> ItemPersona(p,vm)
            }
        }

    }
}


@Composable
fun ItemPersona(p: Persona, vm: PersonaViewModel){
    var showDialog by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf(p.nombre) }
    var apellido by remember { mutableStateOf(p.apellido) }

    Column(Modifier.padding(8.dp)){
        if(showDialog){
            AlertDialog(
                onDismissRequest = {showDialog = false},
                confirmButton = {
                    Button(
                        onClick = {
                            vm.actualizar(p.copy(nombre= nombre , apellido = apellido))
                            showDialog = false
                        }
                    ) { Text("Guardar")}
                },
                dismissButton = {
                    Button(onClick = {showDialog = false}) {
                        Text("Cancelar")
                    }
                },
                title = {Text("Editar persona")},
                text = {
                    Column() {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = {nombre = it},
                            label = {Text("Nombre")}
                        )
                        OutlinedTextField(
                            value = apellido,
                            onValueChange = {apellido = it},
                            label = {Text("Apellido")}
                        )
                    }
                }

            )
        }

        Row(modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text("${p.nombre} ${p.apellido}", modifier = Modifier.weight(1f))

            IconButton(onClick = {showDialog = true}) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
            }

            IconButton(onClick = {vm.eliminar(p)}) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
            }

        }
    }
}
@Preview
@Composable
fun pantallaPrincipal(){
    Column(modifier = Modifier.fillMaxSize().background(color = Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.logo)
            , contentDescription = "Logo",
            modifier = Modifier.size(width = 400.dp, height = 400.dp)
        )

        Row() {
            Button(
              onClick =  {}
            ) {
                Text(text = "Agregar libros")
            }
            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = {}
            ) {
                Text(text = "Listar libros")
            }
            Spacer(modifier = Modifier.width(20.dp))



        }


        Button(
            onClick = {},

        ) {
            //Icon(imageVector = Icons.Default.Close, Color.White)
            Text(text = "Salir")
        }


    }

}