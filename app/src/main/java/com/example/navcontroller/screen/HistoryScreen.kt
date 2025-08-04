package com.example.navcontroller.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.livedata.observeAsState


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navcontroller.model.HistoryEntity
import com.example.navcontroller.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onClear: () -> Unit,
    onDelete: (HistoryEntity) -> Unit
) {
    val forHistoryViewModel: HomeViewModel = hiltViewModel()
    forHistoryViewModel.loadHistory()

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "History",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text("Back", Modifier

                        .clickable { onBack() }
                        .padding(16.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            modifier = Modifier.shadow(4.dp)
        )

        if(forHistoryViewModel.historyList.value.isNotEmpty()) {
            Text(
                "Clear All", Modifier

                    .clickable { onClear() }
                    .padding(16.dp),
                color = Color.Blue
            )
        }


        LazyColumn {
            items(forHistoryViewModel.historyList.value) { item ->
                HistoryCard(item = item, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun HistoryCard(item: HistoryEntity, onDelete: (HistoryEntity) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("From (${item.sourceLangCode}): ${item.sourceText}")
                    Text("To (${item.targetLangCode}): ${item.translatedText}")
                }
                IconButton(onClick = { onDelete(item) }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)


                }
            }
        }
    }
}







