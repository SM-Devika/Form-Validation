package com.example.formvalidation

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.withStyle
import android.os.Build
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    object Permission : Screen("permission")
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
                        HomeScreen(navController)
                    }
                    composable(Screen.Permission.route) {
                        PermissionScreen(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Screen.Permission.route) {
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register Here",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "Welcome! Fill the required field\uD83D\uDE03",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                placeholder = { Text("John Doe") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                placeholder = { Text("hello@example.com") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("********") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "By continuing, you agree to our terms of service.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && email.contains("@") && password.isNotBlank()) {
                        viewModel.registeredEmail = email
                        viewModel.registeredPassword = password
                        showDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Sign up", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("or", style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { /* TODO: Google Sign-In */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = Color(0xFF1E88E5), fontWeight = FontWeight.SemiBold)) {
                        append("Sign in here")
                    }
                },
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Registered Successfully") },
                text = { Text("Welcome $name!\nEmail: $email") },
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
    var passwordVisible by remember { mutableStateOf(false) }
    var keepSignedIn by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "Welcome back to the app",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email Address") },
                placeholder = { Text("hello@example.com") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Password", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Forgot Password?",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF3B82F6)),
                    modifier = Modifier.clickable { /* TODO */ }
                )
            }


            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("••••••••") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = keepSignedIn,
                    onCheckedChange = { keepSignedIn = it }
                )
                Text("Keep me signed in", style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = {
                    if (email == viewModel.registeredEmail && password == viewModel.registeredPassword) {
                        navController.navigate(Screen.Home.route)
                    } else {
                        errorMessage = "Invalid credentials. Try again."
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Login", color = Color.White)
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text("or", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { /* TODO: Google Sign-In */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF1F1F1))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google",
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Create an account",
                color = Color(0xFF3B82F6),
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Register.route)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}



@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.home),
                contentDescription = "Graduation Celebration",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Congratulations!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Text(
                text = "We've sent you a verification email, please check your inbox and follow the instructions to verify your account.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Thank you for signing up with us!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Sign in here",
                color = Color(0xFF1E88E5),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}


@Composable
fun PermissionScreen(navController: NavController) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Permission Icon",
                tint = Color(0xFF6A1B9A),
                modifier = Modifier.size(100.dp)
            )

            Text(
                text = "Storage Permission Required",
                fontSize = 20.sp,
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "We need access to your photos and videos to continue.",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    permissionLauncher.launch(permissionsToRequest)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .height(50.dp)
            ) {
                Text("Request Permission", color = Color.White)
            }
        }
    }
}
