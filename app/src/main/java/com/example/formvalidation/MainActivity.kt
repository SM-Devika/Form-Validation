package com.example.formvalidation  // ✅ Use your real package name

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Register : Screen("register")
    object Login : Screen("login")
    object Home : Screen("home")
}

class UserViewModel : ViewModel() {
    var registeredEmail by mutableStateOf("")
    var registeredPassword by mutableStateOf("")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val viewModel: UserViewModel = viewModel()

                NavHost(navController = navController, startDestination = Screen.Register.route) {
                    composable(Screen.Register.route) {
                        RegisterScreen(navController, viewModel)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(navController, viewModel)
                    }
                    composable(Screen.Home.route) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController, viewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        OutlinedTextField(value = contact, onValueChange = { contact = it }, label = { Text("Contact") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (name.isNotBlank() && contact.isNotBlank() && email.contains("@") && password.isNotBlank()) {
                viewModel.registeredEmail = email
                viewModel.registeredPassword = password
                showDialog = true
            }
        }) {
            Text("SUBMIT")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Form Submitted") },
                text = {
                    Text("Hello $name!\nEmail: $email\nContact: $contact")
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        navController.navigate(Screen.Login.route)
                    }) {
                        Text("Go to Login")
                    }
                }
            )
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController, viewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (email == viewModel.registeredEmail && password == viewModel.registeredPassword) {
                navController.navigate(Screen.Home.route)
            } else {
                errorMessage = "Invalid credentials. Try again."
            }
        }) {
            Text("LOGIN")
        }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("WELCOME TO THE HOME SCREEN!", style = MaterialTheme.typography.headlineMedium)
    }
}
