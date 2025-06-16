package com.example.formvalidation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.formvalidation.ui.theme.FormValidationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormValidationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FormScreen()
                }
            }
        }
    }
}

@Composable
fun FormScreen() {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf(false) }
    var contactError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = false
            },
            label = { Text("Name") },
            isError = nameError,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError) Text("Name is required", color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contact,
            onValueChange = {
                contact = it
                contactError = false
            },
            label = { Text("Contact Number") },
            isError = contactError,
            modifier = Modifier.fillMaxWidth()
        )
        if (contactError) Text("Contact is required", color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            label = { Text("Email") },
            isError = emailError,
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError) Text("Valid email is required", color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = { Text("Password") },
            isError = passwordError,
            modifier = Modifier.fillMaxWidth()
        )
        if (passwordError) Text("Password is required", color = MaterialTheme.colorScheme.error)

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                nameError = name.isBlank()
                contactError = contact.isBlank()
                emailError = !email.contains("@")
                passwordError = password.isBlank()

                val isFormValid = !nameError && !contactError && !emailError && !passwordError

                if (isFormValid) {
                    showDialog = true
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("SUBMIT")
        }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Form Submitted") },
                text = { Text("Hello $name!\nEmail: $email\nContact: $contact") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
