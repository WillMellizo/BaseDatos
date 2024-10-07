package com.example.basedatos.Screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.basedatos.Model.User
import com.example.basedatos.Repository.UserRepository
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.basedatos.R

@Composable
fun UserApp(userRepository: UserRepository) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var showRegistrationFields by rememberSaveable { mutableStateOf(false) }
    var users by rememberSaveable { mutableStateOf(listOf<User>()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Modificador de fondo con gradiente
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD))
    )

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .background(brush = backgroundBrush), // Agregar fondo degradado
        verticalArrangement = Arrangement.Center // Centrar verticalmente
    ) {
        // Imagen centrada arriba del título
        Image(
            painter = painterResource(id = R.drawable.contacto), // Reemplaza con el nombre de tu imagen
            contentDescription = "User Registration Image",
            modifier = Modifier
                .size(100.dp) // Ajusta el tamaño según necesites (100.dp es un ejemplo)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Fit // Ajuste para que la imagen se vea completa
        )

        // Título centrado
        Text(
            text = "Registro de Usuarios",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center // Centrar el texto
        )

        // Botones centrados en un Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Centrar horizontalmente
        ) {
            Button(onClick = {
                showRegistrationFields = true  // Al hacer clic, se muestran los campos
            }) {
                Text(text = "Registrar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                scope.launch {
                    users = withContext(Dispatchers.IO) {
                        userRepository.getAllUsers()
                    }
                }
            }) {
                Text("Listar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de entrada y botón centrados
        if (showRegistrationFields) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Centrar horizontalmente
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(text = "Nombre") },
                    modifier = Modifier.fillMaxWidth(0.8f) // Ancho del campo (opcional)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text(text = "Apellido") },
                    modifier = Modifier.fillMaxWidth(0.8f) // Ancho del campo (opcional)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = edad,
                    onValueChange = { newValue ->
                        // Permitir solo números en el campo de edad
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            edad = newValue
                        }
                    },
                    label = { Text(text = "Edad") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(0.8f) // Ancho del campo (opcional)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    // Verificación de campos vacíos
                    if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty()) {
                        Toast.makeText(context, "Debe llenar todos los campos solicitados", Toast.LENGTH_LONG).show()
                    } else {
                        val user = User(
                            nombre = nombre,
                            apellido = apellido,
                            edad = edad.toIntOrNull() ?: 0
                        )
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                userRepository.insert(user)
                            }

                            Toast.makeText(
                                context,
                                "Usuario registrado: Nombre: $nombre, Apellido: $apellido, Edad: $edad",
                                Toast.LENGTH_LONG
                            ).show()

                            // Limpiar campos después del registro
                            nombre = ""
                            apellido = ""
                            edad = ""
                            showRegistrationFields = false

                            users = userRepository.getAllUsers()  // Actualiza la lista de usuarios
                        }
                    }
                }, modifier = Modifier.fillMaxWidth(0.8f)) { // Ancho del botón (opcional)
                    Text(text = "Registrar Usuario")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Mostrar la lista de usuarios en LazyColumn
        LazyColumn {
            items(users) { user ->
                UserCard(user, userRepository, context, scope) { updatedUsers ->
                    users = updatedUsers // Actualiza la lista en UserApp
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para salir de la aplicación, centrado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Centrar horizontalmente
        ) {
            Button(onClick = {
                // Cerrar la aplicación
                (context as? Activity)?.finish()
            }, modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(text = "Salir")
            }
        }
    }
}

@Composable
fun UserCard(
    user: User,
    userRepository: UserRepository,
    context: Context,
    scope: CoroutineScope,
    onUserListUpdated: (List<User>) -> Unit // Callback para actualizar la lista
) {
    var editMode by rememberSaveable { mutableStateOf(false) } // Estado para modo de edición
    var nombre by rememberSaveable { mutableStateOf(user.nombre) } // Estado para el nombre
    var apellido by rememberSaveable { mutableStateOf(user.apellido) } // Estado para el apellido
    var edad by rememberSaveable { mutableStateOf(user.edad.toString()) } // Estado para la edad

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (editMode) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = edad,
                    onValueChange = { newValue ->
                        // Permitir solo números en el campo de edad
                        if (newValue.all { it.isDigit() } || newValue.isEmpty()) {
                            edad = newValue
                        }
                    },
                    label = { Text("Edad") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    // Verificación de campos vacíos
                    if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty()) {
                        Toast.makeText(context, "Debe llenar todos los campos solicitados", Toast.LENGTH_LONG).show()
                    } else {
                        // Actualizar usuario
                        val updatedUser = User(
                            id = user.id, // Preservar el ID del usuario existente
                            nombre = nombre,
                            apellido = apellido,
                            edad = edad.toIntOrNull() ?: 0
                        )
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                userRepository.update(updatedUser) // Método de actualización
                            }
                            Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                            onUserListUpdated(userRepository.getAllUsers()) // Actualiza la lista
                            editMode = false // Salir del modo de edición
                        }
                    }
                }) {
                    Text("Guardar Cambios")
                }

                Button(onClick = {
                    editMode = false // Salir del modo de edición
                }) {
                    Text("Cancelar")
                }
            } else {
                // Mostrar datos del usuario
                Text("Nombre: $nombre", style = MaterialTheme.typography.bodyLarge)
                Text("Apellido: $apellido", style = MaterialTheme.typography.bodyLarge)
                Text("Edad: $edad", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Button(onClick = { editMode = true }) {
                        Text("Editar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                userRepository.delete(user) // Método de eliminación
                            }
                            Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                            onUserListUpdated(userRepository.getAllUsers()) // Actualiza la lista
                        }
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
