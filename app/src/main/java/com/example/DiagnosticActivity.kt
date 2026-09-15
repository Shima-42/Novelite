package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.NoveliteTheme

class DiagnosticActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      NoveliteTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          DiagnosticContent()
        }
      }
    }
  }
}

@Composable
fun DiagnosticContent() {
  var count by remember { mutableStateOf(0) }
  var textInput by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "Diagnostic Activity",
      style = MaterialTheme.typography.headlineMedium,
      modifier = Modifier.testTag("diagnostic_title")
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
      text = "Counter: $count",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.testTag("diagnostic_counter_text")
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = { count++ },
      modifier = Modifier.testTag("diagnostic_button")
    ) {
      Text("Increment")
    }
    Spacer(modifier = Modifier.height(24.dp))
    OutlinedTextField(
      value = textInput,
      onValueChange = { textInput = it },
      label = { Text("Test Input") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("diagnostic_text_input")
    )
    Spacer(modifier = Modifier.height(24.dp))
    val context = androidx.compose.ui.platform.LocalContext.current
    Button(
      onClick = {
        val intent = android.content.Intent(context, MainActivity::class.java)
        context.startActivity(intent)
      },
      modifier = Modifier.testTag("launch_main_activity_button")
    ) {
      Text("Open Main App")
    }
  }
}
