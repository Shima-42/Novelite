package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.NoveliteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosticScreen(viewModel: NoveliteViewModel) {
  var count by remember { mutableStateOf(0) }
  val itemsList = remember { List(50) { "Diagnostic List Item #$it" } }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("UI Responsiveness Diagnostic") },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
      )
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Counter: $count",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.testTag("diagnostic_counter_text")
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(
        onClick = { count++ },
        modifier = Modifier.testTag("diagnostic_increment_button")
      ) {
        Text("Increment Counter")
      }
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = "Scrollable List (Isolating Touch & Layout):",
        style = MaterialTheme.typography.titleMedium
      )
      Spacer(modifier = Modifier.height(8.dp))
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .testTag("diagnostic_lazy_column"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(itemsList) { itemText ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .testTag("diagnostic_card_$itemText")
          ) {
            Box(modifier = Modifier.padding(16.dp)) {
              Text(text = itemText, style = MaterialTheme.typography.bodyLarge)
            }
          }
        }
      }
    }
  }
}
