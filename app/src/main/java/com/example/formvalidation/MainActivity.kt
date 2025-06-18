// MainActivity.kt
package com.example.formvalidation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
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

                NavHost(navController = navController, startDestination = Screen.Splash.route) {
                    composable(Screen.Splash.route) {
                        SplashScreen(navController)
                    }
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
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate(Screen.Register.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome to the App", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Example", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Splash Image",
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun RegisterScreen(navController: NavController, viewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf("") }

    fun isValidPhone(number: String): Boolean {
        return number.length == 10 && number.all { it.isDigit() } &&
                number.toSet().size > 1 && !number.all { it == '0' }
    }

    LaunchedEffect(phone) {
        if (phone.isNotBlank() && !isValidPhone(phone)) {
            phoneError = "Invalid phone number"
        } else {
            phoneError = ""
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Register", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone No") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if (phoneError.isNotEmpty()) {
                Text(phoneError, color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    if (name.isNotBlank() && email.contains("@") && password.isNotBlank() && phoneError.isEmpty()) {
                        viewModel.registeredEmail = email
                        viewModel.registeredPassword = password
                        showDialog = true
                    }
                }) {
                    Text("SIGN IN")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    navController.navigate(Screen.Login.route)
                }) {
                    Text("LOGIN")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Registered Successfully") },
                    text = { Text("Hello $name!\nEmail: $email\nPhone: $phone") },
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
}

@Composable
fun LoginScreen(navController: NavHostController, viewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation()
            )

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
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("WELCOME TO THE HOME SCREEN!", style = MaterialTheme.typography.headlineMedium)
    }
}
