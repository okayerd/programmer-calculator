package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.data.local.CalculatorDatabase
import com.example.data.repository.CalculationRepository
import com.example.ui.screens.CalculatorScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ProgrammerCalculatorViewModel

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = CalculatorDatabase.getDatabase(applicationContext)
    val repository = CalculationRepository(database.calculationDao())
    val viewModelFactory = ProgrammerCalculatorViewModel.provideFactory(repository)

    setContent {
      val viewModel: ProgrammerCalculatorViewModel by viewModels { viewModelFactory }
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          CalculatorScreen(viewModel = viewModel)
        }
      }
    }
  }
}

