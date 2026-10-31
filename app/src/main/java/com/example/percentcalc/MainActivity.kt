package com.example.percentcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CalcScreen()
                }
            }
        }
    }
}

private fun normalize(s: String): String =
    s.map { c ->
        when (c) {
            in '۰'..'۹' -> '0' + (c - '۰')
            in '٠'..'٩' -> '0' + (c - '٠')
            '٫', ',', '،' -> '.'
            else -> c
        }
    }.joinToString("")

private fun fmt(v: Double): String =
    String.format(Locale.US, "%,.2f", v)

@Composable
fun CalcScreen() {
    var input by remember { mutableStateOf("") }
    val x = normalize(input).toDoubleOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("محاسبه درصد", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("عدد x") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        if (x != null) {
            val line1 = x * 0.46
            val line2 = x * 0.20
            val line3dot = x * 0.34
            val line3 = line1 + line3dot

            ResultRow("Line1  (x × 46%)", line1)
            ResultRow("Line2  (x × 20%)", line2)
            ResultRow("Line3  (Line1 + x × 34%)", line3)
            ResultRow("Line3.  (x × 34%)", line3dot)
            HorizontalDivider()
            ResultRow("Total", x)
        }
    }
}

@Composable
fun ResultRow(label: String, value: Double) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Text(fmt(value), style = MaterialTheme.typography.titleMedium)
        }
    }
}
