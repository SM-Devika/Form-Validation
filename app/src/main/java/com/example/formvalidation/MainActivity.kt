package com.example.formvalidation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.text.ClickableText
import com.example.formvalidation.viewmodel.UserViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.compose.material3.TopAppBarDefaults
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.shape.CircleShape
import com.example.formvalidation.model.User
import com.example.formvalidation.network.RegisterApiInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.util.Patterns
import androidx.compose.material3.*
import android.util.Log
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.formvalidation.uii.CountryItem
import com.example.formvalidation.uii.CountryDetailScreen
import com.example.formvalidation.uii.CountryListScreen
import com.example.formvalidation.viewmodel.CountryViewModel
import kotlinx.coroutines.delay


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Register : Screen("register")
    object Login : Screen("login")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Permission : Screen("permission")
    object HomeTab : Screen("home_tab")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
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
                    composable(Screen.ForgotPassword.route) {
                        ForgotPasswordScreen(navController)
                    }

                    composable(Screen.Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Screen.Permission.route) {
                        PermissionScreen(navController)
                    }
                    composable(Screen.HomeTab.route) {
                        HomeTabScreen(navController)
                    }
                    composable(Screen.Profile.route) {
                        FullProfileScreen(navController = navController)
                    }
                    composable("countryList") {
                        CountryListScreen(
                            onCountryClick = { country ->
                                navController.navigate("detail/${country.name}/${country.flags.png}")
                            }
                        )
                    }

                    composable(
                        route = "detail/{name}/{flagUrl}/{capital}/{region}/{population}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("flagUrl") { type = NavType.StringType },
                            navArgument("capital") { type = NavType.StringType },
                            navArgument("region") { type = NavType.StringType },
                            navArgument("population") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = Uri.decode(backStackEntry.arguments?.getString("name") ?: "")
                        val flagUrl =
                            Uri.decode(backStackEntry.arguments?.getString("flagUrl") ?: "")
                        val capital =
                            Uri.decode(backStackEntry.arguments?.getString("capital") ?: "N/A")
                        val region =
                            Uri.decode(backStackEntry.arguments?.getString("region") ?: "N/A")
                        val population =
                            Uri.decode(backStackEntry.arguments?.getString("population") ?: "N/A")

                        CountryDetailScreen(
                            navController = navController,
                            name = name,
                            flagUrl = flagUrl,
                            capital = capital,
                            region = region,
                            population = population
                        )
                    }

                    composable("countryList") {
                        CountryListScreen(
                            onCountryClick = { country ->
                                navController.navigate("detail/${country.name}/${country.flags.png}")
                            }
                        )
                    }

                    composable(
                        route = "detail/{name}/{flagUrl}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType },
                            navArgument("flagUrl") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val name = backStackEntry.arguments?.getString("name") ?: ""
                        val flagUrl = backStackEntry.arguments?.getString("flagUrl") ?: ""
                        CountryDetailScreen(name, flagUrl)
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



    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$")
        return passwordRegex.matches(password)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterScreen(navController: NavHostController, viewModel: UserViewModel) {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "SIGN UP!",
                                style = MaterialTheme.typography.titleLarge.copy(color = Color.White)
                            )
                            Text(
                                text = "To get started!",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(0xFF6A1B9A)
                    )
                )
            },
            content = { padding ->
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .align(Alignment.Center)
                            .background(Color.White, shape = RoundedCornerShape(24.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Name") },
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Password") },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                when {
                                    name.isBlank() || email.isBlank() || password.isBlank() -> {
                                        errorMessage = "Please fill all fields"
                                    }
                                    !isValidEmail(email) -> {
                                        errorMessage = "Oops! Please enter a valid email"
                                    }
                                    !isValidPassword(password) -> {
                                        errorMessage = "Oops! Your password doesn't meet the security standards"
                                    }
                                    else -> {
                                        isLoading = true
                                        errorMessage = ""
                                        val user = User(name = name, email = email, password = password)
                                        viewModel.registerUser(user) { success ->
                                            isLoading = false
                                            if (success) {
                                                navController.navigate(Screen.Login.route)
                                            } else {
                                                errorMessage = "Registration failed. Try again."
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                        ) {
                            Text("Sign Up", color = Color.White)
                        }

                        if (isLoading) {
                            Spacer(modifier = Modifier.height(12.dp))
                            CircularProgressIndicator()
                        }

                        if (errorMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(errorMessage, color = MaterialTheme.colorScheme.error)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val annotatedText = buildAnnotatedString {
                            append("Already have an account? ")
                            pushStringAnnotation(tag = "Sign in here", annotation = "login")
                            withStyle(style = SpanStyle(color = Color(0xFF6A1B9A), fontWeight = FontWeight.Bold)) {
                                append("Sign in here")
                            }
                            pop()
                        }

                        ClickableText(
                            text = annotatedText,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                            onClick = { offset ->
                                annotatedText.getStringAnnotations("LOGIN", offset, offset)
                                    .firstOrNull()?.let {
                                        navController.navigate(Screen.Login.route)
                                    }
                            }
                        )
                    }
                }
            }
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginScreen(navController: NavHostController, viewModel: UserViewModel) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var keepSignedIn by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Account Verification") },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (!navController.popBackStack()) {
                                navController.navigate(Screen.Register.route)
                            }
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF3B82F6),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
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
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.ForgotPassword.route)
                            }
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
                        modifier = Modifier.fillMaxWidth()
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
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Please enter all fields"
                            } else {
                                isLoading = true
                                errorMessage = ""
                                viewModel.loginUser(email, password) { success ->
                                    isLoading = false
                                    if (success) {
                                        navController.navigate(Screen.Home.route)
                                    } else {
                                        errorMessage = "Invalid credentials. Please try again."
                                    }
                                }
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

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CircularProgressIndicator()
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
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ForgotPasswordScreen(navController: NavHostController) {
        var email by remember { mutableStateOf("") }
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Forgotten password") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF9C27B0),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Forgot Password?",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF3B82F6))
                    )

                    Text(
                        "Enter your email below to reset your password",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (!isValidEmail(email)) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Invalid email address")
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Password reset link sent")
                                    delay(1000) // wait for 1 second
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset Password", color = Color.White)
                    }
                }
            }
        }
    }



    @Composable
    fun HomeScreen(navController: NavController) {
        val bottomNavController = rememberNavController()

        Scaffold(
            bottomBar = { BottomNavigationBar(bottomNavController) }
        ) { innerPadding ->
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.HomeTab.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.HomeTab.route) {
                    HomeTabScreen(navController = navController)
                }
                composable(Screen.Profile.route) {
                    FullProfileScreen(navController = navController)
                }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        }
    }


    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            Screen.HomeTab,
            Screen.Profile,
            Screen.Settings
        )

        NavigationBar {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route

            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        when (screen) {
                            is Screen.HomeTab -> Icon(Icons.Default.Home, contentDescription = null)
                            is Screen.Profile -> Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null
                            )

                            is Screen.Settings -> Icon(
                                Icons.Default.Settings,
                                contentDescription = null
                            )

                            else -> Icon(Icons.Default.Home, contentDescription = null)
                        }

                    },
                    label = { Text(screen.route.replaceFirstChar { it.uppercase() }) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeTabScreen(navController: NavController) {
        val viewModel: CountryViewModel = viewModel()
        val countries by viewModel.countries.collectAsState()
        val error by viewModel.error.collectAsState()
        var searchQuery by remember { mutableStateOf("") }

        val filteredCountries = if (searchQuery.isEmpty()) {
            countries
        } else {
            countries.filter {
                it.name.common.contains(searchQuery, ignoreCase = true)
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Select Country",
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6200EA)
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search country") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                when {
                    error != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Error: $error", color = MaterialTheme.colorScheme.error)
                        }
                    }

                    countries.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filteredCountries) { country ->
                                CountryItem(
                                    name = country.name.common,
                                    flagUrl = country.flags.png,
                                    onClick = {
                                        val encodedName = Uri.encode(country.name.common)
                                        val encodedFlagUrl = Uri.encode(country.flags.png)
                                        val encodedCapital = Uri.encode(country.capital?.firstOrNull() ?: "N/A")
                                        val encodedRegion = Uri.encode(country.region ?: "N/A")
                                        val encodedPopulation = Uri.encode(country.population?.toString() ?: "N/A")

                                        navController.navigate("detail/$encodedName/$encodedFlagUrl/$encodedCapital/$encodedRegion/$encodedPopulation")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    data class Country(val name: String, val flagUrl: String)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CountryDetailScreen(name: String, flagUrl: String) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(name) }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = flagUrl,
                    contentDescription = "$name Flag",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FullProfileScreen(navController: NavController) {
        var showMenu by remember { mutableStateOf(false) }
        val emailState = remember { mutableStateOf("") }
        val phoneState = remember { mutableStateOf("") }
        val linkedInState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        var profileImageResId by remember { mutableStateOf<Int?>(R.drawable.profile) }

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    title = {
                        Text("Your Profile", color = Color.White)
                    },
                    actions = {
                        IconButton(onClick = { /* Settings click */ }) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF8E24AA)
                    )
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    if (profileImageResId != null) {
                        Image(
                            painter = painterResource(id = profileImageResId!!),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Default Avatar",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Gray, CircleShape),
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier
                            .background(Color.White, shape = CircleShape)
                            .border(1.dp, Color.Gray, shape = CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Change Picture",
                            tint = Color(0xFF8E24AA)
                        )
                    }
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Update Picture") },
                        onClick = {
                            showMenu = false
                            profileImageResId = R.drawable.profile
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Change Picture") },
                        onClick = {
                            showMenu = false
                            // TODO: Add change logic
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Picture") },
                        onClick = {
                            showMenu = false
                            profileImageResId = null
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Devika",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8E24AA)
                )

                Text(
                    text = "Android Developer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileField("Your Email", emailState, Icons.Default.Email)
                ProfileField("Phone Number", phoneState, Icons.Default.Call)
                ProfileField("LinkedIn", linkedInState, Icons.Default.Public)
                ProfileField("Password", passwordState, Icons.Default.Lock)
            }
        }
    }

    @Composable
    fun ProfileField(label: String, textState: MutableState<String>, icon: ImageVector) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                leadingIcon = { Icon(icon, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }



    @Composable
    fun SettingsScreen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("⚙️ Settings Screen", style = MaterialTheme.typography.headlineMedium)
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PermissionScreen(navController: NavController) {
        val context = LocalContext.current
        var permissionGranted by remember { mutableStateOf(false) }

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT).show()
                permissionGranted = true
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

        LaunchedEffect(permissionGranted) {
            if (permissionGranted) {
                navController.navigate(Screen.Register.route) {
                    popUpTo(Screen.Permission.route) { inclusive = true }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Storage Access") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6A1B9A),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
    }
}
